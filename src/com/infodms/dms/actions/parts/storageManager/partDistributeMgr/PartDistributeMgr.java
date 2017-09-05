package com.infodms.dms.actions.parts.storageManager.partDistributeMgr;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartOutstockDao;
import com.infodms.dms.dao.parts.storageManager.partDistributeMgr.PartDistributeMgrDao;
import com.infodms.dms.dao.parts.storageManager.partShelfMgr.PartShelfMgrDao;
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
 * 接收入库
 *
 * @author wucl
 * @date 2014-3-3
 */
public class PartDistributeMgr {

    public Logger logger = Logger.getLogger(PartDistributeMgr.class);
    private final PartDistributeMgrDao plmDao = PartDistributeMgrDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();


    private final String PART_DISTRIBUTE_MANAGE = "/jsp/parts/storageManager/partDistributeMgr/partDistribute.jsp";
    private final String PART_DISTRIBUTE_INIT = "/jsp/parts/storageManager/partDistributeMgr/distributeInit.jsp";
    private final String LOCATION_INIT = "/jsp/parts/storageManager/partDistributeMgr/selectLocation.jsp";
    private final String STORAGE_RECORD_URL = "/jsp/parts/storageManager/partDistributeMgr/storagRecord.jsp";
    /**
     * 接收入库--初始化跳转页面
     *
     * @date 2014-3-3
     * @author wucl
     */
    public void init() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Map<String, Object> map = plmDao.getWaitStorageNumTotal();

            List<OrgBean> beanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
            if (null != beanList && beanList.size() > 0) {
                String dealerId = beanList.get(0).getOrgId() + "";
                List<Map<String, Object>> wareHouseList = PurchasePlanSettingDao.getInstance().getWareHouse(dealerId);
                act.setOutData("wareHouseList", wareHouseList);
                act.setOutData("size", wareHouseList.size());
            }

            act.setOutData("map", map);
            act.setForword(this.PART_DISTRIBUTE_MANAGE);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接收入库--页面初始化失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 接收入库--查询数据
     *
     * @date 2014-3-3
     * @author wucl
     */
    public void query() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String part_code = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String part_name = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
            String TstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));
            String TendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("PART_CODE", part_code);
            map.put("PART_NAME", part_name);
            map.put("TstartDate", TstartDate);
            map.put("TendDate", TendDate);

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = plmDao.getPartLineQuery(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接收入库--数据信息查询失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 接收入库--货位分配跳转页面初始化
     *
     * @date 2014-3-3
     * @author wucl
     */
    public void distributeInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String distributeId = request.getParamValue("Id");
            Map<String, Object> map = plmDao.getObjectById(distributeId);
            act.setOutData("map", map);

            List<Map<String, Object>> locationList = plmDao.getPartLocation();
            act.setOutData("locationList", locationList);

            List<OrgBean> beanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
            if (null != beanList && beanList.size() > 0) {
                String dealerId = beanList.get(0).getOrgId() + "";
                List<Map<String, Object>> wareHouseList = PurchasePlanSettingDao.getInstance().getWareHouse(dealerId);
                act.setOutData("wareHouseList", wareHouseList);
            }

            String partId = map.get("PART_ID").toString();
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = plmDao.getPartLog(distributeId, curPage, Constant.PAGE_SIZE);
            List<Map<String, Object>> list2 = ps.getRecords();
            if (list2 != null && list2.size() > 0) {
                if (list2.get(0).get("LOC_ID") != null) {
                    act.setOutData("LOCID", list2.get(0).get("LOC_ID").toString());
                }
            }
            act.setOutData("distributeId", distributeId);
            act.setForword(this.PART_DISTRIBUTE_INIT);
        } catch (Exception e) {
            BizException be = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位分配跳转页面初始化失败");
            logger.error(logonUser, be);
            act.setException(be);
        }
    }

    /**
     * @author wucl
     * @date 2013-3-4
     * 获取货位已分配信息
     */
    public void getpartList() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
//            String partId = request.getParamValue("id");
            String distributeId = request.getParamValue("distributeId");
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = plmDao.getPartLog(distributeId, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException be = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取货位已分配信息失败");
            logger.error(logonUser, be);
            act.setException(be);
        }
    }

    /**
     * 判断货位编码是否存在
     *
     * @author wucl
     * @date 2014-3-20
     */
    public void checkSeatExist() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String whName = CommonUtils.checkNull(request.getParamValue("whName"));
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
            TtPartLocationPO plp = new TtPartLocationPO();//货位表
            plp.setLocCode(loc_code);
            plp.setWhId(Long.valueOf(whId));
            List<PO> list = plmDao.select(plp);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                plp = (TtPartLocationPO) list.get(0);
                act.setOutData("LOC_ID", plp.getLocId());
                act.setOutData("LOC_CODE", plp.getLocCode());
                act.setOutData("LOC_NAME", plp.getLocName());
                act.setOutData("returnValue", 1);//该货位编码存在
            } else {
                act.setOutData("returnValue", 2);//该货位编码不存在
                act.setOutData("whName", whName);
                act.setOutData("LOC_CODE", plp.getLocCode());
            }
            String part_id = CommonUtils.checkNull(request.getParamValue("PART_ID"));
            if (part_id != "") {
                act.setOutData("PART_ID", part_id);
                act.setOutData("partCode", partCode);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "判断货位编码是否存在出现异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 批量判断货位编码是否存在
     *
     * @author wucl
     * @date 2014-3-20
     */
    public void checkAllSeatExist() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String pId = CommonUtils.checkNull(request.getParamValue("pId"));
            String lineNum = CommonUtils.checkNull(request.getParamValue("lineNum"));
            TtPartLocationPO plp = new TtPartLocationPO();
            plp.setLocCode(loc_code);
            plp.setWhId(Long.valueOf(whId));
            List<PO> list = plmDao.select(plp);

            if (list != null && list.size() > 0 && list.get(0) != null) {
                plp = (TtPartLocationPO) list.get(0);
                act.setOutData("LOC_ID", plp.getLocId());
                act.setOutData("PID", pId);
                act.setOutData("LOC_CODE", plp.getLocCode());
                act.setOutData("LOC_NAME", plp.getLocName());
                act.setOutData("returnValue", 1);//该货位编码存在
            } else {
                act.setOutData("returnValue", 2);//该货位编码不存在
                act.setOutData("lineNum", lineNum);//该货位编码不存在

            }
            String part_id = CommonUtils.checkNull(request.getParamValue("PART_ID"));
            if (part_id != "") {
                act.setOutData("PART_ID", part_id);
            }

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "判断货位编码是否存在出现异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 货位分配处理
     *
     * @date 2014-3-3
     * @author wucl
     */
    public void editSave() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String error = "";
        try {
            String distribute_id = CommonUtils.checkNull(request.getParamValue("DISTRIBUTE_ID"));
            String part_num = CommonUtils.checkNull(request.getParamValue("PART_NUM"));
            String part_id = CommonUtils.checkNull(request.getParamValue("PART_ID"));
            String part_code = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String part_name = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            String[] locs = loc_code.split(",");
            //1.检查分配的货位数量是否合理
            TtPartLocationDistributePO tldp = new TtPartLocationDistributePO();
            tldp.setDistributeId(Long.parseLong(distribute_id));
            List<PO> destribute = plmDao.select(tldp);
            if (destribute != null && destribute.size() > 0 && destribute.get(0) != null) {
                tldp = (TtPartLocationDistributePO) destribute.get(0);
                long erp_num = tldp.getErpStorageNum();
                long finish_storage_num = tldp.getFinishStorageNum();
                long wait_storage_num = tldp.getWaitStorageNum();
                long part_num_new = Long.parseLong(part_num);
                if (part_num_new > wait_storage_num || finish_storage_num > erp_num) {
                    error = "[入库数量]大于[待入库数量]；或是[已入库数量]大于[入库数量]！";
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, error);
                    throw e1;
                }
            }
            //2.检查 tt_part_loaction_define 表 获取loc_id
            List<Map<String, Object>> listLocId = plmDao.getLocId(part_id, locs[0], whId);
            String loc_id = "";
            if (listLocId != null && listLocId.size() > 0 && listLocId.get(0).get("LOC_ID") != null) {
                loc_id = listLocId.get(0).get("LOC_ID").toString();
            } else {
                TtPartLoactionDefinePO dp = new TtPartLoactionDefinePO();
                loc_id = CommonUtils.parseLong(SequenceManager.getSequence("")) + "";
                dp.setLocId(Long.parseLong(loc_id));
                dp.setRelocId(Long.parseLong(locs[0]));
                dp.setLocCode(locs[1]);
                dp.setLocName(locs[2]);
                dp.setPartId(Long.parseLong(part_id));
                dp.setWhId(Long.parseLong(whId));
                dp.setOrgId(logonUser.getOrgId());
                dp.setCreateBy(logonUser.getUserId());
                dp.setCreateDate(new Date());
                dp.setState(Constant.STATUS_ENABLE);
                plmDao.insert(dp);
            }
            //3.插入 tt_part_record 表
            Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("PART_NUM", part_num);
            map.put("PART_ID", part_id);
            map.put("PART_CODE", part_code);
            map.put("PART_NAME", part_name);
            map.put("WH_ID", whId);
            map.put("CONFIG_ID", Constant.PART_CODE_RELATION_43);
            map.put("LOC_ID", loc_id);
            map.put("LOC_CODE", locs[1]);
            map.put("LOC_NAME", locs[2]);
            this.insertRecord(InId, 1, map);
            //4.调用PKG_PART.p_updatepartstock进行入库
            ArrayList<Object> ins = new ArrayList<Object>();
            ins.add(0, InId);
            ins.add(1, Constant.PART_CODE_RELATION_43);
            ins.add(2, 1);
            plmDao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
            //5.更新 tt_part_location_distribute ，更新入库情况
            this.updateDistribute();
            //6.插入tt_part_location_distr_log表，配件货位分配入库记录
            this.insertDistr_log();
            //7.更新在途数量
            TtPartOnwayPO tpop1 = new TtPartOnwayPO();
            tpop1.setPartId(Long.valueOf(part_id));
            List<PO> poList = plmDao.select(tpop1);
            if (poList != null && poList.size() > 0 && poList.get(0) != null) {
                tpop1 = (TtPartOnwayPO) poList.get(0);
                TtPartOnwayPO tpop2 = new TtPartOnwayPO();
                Long updateNum = tpop1.getOnQty() - Long.valueOf(part_num);
                tpop2.setOnQty(updateNum);
                plmDao.update(tpop1, tpop2);
            }
            act.setOutData("returnValue", 1);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 货位分配处理失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 批量货位分配处理
     *
     * @date 2014-3-3
     * @author wucl
     */
    public void editAllSave() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String error = "";
        try {
            String[] soIdArr = CommonUtils.checkNull(request.getParamValue("cbAr")).split(",");
            for (int i = 0; i < soIdArr.length; i++) {
                String[] temp = soIdArr[i].split("@");
                String part_code = temp[0];//配件编码
                String part_num = temp[1];//货位入库数量
                String wait_num = temp[2];//待入库数量
                String distribute_id = temp[3];//主表ID
                String part_id = temp[4];//配件ID
                String ERP_STORAGE_NUM = temp[5];//erp入库数量
                String FINISH_STORAGE_NUM = temp[6];//已入库数量
                String whId = temp[7];//库房ID
                String LOC_ID = temp[8];//货位ID
                String LOC_CODE = temp[9];//货位code
                String LOC_NAME = temp[10];//货位名字

                //根据配件code取得配件名字
                TtPartDefinePO po = new TtPartDefinePO();
                po.setPartOldcode(part_code);
                TtPartDefinePO poValue = new TtPartDefinePO();
                poValue = (TtPartDefinePO) plmDao.select(po).get(0);

                String part_name = poValue.getPartCname();//配件名称

                //1.检查分配的货位数量是否合理
                TtPartLocationDistributePO tldp = new TtPartLocationDistributePO();
                tldp.setDistributeId(Long.parseLong(distribute_id));
                List<PO> destribute = plmDao.select(tldp);
                if (destribute != null && destribute.size() > 0 && destribute.get(0) != null) {
                    tldp = (TtPartLocationDistributePO) destribute.get(0);
                    long erp_num = tldp.getErpStorageNum();
                    long finish_storage_num = tldp.getFinishStorageNum();
                    long wait_storage_num = tldp.getWaitStorageNum();
                    long part_num_new = Long.parseLong(part_num);
                    if (part_num_new > wait_storage_num || finish_storage_num > erp_num) {
                        error = "配件【" + part_code + "】已入库数量大于U9入库数量,请刷新页面避免重复入库！";
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, error);
                        act.setOutData("error", error);
                        return;
                    }
                }
                //2.检查 tt_part_loaction_define 表 获取loc_id
                List<Map<String, Object>> listLocId = plmDao.getLocId(part_id, LOC_ID, whId);
                String loc_id = "";
                if (listLocId != null && listLocId.size() > 0 && listLocId.get(0).get("LOC_ID") != null) {
                    loc_id = listLocId.get(0).get("LOC_ID").toString();
                } else {
                    TtPartLoactionDefinePO dp = new TtPartLoactionDefinePO();
                    loc_id = CommonUtils.parseLong(SequenceManager.getSequence("")) + "";
                    dp.setLocId(Long.parseLong(loc_id));
                    dp.setRelocId(Long.parseLong(LOC_ID));
                    dp.setLocCode(LOC_CODE);
                    dp.setLocName(LOC_NAME);
                    dp.setPartId(Long.parseLong(part_id));
                    dp.setWhId(Long.parseLong(whId));
                    dp.setOrgId(logonUser.getOrgId());
                    dp.setCreateBy(logonUser.getUserId());
                    dp.setCreateDate(new Date());
                    dp.setState(Constant.STATUS_ENABLE);
                    plmDao.insert(dp);
                }
                //3.插入 tt_part_record 表
                Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PART_NUM", part_num);
                map.put("PART_ID", part_id);
                map.put("PART_CODE", part_code);
                map.put("PART_NAME", part_name);
                map.put("WH_ID", whId);
                map.put("CONFIG_ID", Constant.PART_CODE_RELATION_43);
                map.put("LOC_ID", loc_id);
                map.put("LOC_CODE", LOC_CODE);
                map.put("LOC_NAME", LOC_NAME);
                map.put("WAIT_NUM", wait_num);
                map.put("ERP_STORAGE_NUM", ERP_STORAGE_NUM);//erp入库数量
                map.put("FINISH_STORAGE_NUM", FINISH_STORAGE_NUM);//已入库数量
                map.put("DISTRIBUTE_ID", distribute_id);//主表ID
                map.put("RELOC_ID", LOC_ID);//实际货位ID

                this.insertRecord(InId, 1, map);
                //4.调用PKG_PART.p_updatepartstock进行入库
                ArrayList<Object> ins = new ArrayList<Object>();
                ins.add(0, InId);
                ins.add(1, Constant.PART_CODE_RELATION_43);
                ins.add(2, 1);
                plmDao.callProcedure("PKG_PART.p_updatepartstock", ins, null);

                //5.更新 tt_part_location_distribute ，更新入库情况
                this.updateAllDistribute(map);

                //6.插入tt_part_location_distr_log表，配件货位分配入库记录
                this.insertAllDistr_log(map);

                //7.更新在途数量
                System.out.println("--------------更新在途数量");
                TtPartOnwayPO tpop1 = new TtPartOnwayPO();
                tpop1.setPartId(Long.valueOf(part_id));
                List<PO> poList = plmDao.select(tpop1);
                if (poList != null && poList.size() > 0 && poList.get(0) != null) {
                    tpop1 = (TtPartOnwayPO) poList.get(0);
                    TtPartOnwayPO tpop2 = new TtPartOnwayPO();
                    Long updateNum = tpop1.getOnQty() - Long.valueOf(part_num);
                    tpop2.setOnQty(updateNum);
                    plmDao.update(tpop1, tpop2);
                }
            }
            act.setOutData("returnValue", 1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "接收入库失败，请联系管理员！");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    private void updateAllDistribute(Map<String, Object> map) throws Exception {

        System.out.println("开始");
        String part_num = map.get("PART_NUM").toString();
        String finish_storage_num = map.get("FINISH_STORAGE_NUM").toString();
        String wait_storage_num = map.get("WAIT_NUM").toString();


        long finish_storage_num_new = Long.parseLong(finish_storage_num) + Long.parseLong(part_num);
        long wait_storage_num_new = Long.parseLong(wait_storage_num) - Long.parseLong(part_num);

        TtPartLocationDistributePO tp1 = new TtPartLocationDistributePO();
        tp1.setFinishStorageNum(finish_storage_num_new);
        tp1.setWaitStorageNum(wait_storage_num_new);
        TtPartLocationDistributePO tp2 = new TtPartLocationDistributePO();
        String distribute_id = map.get("DISTRIBUTE_ID").toString();
        tp2.setDistributeId(Long.parseLong(distribute_id));
        plmDao.update(tp2, tp1);
    }

    private boolean insertAllDistr_log(Map<String, Object> map) throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        String part_num = map.get("PART_NUM").toString();
        String part_id = map.get("PART_ID").toString();
        String part_code = map.get("PART_CODE").toString();
        String part_name = map.get("PART_NAME").toString();
        String whId = map.get("WH_ID").toString();
        String loc_id = map.get("RELOC_ID").toString();//取实际货位
        String loc_code = map.get("LOC_CODE").toString();
        String loc_name = map.get("LOC_NAME").toString();
        String distribute_id = map.get("DISTRIBUTE_ID").toString();
        String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_55);

        VwPartStockPO stockPO = new VwPartStockPO();
        stockPO.setPartId(Long.valueOf(part_id));
        stockPO.setWhId(Long.valueOf(map.get("WH_ID").toString()));
        stockPO.setLocCode(map.get("LOC_CODE").toString());

        stockPO = (VwPartStockPO) plmDao.select(stockPO).get(0);

        //配件货位分配入库记录信息
        TtPartLocationDistrLogPO tapo = new TtPartLocationDistrLogPO();
        String distriLogId = SequenceManager.getSequence(null);
        tapo.setDistriLogId(Long.parseLong(distriLogId));
        tapo.setDistriCode(orderCode);
        tapo.setDistributeId(Long.parseLong(distribute_id));
        tapo.setPartId(Long.parseLong(part_id));
        tapo.setPartCode(part_code);
        tapo.setPartName(part_name);
        tapo.setPartNum(Long.parseLong(part_num));
        //tapo.setOemMinPkg(Long.parseLong(oemMinPkg));
        tapo.setWhId(Long.parseLong(whId));
        //tapo.setRemarks(remarks);
        tapo.setLocId(Long.parseLong(loc_id));
        tapo.setLocCode(loc_code);
        tapo.setLocName(loc_name);
        tapo.setStorageDate(new Date());
        tapo.setCreateDate(new Date());
        tapo.setCreateBy(logonUser.getUserId());
        //获取当前库存
        if (stockPO.getItemQty() != null) {
            tapo.setItemQty(stockPO.getItemQty());
        }
        plmDao.insert(tapo);
        return true;
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
        plmDao.insert(insertPRPo);
    }

    private void updateDistribute() throws Exception {

        String part_num = CommonUtils.checkNull(request.getParamValue("PART_NUM"));
        String finish_storage_num = CommonUtils.checkNull(request.getParamValue("FINISH_STORAGE_NUM")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("FINISH_STORAGE_NUM"));
        String wait_storage_num = CommonUtils.checkNull(request.getParamValue("WAIT_STORAGE_NUM")) == "" ? "0" : CommonUtils.checkNull(request.getParamValue("WAIT_STORAGE_NUM"));
        long finish_storage_num_new = Long.parseLong(finish_storage_num) + Long.parseLong(part_num);
        long wait_storage_num_new = Long.parseLong(wait_storage_num) - Long.parseLong(part_num);

        TtPartLocationDistributePO tp1 = new TtPartLocationDistributePO();
        tp1.setFinishStorageNum(finish_storage_num_new);
        tp1.setWaitStorageNum(wait_storage_num_new);
        TtPartLocationDistributePO tp2 = new TtPartLocationDistributePO();
        String distribute_id = CommonUtils.checkNull(request.getParamValue("DISTRIBUTE_ID"));
        tp2.setDistributeId(Long.parseLong(distribute_id));
        plmDao.update(tp2, tp1);
    }

    private boolean insertDistr_log() throws Exception {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        String part_num = CommonUtils.checkNull(request.getParamValue("PART_NUM"));
        String remarks = CommonUtils.checkNull(request.getParamValue("REMARKS"));
        String distribute_id = CommonUtils.checkNull(request.getParamValue("DISTRIBUTE_ID"));
        String part_id = CommonUtils.checkNull(request.getParamValue("PART_ID"));
        String part_code = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
        String part_name = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
        String oemMinPkg = CommonUtils.checkNull(request.getParamValue("OEM_MIN_PKG")) == "" ? "1" : CommonUtils.checkNull(request.getParamValue("OEM_MIN_PKG"));
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
        String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
        String[] locs = loc_code.split(",");
        String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_55);
        VwPartStockPO stockPO = new VwPartStockPO();
        stockPO.setPartId(Long.valueOf(part_id));
        stockPO.setWhId(Long.valueOf(whId));
        stockPO.setLocCode(locs[1]);

        stockPO = (VwPartStockPO) plmDao.select(stockPO).get(0);

        //配件货位分配入库记录信息
        TtPartLocationDistrLogPO tapo = new TtPartLocationDistrLogPO();
        String distriLogId = SequenceManager.getSequence(null);
        tapo.setDistriLogId(Long.parseLong(distriLogId));
        tapo.setDistriCode(orderCode);
        tapo.setDistributeId(Long.parseLong(distribute_id));
        tapo.setPartId(Long.parseLong(part_id));
        tapo.setPartCode(part_code);
        tapo.setPartName(part_name);
        tapo.setPartNum(Long.parseLong(part_num));
        tapo.setOemMinPkg(Long.parseLong(oemMinPkg));
        tapo.setWhId(Long.parseLong(whId));
        tapo.setRemarks(remarks);
        tapo.setLocId(Long.parseLong(locs[0]));
        tapo.setLocCode(locs[1]);
        tapo.setLocName(locs[2]);
        tapo.setStorageDate(new Date());
        tapo.setCreateDate(new Date());
        tapo.setCreateBy(logonUser.getUserId());
        //获取当前库存
        if (stockPO.getItemQty() != null) {
            tapo.setItemQty(stockPO.getItemQty());
        }
        plmDao.insert(tapo);
        return true;
    }

    public void redistributeInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        //重新分配回滚操作
        try {
            String distri_log_id = request.getParamValue("Id");
            TtPartLocationDistrLogPO tp = new TtPartLocationDistrLogPO();
            tp.setDistriLogId(Long.parseLong(distri_log_id));
            List<PO> listtp = plmDao.select(tp);
            //1.校验 TT_PART_ITEM_STOCK ,当前库存
            if (listtp != null && listtp.size() > 0 && listtp.get(0) != null) {
                tp = (TtPartLocationDistrLogPO) listtp.get(0);
                Map<String, Object> map_q = plmDao.getITEM_QTY(distri_log_id);
                if (map_q != null) {
                    if (map_q.get("ITEM_QTY") != null) {
                        long item_qty = Long.parseLong(map_q.get("ITEM_QTY").toString());//当前库存数量
                        long bookedQty = Long.parseLong(map_q.get("BOOKED_QTY").toString());//已占用数量
                        long normalQty = Long.parseLong(map_q.get("NORMAL_QTY").toString());//已占用数量
                        long part_num = tp.getPartNum();
                        //取消数量小于等于当前账面库存并且等于小于可用数量才可以取消 20140323
                        if (item_qty < part_num || normalQty < part_num) {
                            act.setOutData("returnValue", "3");
                            act.setOutData("normalQty", normalQty);
                            return;
                        }
                    }
                } else {
                    act.setOutData("returnValue", "3");
                    return;
                }

            }
            //2.插入 tt_part_record 表
            TtPartLoactionDefinePO definePo = new TtPartLoactionDefinePO();
            definePo.setRelocId(tp.getLocId());
            definePo.setWhId(tp.getWhId());
            definePo.setPartId(tp.getPartId());
            List<PO> defineList = plmDao.select(definePo);
            if (defineList != null && defineList.size() > 0 && defineList.get(0) != null) {
                definePo = (TtPartLoactionDefinePO) defineList.get(0);
            }
            Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("PART_NUM", tp.getPartNum());
            map2.put("PART_ID", tp.getPartId());
            map2.put("PART_CODE", tp.getPartCode());
            map2.put("PART_NAME", tp.getPartName());
            map2.put("WH_ID", tp.getWhId());
            map2.put("CONFIG_ID", Constant.PART_CODE_RELATION_44);
            map2.put("LOC_ID", definePo.getLocId());
            map2.put("LOC_CODE", tp.getLocCode());
            map2.put("LOC_NAME", tp.getLocName());
            this.insertRecord(InId, 2, map2);
            //3.调用PKG_PART.p_updatepartstock进行出库
            ArrayList<Object> ins = new ArrayList<Object>();
            ins.add(0, InId);
            ins.add(1, Constant.PART_CODE_RELATION_44);
            ins.add(2, 0);//非占用直接出库
            plmDao.callProcedure("PKG_PART.p_updatepartstock", ins, null);
            //4.更新 tt_part_location_distribute ，更新入库情况
            if (listtp != null && listtp.size() > 0) {
                tp = (TtPartLocationDistrLogPO) listtp.get(0);
                TtPartLocationDistributePO tdp = new TtPartLocationDistributePO();
                tdp.setDistributeId(tp.getDistributeId());
                List<PO> listtdp = plmDao.select(tdp);
                if (listtdp != null && listtdp.size() > 0) {
                    tdp = (TtPartLocationDistributePO) listtdp.get(0);
                    long part_num = tp.getPartNum();
                    long finish_num = tdp.getFinishStorageNum();
                    long wait_num = tdp.getWaitStorageNum();
                    long finish_storage_num_new = finish_num - part_num;
                    long wait_storage_num_new = wait_num + part_num;
                    TtPartLocationDistributePO tp1 = new TtPartLocationDistributePO();
                    tp1.setFinishStorageNum(finish_storage_num_new);
                    tp1.setWaitStorageNum(wait_storage_num_new);
                    TtPartLocationDistributePO tp2 = new TtPartLocationDistributePO();
                    long distribute_id = tdp.getDistributeId();
                    tp2.setDistributeId(distribute_id);
                    plmDao.update(tp2, tp1);
                }
            }
            //5.删除tt_part_location_distr_log表，配件货位分配入库记录
            TtPartLocationDistrLogPO t1 = new TtPartLocationDistrLogPO();
            t1.setDistriLogId(Long.parseLong(distri_log_id));
            TtPartLocationDistrLogPO t2 = new TtPartLocationDistrLogPO();
            t2.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));
            t2.setUpdateBy(logonUser.getUserId());
            t2.setState(Constant.STATUS_DISABLE);
            t2.setStatus(2);
            plmDao.update(t1, t2);

            act.setOutData("returnValue", "1");
        } catch (Exception e) {
            BizException be = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "重新分配失败");
            logger.error(logonUser, be);
            act.setException(be);
        }
    }

    /**
     * @author wucl
     * @date 2014-3-5
     * 货位选择--跳转到货位选择窗口
     */
    public void selectLocationInit() {
        String loc_id = CommonUtils.checkNull(request.getParamValue("loc_id"));
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));
        if (loc_id != "" && loc_id != null) {
            act.setOutData("loc_id", loc_id);
        }

        List<Map<String, Object>> listLine = PartShelfMgrDao.getInstance().getLineCodeList(whId);
        act.setOutData("listLine", listLine);
        act.setOutData("whId", whId);
        
        System.out.println("loc_id init:"+loc_id);
        System.out.println("whId init:"+whId);
        act.setForword(this.LOCATION_INIT);
    }

    /**
     * @author wucl
     * @date 2014-3-5
     * 货位选择--查询货位信息(货位)
     */
    public void selectLocation() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String lineID = CommonUtils.checkNull(request.getParamValue("LINE_ID"));
            String shelfID = CommonUtils.checkNull(request.getParamValue("SHELF_ID"));
            String floorID = CommonUtils.checkNull(request.getParamValue("FLOOR_ID"));
            String position_code = CommonUtils.checkNull(request.getParamValue("POSITION_CODE"));
            String locCode = CommonUtils.checkNull(request.getParamValue("locCode"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
System.out.println("whId:"+whId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("LINE_ID", lineID);
            map.put("SHELF_ID", shelfID);
            map.put("FLOOR_ID", floorID);
            map.put("POSITION_CODE", position_code);
            map.put("loc_Code", locCode);
            map.put("part_OldCode", partOldCode);
            map.put("whId", whId);

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = plmDao.queryLocation(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "货位选择--查询货位信息失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: 入库记录查询初始化
     */
    public void queryStorageRecordInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            String buttonFalg = "";
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            act.setOutData("buttonFalg", buttonFalg);
            act.setOutData("flag", flag);
            act.setForword(STORAGE_RECORD_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件入库记录");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }
    /**
     * @author wucl
     * @date 2013-3-4
     * 获取入库记录
     */
    public void queryStorageRecord() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = plmDao.getStorageRecord(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException be = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取入库记录失败");
            logger.error(logonUser, be);
            act.setException(be);
        }
    }
}
