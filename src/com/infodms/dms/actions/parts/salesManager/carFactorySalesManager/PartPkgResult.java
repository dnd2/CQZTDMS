package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.salesManager.PartPickOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartPkgDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPkgBoxDtlPO;
import com.infodms.dms.po.TtPartPkgDtlPO;
import com.infodms.dms.po.TtPartSoMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PartPkgResult extends BaseImport {
    public Logger logger = Logger.getLogger(PartPkgResult.class);
    String PART_PKG_RESULT_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgResultMain.jsp";
    String PART_PKG_RESULT_MOD_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgResultMod.jsp";
    String PART_PKG_RESULT_DETAIL_MOD_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgResultDetailMod.jsp";
    String PART_PKG_BOX_MOD_URL = "/jsp/parts/salesManager/carFactorySalesManager/partPkgBoxlMod.jsp";

    PartPkgDao partPkgDao = PartPkgDao.getInstance();

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setForword(PART_PKG_RESULT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryPkgResult(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void queryPkgInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.getPkgInfo(request, curPage, Constant.PAGE_SIZE_MIDDLE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void queryUnPkgedPartInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartPickOrderDao dao = PartPickOrderDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryUnPkgedPartInfo(request, loginUser, curPage, Constant.PAGE_SIZE_MAX);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void rollbackOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        String err = "配件装箱取消失败";
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            //validateState(pickOrderId);
            //回退状态
            //dao.rollbackSoMainState(pickOrderId);
            if (dao.pkgCancelable(pickOrderId, null)) {
                err = "捡货单【" + pickOrderId + "】已经产生发运单，不能取消装箱单!";
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件装箱取消失败!");
                throw e1;
            }
            TtPartSoMainPO soMainPO1 = new TtPartSoMainPO();
            TtPartSoMainPO soMainPO2 = new TtPartSoMainPO();
            soMainPO1.setPickOrderId(pickOrderId);
            soMainPO2.setState(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);

            dao.update(soMainPO1, soMainPO2);

            //清理装箱明细
            dao.cleanPkgDtl(pickOrderId);
            //清除包装信息
            dao.cleanPkgInfo(pickOrderId);
            act.setOutData("success", "取消成功!");
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, err);
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 取消装箱(单个箱子)
     */
    public void rollbackOrder1() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        String err = "配件装箱取消失败";
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
            //回退状态
            //dao.rollbackSoMainState(pickOrderId);
            if (dao.pkgCancelable(pickOrderId, pkgNo)) {
                err = "箱号【" + pkgNo + "】已经生成发运单，不能取消!";
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件装箱取消失败!");
                throw e1;
            }
            //删除装箱明细并删除该拣货单产生的现场BO
            TtPartPkgDtlPO dtlPO = new TtPartPkgDtlPO();
            dtlPO.setPickOrderId(pickOrderId);
            dtlPO.setPkgNo(pkgNo);

            dao.cleanBoDtl(pickOrderId);

            //同时删除该箱子
            TtPartPkgBoxDtlPO boxDtlPO = new TtPartPkgBoxDtlPO();
            boxDtlPO.setPickOrderId(pickOrderId);
            boxDtlPO.setPkgNo(pkgNo);
            dao.delete(dtlPO);
            dao.delete(boxDtlPO);

            //判断该拣货单是否已经全部取消装箱,如果已经全部取消就更新状态为财务审核通过,否则为装箱中
            dao.updateSoMainState(pickOrderId);

            act.setOutData("success", "取消成功!");
        } catch (Exception e) {//异常方法
            POContext.endTxn(false);
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, err);
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void modifyOrderPage() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            //validateState(pickOrderId);
            //获取数据
            /*List<Map<String,Object>> dtlList = dao.getPkgDetail(pickOrderId);
            act.setOutData("dtlList", dtlList);*/
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            String dealerId = CommonUtils.checkNull((mainMap.get("DEALER_ID")));

            act.setOutData("dealerId", dealerId);
            act.setOutData("whId", whId);
            act.setOutData("pickOrderId", pickOrderId);
            act.setForword(PART_PKG_RESULT_MOD_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PKG_RESULT_URL);
        }
    }

    public void modifyDtlPage() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            Map<String, Object> map = dao.getPkg(pickOrderId, pkgNo);
            //获取数据
            List<Map<String, Object>> dtlList = dao.getPkgDetail(pkgNo, pickOrderId);
            act.setOutData("dtlList", dtlList);
            act.setOutData("map", map);
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("whId", whId);
            act.setOutData("pkgNo", pkgNo);
            act.setForword(PART_PKG_RESULT_DETAIL_MOD_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱查询失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_PKG_RESULT_URL);
        }
    }

    public void modifyOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String boxIdPkgNo = CommonUtils.checkNull(request.getParamValue("boxId_pkgNo"));
            String boxId = boxIdPkgNo.split(",")[0];
            String pkgNo = boxIdPkgNo.split(",")[1];
            //List<Map<String,Object>> list = dao.getPkgDetail(pkgNo,pickOrderId);
            Map<String, Object> map = dao.getPkg(pickOrderId, pkgNo);
            //act.setOutData("list", list);
            act.setOutData("map", map);
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("pkgNo", pkgNo);
            act.setOutData("boxId", boxId);
            act.setForword(PART_PKG_BOX_MOD_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件装箱修改失败!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 修改装箱信息
     */
    public void modPkg() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("pkgNo"));//现在修改的箱号
            String newPkgNo = CommonUtils.checkNull(request.getParamValue("newPkgNo"));//现在修改的箱号
            String boxId = CommonUtils.checkNull(request.getParamValue("boxId"));//
            String box_len = CommonUtils.checkNull(request.getParamValue("BOX_LEN"));//包装尺寸(长)
            String box_wid = CommonUtils.checkNull(request.getParamValue("BOX_WID"));//包装尺寸(宽)
            String box_hei = CommonUtils.checkNull(request.getParamValue("BOX_HEI"));//包装尺寸(高)
            String volume = CommonUtils.checkNull(request.getParamValue("VOLUME"));//体积
            String box_wei = CommonUtils.checkNull(request.getParamValue("BOX_WEI"));//单箱重量
            String ch_weight = CommonUtils.checkNull(request.getParamValue("CH_WEIGHT"));//计费重量
            String eq_weight = CommonUtils.checkNull(request.getParamValue("EQ_WEIGHT"));//折合重量

            //更新包装信息
            TtPartPkgBoxDtlPO boxDtlPO1 = new TtPartPkgBoxDtlPO();
            TtPartPkgBoxDtlPO boxDtlPO2 = new TtPartPkgBoxDtlPO();
            boxDtlPO1.setPkgNo(pkgNo);
            boxDtlPO1.setPickOrderId(pickOrderId);

            boxDtlPO2.setLength(CommonUtils.parseDouble(box_len));
            boxDtlPO2.setWidth(CommonUtils.parseDouble(box_wid));
            boxDtlPO2.setHeight(CommonUtils.parseDouble(box_hei));
            boxDtlPO2.setWeight(CommonUtils.parseDouble(box_wei));
            boxDtlPO2.setVolume(CommonUtils.parseDouble(volume));
            boxDtlPO2.setEqWeight(CommonUtils.parseDouble(eq_weight));
            boxDtlPO2.setChWeight(CommonUtils.parseDouble(ch_weight));
            boxDtlPO2.setUpdateDate(new Date());
            boxDtlPO2.setUpdateBy(loginUser.getUserId());
            boxDtlPO2.setPkgNo(newPkgNo);

            //更新装箱信息
            TtPartPkgDtlPO pkgDtlPO = new TtPartPkgDtlPO();
            pkgDtlPO.setPickOrderId(pickOrderId);
            pkgDtlPO.setPkgNo(pkgNo);

            TtPartPkgDtlPO pkgDtlPO1 = new TtPartPkgDtlPO();
            pkgDtlPO1.setPkgNo(newPkgNo);
            pkgDtlPO1.setUpdateBy(loginUser.getUserId());
            pkgDtlPO1.setUpdateDate(new Date());

            dao.update(pkgDtlPO, pkgDtlPO1);

            dao.update(boxDtlPO1, boxDtlPO2);

            act.setOutData("success", "修改成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱信息修改失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    /**
     * 装箱结果修改之装箱明细修改
     * 此处存在2种方式，
     * 1、修改已装箱的明细，向其中添加装箱明细或者修改装箱数量
     * 2、将未装修的明细装到一个新增加的箱子中
     */
    @SuppressWarnings("unchecked")
	public void saveModify() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        String err;
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//捡货单号
            String pkgNo = CommonUtils.checkNull(request.getParamValue("PKG_NO"));//输入的箱号
            if (dao.pkgCancelable(pickOrderId, pkgNo)) {
                err = "箱号【" + pkgNo + "】已经打印发运单，不能修改装箱明细!";
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            }
            String[] partIds = request.getParamValues("cb");//明细ID

            String box_len = "";//包装尺寸(长)
            String box_wid = "";//包装尺寸(宽)
            String box_hei = "";//包装尺寸(高)
            String volume = "";//体积
            String box_wei = "";//单箱重量
            String ch_weight = "";//计费重量
            String eq_weight = "";//折合重量

            //判断拣货单和箱号是否已存在
            boolean pkgNoFlag = dao.queryPkgBoxInfo(pickOrderId, pkgNo);
            //无装箱信息则插入新的装箱信息
            if (!pkgNoFlag && !"".equals(pkgNo)) {
                box_len = CommonUtils.checkNull4Default(request.getParamValue("BOX_LEN"), "0");//包装尺寸(长)
                box_wid = CommonUtils.checkNull4Default(request.getParamValue("BOX_WID"), "0");//包装尺寸(宽)
                box_hei = CommonUtils.checkNull4Default(request.getParamValue("BOX_HEI"), "0");//包装尺寸(高)
                volume = CommonUtils.checkNull4Default(request.getParamValue("VOLUME"), "0");//体积
                box_wei = CommonUtils.checkNull4Default(request.getParamValue("BOX_WEI"), "0");//单箱重量
                ch_weight = CommonUtils.checkNull4Default(request.getParamValue("CH_WEIGHT"), "0");//计费重量
                eq_weight = CommonUtils.checkNull4Default(request.getParamValue("EQ_WEIGHT"), "0");//折合重量
                //包装明细信息tt_part_pkg_box_dtl
                TtPartPkgBoxDtlPO boxDtlPO = new TtPartPkgBoxDtlPO();
                boxDtlPO.setBoxId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                boxDtlPO.setPickOrderId(pickOrderId);
                boxDtlPO.setPkgNo(pkgNo);
                boxDtlPO.setLength(CommonUtils.parseDouble(box_len));
                boxDtlPO.setWidth(CommonUtils.parseDouble(box_wid));
                boxDtlPO.setHeight(CommonUtils.parseDouble(box_hei));
                boxDtlPO.setWeight(CommonUtils.parseDouble(box_wei));
                boxDtlPO.setVolume(CommonUtils.parseDouble(volume));
                boxDtlPO.setEqWeight(CommonUtils.parseDouble(eq_weight));
                boxDtlPO.setChWeight(CommonUtils.parseDouble(ch_weight));
                boxDtlPO.setCreateDate(new Date());
                boxDtlPO.setCreateBy(loginUser.getUserId());

                dao.insert(boxDtlPO);
            }

            //判断箱号是否还允许修改
            boolean flag = dao.isUpdateable(pickOrderId, pkgNo);
            if (!flag) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "箱号【" + pkgNo + "】已出库,不能修改!");
                throw e1;
            }

            //获取当前销售单的装箱状态
            TtPartSoMainPO soMainPO = new TtPartSoMainPO();
            soMainPO.setPickOrderId(pickOrderId);
            soMainPO = (TtPartSoMainPO) dao.select(soMainPO).get(0);
            Integer state = soMainPO.getState();
            //20170901 add start
            Long soId = soMainPO.getSoId();
            //20170901 add end

            //两种装箱方式   ：1：正常  2：按照最小装箱量
            //按照最小装箱量集合
            List<TtPartPkgDtlPO> minPkgList = new ArrayList<TtPartPkgDtlPO>();
            //按照正常装
            List<TtPartPkgDtlPO> normalList = new ArrayList<TtPartPkgDtlPO>();
            List<String> pkgIdList = new ArrayList<String>();
            request.setAttribute("pkgIdList", pkgIdList);
            //获取仓库ID
            List<Map<String, Object>> soOrderList = dao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);
            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));

            //通过箱号查询查询箱号id
            TtPartPkgBoxDtlPO selBoxPo=new TtPartPkgBoxDtlPO();
            selBoxPo.setPkgNo(pkgNo);
            selBoxPo.setPickOrderId(pickOrderId);
            selBoxPo.setStatus(1);
            selBoxPo = (TtPartPkgBoxDtlPO)dao.select(selBoxPo).get(0);
            if(selBoxPo==null){
            	 BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "箱号【" + pkgNo + "】未查找到箱信息!");
                 throw e1;
            }
            Long boxId=selBoxPo.getBoxId();
            
            //循环遍历装箱明细
            for (int i = 0; i < partIds.length; i++) {
                String pkgQtyStr = CommonUtils.checkNull(request.getParamValue("pkgQty_" + partIds[i]));//装箱数量
                String salQtyStr = CommonUtils.checkNull(request.getParamValue("salQty_" + partIds[i]));//销售数量
                String dtlPkgNoStr = CommonUtils.checkNull(request.getParamValue("pkgNo_" + partIds[i]));//配件对应的箱号 （因目前只有修改，箱号和上面PKG_NO值是一样的）
                String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE" + partIds[i]));
                String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME" + partIds[i]));
                String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE" + partIds[i]));
                String locName = CommonUtils.checkNull(request.getParamValue("LOC_NAME" + partIds[i]));
                String remark = CommonUtils.checkNull(request.getParamValue("remark_" + partIds[i]));
                String unit = CommonUtils.checkNull(request.getParamValue("UNIT" + partIds[i]));
                String batchNo = CommonUtils.checkNull(request.getParamValue("BATCHNO_" + partIds[i]));//批次号  20170901 add

                
                long pkgQtyPage = CommonUtils.parseLong(pkgQtyStr);
                long salQty = CommonUtils.parseLong(salQtyStr);
                String partId = partIds[i].split(",")[0];//配件ID
                String locId = partIds[i].split(",")[1];//配件对应的货位ID
                String errInfo = "";
                String pkgId = CommonUtils.checkNull(request.getParamValue("PKG_ID" + partIds[i]));

                //修改装箱明细数量修改为0的特殊情况，存在2种情况
                //1、完全现场BO(即配件一个都没有装箱)
                //2、部分现场BO(即配件未完全整装箱，部分数量装到某几个箱中)
                if ("0".equals(pkgQtyStr) && !"".equals(dtlPkgNoStr)) {
                    //如果当前拣货单已经完成装箱,由于这里不会改变状态,此时需要修改现场BO数量
                    if (state.intValue() == Constant.CAR_FACTORY_PKG_STATE_02.intValue()) {
                        //查询配件对应的已装箱明细信息
                        TtPartPkgDtlPO pkgDtlPO = new TtPartPkgDtlPO();
                        pkgDtlPO.setPkgId(CommonUtils.parseLong(pkgId));
                        pkgDtlPO.setBatchNo(batchNo);//20170904 add
                        pkgDtlPO = (TtPartPkgDtlPO) dao.select(pkgDtlPO).get(0);
                        Long pkgQty = pkgDtlPO.getPkgQty();
                        //查询现场BO明细信息
                        Map<String, Object> map = partPkgDao.queryPkgId(pickOrderId, partId, locId,batchNo);
                        //有则更新现场BO数量、//无明细增加现场BO明细
                        if (map != null && map.get("PKG_ID") != null) {
                            Long pkgIdBo = ((BigDecimal) map.get("PKG_ID")).longValue();
                            partPkgDao.updateBodtl(pkgIdBo, pkgQty,batchNo);
                        } else {
                            insertPkgDtlBo(pkgDtlPO, pkgQty);
                        }
                    }
                    //删除该装箱明细
                    TtPartPkgDtlPO dtlPO = new TtPartPkgDtlPO();
                    dtlPO.setPkgId(CommonUtils.parseLong(pkgId));
                    dtlPO.setBatchNo(batchNo);//20170904 add
                    dao.delete(dtlPO);
                } else {
                    //新增或者修改装箱明细
                    //获取当前货位下的配件的所有已装箱数量
                    Map<String, Object> map = dao.queryAllPkgedQty(pickOrderId, partId, locId,batchNo);
                    long allPkgQty = 0l;
                    long spAllPkgQty = 0l;
                    long xcBOQty = 0l;

                    if (map != null) {
                        allPkgQty = ((BigDecimal) map.get("ALL_PKG_QTY")).longValue();
                        spAllPkgQty = salQty - allPkgQty;
                    }
                    //如果已经存在装箱明细就修改
                    if (!"".equals(pkgId)) {
                        TtPartPkgDtlPO dtlPO = new TtPartPkgDtlPO();
                        dtlPO.setPkgId(CommonUtils.parseLong(pkgId));
                        dtlPO.setBatchNo(batchNo);//20170904 add
                        dtlPO = (TtPartPkgDtlPO) dao.select(dtlPO).get(0);
                        Long pkgQty = dtlPO.getPkgQty();

                        if (pkgQtyPage > (spAllPkgQty + pkgQty)) {
                            errInfo = "货位" + locName + "上的配件【" + partOldCode + "】的总销售数量为" + salQty + ",本次装箱总数量不能大于" + (spAllPkgQty + pkgQty) + ";";
                            BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, errInfo);
                            throw e1;
                        }

                        TtPartPkgDtlPO spo = new TtPartPkgDtlPO();
                        TtPartPkgDtlPO po = new TtPartPkgDtlPO();
                        spo.setPkgId(CommonUtils.parseLong(pkgId));
                        spo.setBatchNo(batchNo);//20170904 add
                        po.setPkgQty(pkgQtyPage);
                        po.setRemark(remark);
                        dao.update(spo, po);

                        //如果当前拣货单已经完成装箱,由于这里不会改变状态,此时需要增加现场BO数量
                        if (state.intValue() == Constant.CAR_FACTORY_PKG_STATE_02.intValue()) {
                            Map<String, Object> pkgMap = partPkgDao.queryPkgId(pickOrderId, partId, locId,batchNo);
                            System.out.println("=========现场bo");
                            //已经产生现场bo的情况
                            if (pkgMap != null && pkgMap.get("PKG_ID") != null) {//更新现场BO数量
                                Long pkgIdBo = ((BigDecimal) pkgMap.get("PKG_ID")).longValue();

                                System.out.println("=========现场bo pkgQtyPage："+pkgQtyPage+"====pkgQty:"+pkgQty);
                                //如果把装箱数量改大,那就减少现场BO的数量
                                if (pkgQtyPage > pkgQty) {
                                    TtPartPkgDtlPO selectPo = new TtPartPkgDtlPO();
                                    selectPo.setPkgId(pkgIdBo);
                                    selectPo.setBatchNo(batchNo);//20170904 add
                                    selectPo = (TtPartPkgDtlPO) dao.select(selectPo).get(0);
                                    Long locBoQty = selectPo.getLocBoQty();
                                    System.out.println("=========现场bo locBoQty："+locBoQty);
                                    //如果改大之后就没有现场bo了，那就删除改装箱明细
                                    if (locBoQty - (pkgQtyPage - pkgQty) == 0) {
                                        TtPartPkgDtlPO delPo = new TtPartPkgDtlPO();
                                        delPo.setPkgId(pkgIdBo);
                                        selectPo.setBatchNo(batchNo);//20170904 add
                                        dao.delete(delPo);
                                    } else {
                                        partPkgDao.updateBodtl(pkgIdBo, (pkgQty - pkgQtyPage),batchNo);
                                    }
                                }
                                //如果把装箱数量改小,那就增加现场BO的数量
                                if (pkgQtyPage < pkgQty) {
                                    partPkgDao.updateBodtl(pkgIdBo, (pkgQty - pkgQtyPage),batchNo);
                                }
                            } else {//如果没有产生过现场bo,那到这一步只能是把装箱数量改小了，故要新增现场bo
                                insertPkgDtlBo(dtlPO, (pkgQty - pkgQtyPage));
                            }
                        }
                    } else {//如果不存在就新增装箱明细
                        //新增时需要验证当前拣货单中的配件在占用明细中是否存在,若存在就继续往下执行,否则不执行
                        boolean isExistBookDtl = partPkgDao.isExistBookDtl(pickOrderId, partId, locId,batchNo);
                        if (isExistBookDtl) {
                            if (pkgQtyPage > spAllPkgQty) {
                                errInfo = "货位" + locName + "上的配件【" + partOldCode + "】的总销售数量为" + salQty + ",本次装箱总数量不能大于" + (spAllPkgQty) + ";";
                                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, errInfo);
                                throw e1;
                            }
                            TtPartPkgDtlPO po = new TtPartPkgDtlPO();
                            String pkgType = CommonUtils.checkNull(request.getParamValue("pkgType_" + partIds[i]));

                            po.setPkgNo(pkgNo);
                            po.setPickOrderId(pickOrderId);
                            po.setPartCname(partCname);
                            po.setPartCode(partCode);
                            po.setPartOldcode(partOldCode);
                            po.setPartId(Long.valueOf(partId));
                            po.setLocId(Long.valueOf(locId));
                            po.setBatchNo(batchNo);//20170901 add
                            po.setSoId(soId);//20170901 add
                            po.setBoxId(boxId);//20170901 add
                            po.setUnit(unit);
                            po.setRemark(remark);
                            po.setCreateBy(loginUser.getUserId());
                            po.setPkgType(Integer.valueOf(pkgType));
                            po.setSalesQty(Long.valueOf(salQty));
                            po.setCreateDate(new Date());
                            po.setVer(1);

                            po.setPkgQty(pkgQtyPage);
                            normalList.add(po);

                            //现场BO数量更新
                            partPkgDao.updatePkgBodtl(pickOrderId, partId, pkgQtyStr,batchNo);

                        }

                    }
                }
                //清理现场BO
                partPkgDao.deleXCBodtl(pickOrderId);
            }

            if (normalList.size() > 0) {
                insertPkgDtl("1", normalList, whId);
            }

            act.setOutData("success", "修改成功!");
        } catch (Exception e) {//异常方法

            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }

            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "装箱修改失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }


    private void insertPkgDtlBo(TtPartPkgDtlPO pkgDtlPO, Long difQty) throws Exception {
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        try {

            TtPartPkgDtlPO po = new TtPartPkgDtlPO();
            po.setPkgId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            po.setPickOrderId(pkgDtlPO.getPickOrderId());
            po.setPartCname(pkgDtlPO.getPartCname());
            po.setPartCode(pkgDtlPO.getPartCode());
            po.setPartOldcode(pkgDtlPO.getPartOldcode());
            po.setPartId(pkgDtlPO.getPartId());
            po.setLocId(pkgDtlPO.getLocId());
            po.setBatchNo(pkgDtlPO.getBatchNo());//批次号 20170901 add
            po.setSoId(pkgDtlPO.getSoId());//销售单id 20170901 add
            po.setSlineId(pkgDtlPO.getSlineId());//销售单明细序号 20170901 add
            po.setUnit(pkgDtlPO.getUnit());
            po.setCreateBy(pkgDtlPO.getCreateBy());
            po.setSalesQty(pkgDtlPO.getSalesQty());
            po.setCreateDate(new Date());
            po.setPkgType(pkgDtlPO.getPkgType());
            po.setVer(1);

            po.setPkgQty(pkgDtlPO.getSalesQty() - difQty);
            po.setLocBoQty(difQty);

            dao.insert(po);

        } catch (Exception e) {
            throw e;
        }

    }

    //CODE为1  是正常装箱的  CODE为2的是有最小装箱量的
    private void insertPkgDtl(String code, List<TtPartPkgDtlPO> dtlList, String whId) throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        PartPkgDao dao = PartPkgDao.getInstance();
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pkg_no = CommonUtils.checkNull(request.getParamValue("PKG_NO"));//页面输入的装箱单号
            String status = CommonUtils.checkNull(request.getParamValue("status"));// 
            List<String> list = (List<String>) request.getAttribute("pkgIdList");
            for (TtPartPkgDtlPO po : dtlList) {
                if (code.equals("1")) {
                    Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                    list.add(pkgId + "");
                    po.setPkgId(pkgId);
                    if (!"".equals(pkg_no)) {
                        po.setPkgNo(pkg_no);
                    }
                    dao.insert(po);
                } else if (code.equals("2")) {
                    Map<String, Object> locMap = dao.queryPartLocationByPartId(po.getPartId() + "", whId);
                    Long minPkg = Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));
                    //装箱的数量是不能小于最小包装箱量的
                    if (po.getPkgQty() < minPkg) {
                        if ("1".equals(status)) {
                            continue;
                        }
                    }
                    long pkgQty = po.getPkgQty();
                    long temp = pkgQty % Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));
                    long tempBoxNo = (pkgQty - temp) / Long.valueOf(CommonUtils.checkNull(locMap.get("MIN_PKG")));  //获得整除后的箱数转换INT
                    int boxNo = Integer.valueOf(tempBoxNo + "");

                    for (int i = 0; i < boxNo; i++) {
                        Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                        list.add(pkgId + "");
                        po.setPkgId(pkgId);
                        if (!"".equals(pkg_no)) {
                            po.setPkgNo(pkg_no);
                        }
                        dao.insert(po);
                    }
                    if ("2".equals(status) && boxNo == 0) {
                        Long pkgId = Long.parseLong(SequenceManager.getSequence(""));
                        list.add(pkgId + "");
                        po.setPkgId(pkgId);
                        if (!"".equals(pkg_no)) {
                            po.setPkgNo(pkg_no);
                        }
                        po.setPkgQty(Long.valueOf(pkgQty));
                        dao.insert(po);
                    }
                }

            }
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void validateState(String pickOrderId) throws Exception {
        ActionContext act = ActionContext.getContext();
        PartPickOrderDao dao = PartPickOrderDao.getInstance();
        List<Map<String, Object>> list = dao.getOrderCount(pickOrderId);
        if (null != list) {
            if (list.size() > 0) {
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "订单状态已经改变，请刷新页面!");
                throw e1;
            }
        }
    }

    /**
     * 装箱时现场BO处理逻辑
     *
     * @param pickOrderId 拣货单ID
     * @param partId      配件ID
     * @param locId       货位ID
     * @param dtlPO
     * @param pkgQtyPage  页面填写的装箱数量
     * @param pkgQty      已装箱数量
     * @throws Exception
     */
    /*20170904 屏蔽 private void xcBOProces(String pickOrderId, String partId, String locId, TtPartPkgDtlPO dtlPO, long pkgQtyPage, long pkgQty) throws Exception {
        //
        Map<String, Object> pkgMap = pkgMap = partPkgDao.queryPkgId(pickOrderId, partId, locId);
        //已有现场BO记录
        if (pkgMap != null && pkgMap.get("PKG_ID") != null) {//更新现场BO数量
            Long pkgIdBo = ((BigDecimal) pkgMap.get("PKG_ID")).longValue();
            //如果把装箱数量改大,那就减少现场BO的数量
            if (pkgQtyPage > pkgQty) {
                TtPartPkgDtlPO selectPo = new TtPartPkgDtlPO();
                selectPo.setPkgId(pkgIdBo);
                selectPo = (TtPartPkgDtlPO) partPkgDao.select(selectPo).get(0);
                Long locBoQty = selectPo.getLocBoQty();
                //如果改大之后就没有现场bo了，那就删除改装箱明细
                if (locBoQty - (pkgQtyPage - pkgQty) == 0) {
                    TtPartPkgDtlPO delPo = new TtPartPkgDtlPO();
                    delPo.setPkgId(pkgIdBo);
                    partPkgDao.delete(delPo);
                } else {
                    partPkgDao.updateBodtl(pkgIdBo, (pkgQty - pkgQtyPage));
                }
            }
            //如果把装箱数量改小,那就增加现场BO的数量
            if (pkgQtyPage < pkgQty) {
                partPkgDao.updateBodtl(pkgIdBo, (pkgQty - pkgQtyPage));
            }
        } else {//如果没有产生过现场bo,那到这一步只能是把装箱数量改小了，故要新增现场bo
            insertPkgDtlBo(dtlPO, (pkgQty - pkgQtyPage));
        }
    }*/
}
