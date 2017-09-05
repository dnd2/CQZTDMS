package com.infodms.dms.actions.parts.baseManager.PartContractManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.PartContractManager.PartContractManagerDao;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPriceHistoryPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartContractDefineLogPO;
import com.infodms.dms.po.TtPartContractDefinePO;
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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/4/5.
 */
public class PartContractQuery  extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartContractQuery.class);
    private static final PartContractManagerDao dao = PartContractManagerDao.getInstance();
    private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/PartContractManager/inputError.jsp";//数据导入出错页面

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 合同管理维护页面
    */
    public void partContractQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_CONTRACT_QUERY);
        }catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 合同查询页面
     */
    public void partContractQuerySearch() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_CONTRACT_QUERY_SEARCH);
        }catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同查询初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void PartContractQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
//            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否过期
            String isTemp = CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否过期
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,vType,isOut,Constant.PAGE_SIZE, curPage,state,isTemp);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void PartContractInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否有效
            String isTemp=CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否临时
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getJBContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,vType,isOut,Constant.PAGE_SIZE, curPage,state,isTemp);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 90天内到期合同管理-查询
     */
    public void outTimeContractQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getOTContractQuery(request,Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    public void contractSign() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getNoSignContract(request,Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void rePartContractQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vendeType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE"));//供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT"));//是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否过期
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.regetContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,eDate,vendeType,isOut,Constant.PAGE_SIZE, curPage,state);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void rePartDDContractQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vendeType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE"));//供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT"));//是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否过期
            String isTemp = CommonUtils.checkNull(request.getParamValue("ISTEMP")); // 是否临时
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.regetDDContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,eDate,vendeType,isOut,Constant.PAGE_SIZE, curPage,state,isTemp);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void PartContractQueryHis() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getContractQueryHis(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,eDate,Constant.PAGE_SIZE, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void InsertConInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        act.setForword(PART_CONTRACT_ADD);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理新增初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 配件信息弹出框 for Add
     */
    public void queryPartsForAddInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
//            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID")); //供应商ID
//            act.setOutData("VENDER_ID",venderId);
            act.setForword(PART_CONTRACT_CHOOSE);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理选择配件初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 返回配件信息
     */
    public void queryPartsDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partolcode = CommonUtils.checkNull(request.getParamValue("partolcode")); // 配件编码
            String partcname = CommonUtils.checkNull(request.getParamValue("partcname")); // 配件名称
         //   String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID")); // 供应商ID
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartException(partolcode, partcname, 13, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 添加
     */
    @SuppressWarnings("unchecked")
    public void InsertContract() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parts = CommonUtils.checkNull(request.getParamValue("parts")); // 配件IDS
            String prices = CommonUtils.checkNull(request.getParamValue("prices")); // 配件IDS
            String ConNum = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); // 合同编号
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID")); // 供应商ID
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME")); // 供应商名称
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); // 合同类型
            String sdate = CommonUtils.checkNull(request.getParamValue("checkSDate")); // 供应商名称
            String edate = CommonUtils.checkNull(request.getParamValue("checkEDate")); // 合同类型
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
            String isTemp = CommonUtils.checkNull(request.getParamValue("ISTEMP")); // 是否临时
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            List<Map<String,Object>> checkList=dao.checkVender(ConNum,Long.parseLong(venderId));
            if(checkList.size()>0){
            	 act.setOutData("success", "合同号已存在");
            	 return;
            }
            if(ConType.equals(Constant.CONTRACT_TYPE_02.toString())) {
                String partsArr[] = parts.split(",");
                String partsArr1[] = prices.split(",");
                for (int i = 0; i < partsArr.length; i++) {
                    TtPartContractDefinePO cpo = new TtPartContractDefinePO();
                    cpo.setPartId(Long.parseLong(partsArr[i]));
                    cpo.setVenderId(Long.parseLong(venderId));
                    cpo.setState(Constant.STATUS_ENABLE);
                    List<TtPartContractDefinePO> list = dao.select(cpo);
                    boolean flag = true;
                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); j++) {
                            Date contractEdate = list.get(j).getContractEdate();
                            Date contractSdate = list.get(j).getContractSdate();
                            if ((sdf.parse(sdate).getTime() <= contractSdate.getTime() && sdf.parse(edate).getTime() >= contractEdate.getTime())||(sdf.parse(sdate).getTime() <= contractEdate.getTime() && sdf.parse(sdate).getTime() >= contractSdate.getTime())
                                    || (sdf.parse(sdate).getTime() >= contractSdate.getTime() && sdf.parse(edate).getTime() <= contractEdate.getTime()) ||
                                    (sdf.parse(edate).getTime() <= contractEdate.getTime() && sdf.parse(edate).getTime() >= contractSdate.getTime())) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (!flag) {
                        BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "合同管理新增合同日期重叠");
                        throw e1;
                    }
                    TtPartContractDefinePO cdpo = new TtPartContractDefinePO();
                    TtPartContractDefineLogPO lpo = new TtPartContractDefineLogPO();
                    long defid = Long.parseLong(SequenceManager.getSequence(""));
                    cdpo.setRemark(remark);
                    cdpo.setDefId(defid);
                    Date parse2 = sdf.parse(sdate);
                    Date parse3 = sdf.parse(edate);
                    cdpo.setContractSdate(parse2);
                    cdpo.setContractEdate(parse3);
                    cdpo.setContractNumber(ConNum);
                    cdpo.setContractType(Integer.parseInt(ConType));
                    cdpo.setCreateBy(logonUser.getUserId());
                    cdpo.setCreateDate(date);
                    cdpo.setVenderId(Long.parseLong(venderId));
                    cdpo.setPartId(Long.parseLong(partsArr[i]));
                    cdpo.setContractPrice(Double.parseDouble(partsArr1[i]));
                    cdpo.setIstemp(Integer.valueOf(isTemp));
                    cdpo.setState(Constant.STATUS_ENABLE);
                    dao.insert(cdpo);
                    lpo.setRemark(remark);
                    lpo.setDefId(defid);
                    lpo.setContractSdate(parse2);
                    lpo.setContractEdate(parse3);
                    lpo.setContractNumber(ConNum);
                    lpo.setContractType(Integer.parseInt(ConType));
                    lpo.setCreateBy(logonUser.getUserId());
                    lpo.setCreateDate(date);
                    lpo.setVenderId(Long.parseLong(venderId));
                    lpo.setPartId(Long.parseLong(partsArr[i]));
                    lpo.setContractPrice(Double.parseDouble(partsArr1[i]));
                    lpo.setIstemp(Integer.valueOf(isTemp));
                    lpo.setState(Constant.STATUS_ENABLE);
                    dao.insert(lpo);
                    String format = sdf.format(new Date());
                    Date parse = sdf.parse(format);

                        TtPartBuyPricePO bppo = new TtPartBuyPricePO();
                        TtPartBuyPricePO updtePo = new TtPartBuyPricePO();
                        TtPartBuyPriceHistoryPO Hipo = new TtPartBuyPriceHistoryPO();
                        bppo.setVenderId(Long.parseLong(venderId));
                        bppo.setPartId(Long.parseLong(partsArr[i]));
                        List<TtPartBuyPricePO> select = dao.select(bppo);
                    if (select.size() > 0) {
                        if (parse2.getTime() <= parse.getTime()&&parse3.getTime()>=parse.getTime()){
                            updtePo.setBuyPrice(Double.parseDouble(partsArr1[i]));
                            updtePo.setUpdateBy(logonUser.getUserId());
                            Date parse1 = parse3;
                            if (parse1.getTime() < parse.getTime()) {
                                updtePo.setIsGuard(Constant.IF_TYPE_YES);
                                Hipo.setIsGuard(Constant.IF_TYPE_YES);
                            } else {
                                updtePo.setIsGuard(Constant.IF_TYPE_NO);
                                Hipo.setIsGuard(Constant.IF_TYPE_NO);
                            }
                            updtePo.setUpdateDate(date);
                            updtePo.setContractSdate(parse2);
                            updtePo.setContractEdate(parse3);
                            updtePo.setContractNumber(ConNum);
                            TtPartBuyPricePO ttPartBuyPricePO = select.get(0);
                            Hipo.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                            Hipo.setPriceId(ttPartBuyPricePO.getPriceId());
                            Hipo.setPartId(ttPartBuyPricePO.getPartId());
                            Hipo.setOldBuyPrice(ttPartBuyPricePO.getBuyPrice());
                            Hipo.setBuyPrice(Double.parseDouble(partsArr1[i]));
                            Hipo.setCreateBy(logonUser.getUserId());
                            Hipo.setCreateDate(new Date());
                            Hipo.setVenderId(ttPartBuyPricePO.getVenderId());
                            dao.update(bppo, updtePo);
                            dao.insert(Hipo);
                        }
                        } else {
                            long priceId = Long.parseLong(SequenceManager.getSequence(""));
                            bppo.setPriceId(priceId);
                            bppo.setCreateBy(logonUser.getUserId());
                            bppo.setCreateDate(date);
                            bppo.setContractNumber(ConNum);
                            Date parse1 = parse3;
                            if (parse1.getTime() < parse.getTime()) {
                                bppo.setIsGuard(Constant.IF_TYPE_YES);
                                Hipo.setIsGuard(Constant.IF_TYPE_YES);
                            } else {
                                bppo.setIsGuard(Constant.IF_TYPE_NO);
                                Hipo.setIsGuard(Constant.IF_TYPE_NO);
                            }
                            bppo.setContractSdate(parse2);
                            bppo.setContractEdate(parse3);
                            bppo.setBuyPrice(Double.parseDouble(partsArr1[i]));
                            bppo.setBuyRatio(1F);
                            dao.insert(bppo);
                        //历史
                            Hipo.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                            Hipo.setPriceId(priceId);
                            Hipo.setPartId(Long.parseLong(partsArr[i]));
                            Hipo.setOldBuyPrice(Double.parseDouble(partsArr1[i]));
                            Hipo.setBuyPrice(Double.parseDouble(partsArr1[i]));
                            Hipo.setCreateBy(logonUser.getUserId());
                            Hipo.setCreateDate(new Date());
                            Hipo.setVenderId(Long.parseLong(venderId));
                            dao.insert(Hipo);
                        }
                    }
            }else{
                TtPartContractDefinePO cdpo = new TtPartContractDefinePO();
                TtPartContractDefineLogPO lpo = new TtPartContractDefineLogPO();
                TtPartContractDefinePO checkPo=new TtPartContractDefinePO();
                checkPo.setContractNumber(ConNum);
                checkPo.setContractType(Integer.parseInt(ConType));
                List<TtPartContractDefinePO> list1 = dao.select(checkPo);
                if(list1.size()>0){
                    BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "合同号已存在");
                    throw e1;
                }
                long defid = Long.parseLong(SequenceManager.getSequence(""));
                cdpo.setRemark(remark);
                cdpo.setDefId(defid);
                cdpo.setContractSdate(sdf.parse(sdate));
                cdpo.setContractEdate(sdf.parse(edate));
                cdpo.setContractNumber(ConNum);
                cdpo.setContractType(Integer.parseInt(ConType));
                cdpo.setCreateBy(logonUser.getUserId());
                cdpo.setCreateDate(date);
                cdpo.setVenderId(Long.parseLong(venderId));
                cdpo.setIstemp(Integer.valueOf(isTemp));
                cdpo.setState(Constant.STATUS_ENABLE);
                dao.insert(cdpo);
                lpo.setRemark(remark);
                lpo.setDefId(defid);
                lpo.setContractSdate(sdf.parse(sdate));
                lpo.setContractEdate(sdf.parse(edate));
                lpo.setContractNumber(ConNum);
                lpo.setContractType(Integer.parseInt(ConType));
                lpo.setCreateBy(logonUser.getUserId());
                lpo.setCreateDate(date);
                lpo.setVenderId(Long.parseLong(venderId));
                lpo.setIstemp(Integer.valueOf(isTemp));
                lpo.setState(Constant.STATUS_ENABLE);
                dao.insert(lpo);
            }
            act.setOutData("success","true");
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e,
                        ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理新增");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出配件采购属性Excel
     */
    public void expPartPurProExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否过期
            String isTemp =CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否临时
            String[] head = new String[9];
            head[0] = "序号";
            head[1] = "合同号";
            head[2] = "合同类型";
            head[3] = "供应商编码";
            head[4] = "供应商名称";
            head[5] = "合同有效期起";
            head[6] = "合同有效期止";
            head[7] = "是否有效";
            head[8] = "是否临时";
            PageResult<Map<String, Object>> ps = dao.getContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,vType,isOut,Constant.PAGE_SIZE_MAX, 1,state,isTemp);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[12];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        detail[2] = CommonUtils.checkNull(map.get("CONTRACT_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("CONTRACT_SDATE"));
                        detail[6] = CommonUtils.checkNull(map.get("CONTRACT_EDATE"));
                        detail[7] = CommonUtils.checkNull(map.get("STATE"));
                        detail[8] = CommonUtils.checkNull(map.get("ISTEMP"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出基本合同Excel
     */
    public void expPartPurProExcel2() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否过期
            String isTemp=CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否临时
            String[] head = new String[9];
            head[0] = "序号";
            head[1] = "合同号";
            head[2] = "合同类型";
            head[3] = "供应商编码";
            head[4] = "供应商名称";
            head[5] = "合同有效期起";
            head[6] = "合同有效期止";
            head[7] = "是否有效";
            head[8] = "是否临时";
            
            PageResult<Map<String, Object>> ps = dao.getJBContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,vType,isOut,Constant.PAGE_SIZE_MAX, 1,state,isTemp);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[12];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        detail[2] = CommonUtils.checkNull(map.get("CONTRACT_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("CONTRACT_SDATE"));
                        detail[6] = CommonUtils.checkNull(map.get("CONTRACT_EDATE"));
                        detail[7] = CommonUtils.checkNull(map.get("STATE"));
                        detail[8] = CommonUtils.checkNull(map.get("ISTEMP"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出合同明细Excel
     */
    public void expPartPurProExcel1() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否过期
            String[] head = new String[13];
            head[0] = "序号";
            head[1] = "合同号";
            head[2] = "合同类型";
            head[3] = "合同价";
            head[4] = "供应商编码";
            head[5] = "供应商名称";
            head[6] = "配件编码";
            head[7] = "配件名称";
            head[8] = "配件件号";
            head[9] = "合同导入人";
            head[10] = "合同有效期起";
            head[11] = "合同有效期止";
            head[12] = "是否有效";
            PageResult<Map<String, Object>> ps = dao.regetContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,eDate,vType,isOut,Constant.PAGE_SIZE_MAX, 1,state);
//            PageResult<Map<String, Object>> ps = dao.getContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,vType,isOut,, 1);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        detail[2] = CommonUtils.checkNull(map.get("CONTRACT_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("CONTRACT_PRICE"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[8] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[9] = CommonUtils.checkNull(map.get("NAME"));
                        detail[10] = CommonUtils.checkNull(map.get("CONTRACT_SDATE"));
                        detail[11] = CommonUtils.checkNull(map.get("CONTRACT_EDATE"));
                        detail[12] = CommonUtils.checkNull(map.get("STATE"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出基本合同Excel
     */
    public void expPartPurProExcel4() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); //供应商编码
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String ConNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME"));//配件名称
            String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
            String ConType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); //合同类型
            String sDate = CommonUtils.checkNull(request.getParamValue("STARTDATE")); //合同开始时间
            String eDate = CommonUtils.checkNull(request.getParamValue("ENDDATE")); //合同结束时间
            String vType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); //供应商类型
            String isOut = CommonUtils.checkNull(request.getParamValue("IS_OUT")); //是否过期
            String state = CommonUtils.checkNull(request.getParamValue("STATE")); //是否过期
            String isTemp = CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否过期
            
            String[] head = new String[14];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            head[3] = "配件件号";
            head[4] = "合同号";
            head[5] = "合同类型";
            head[6] = "合同价";
            head[7] = "供应商编码";
            head[8] = "供应商名称";
            head[9] = "合同导入人";
            head[10] = "合同有效期起";
            head[11] = "合同有效期止";
            head[12] = "是否有效";
            head[13] = "是否临时";
            
            PageResult<Map<String, Object>> ps = dao.regetDDContractQuery(venderCode, venderName,ConNumber,partCode,partName,partOldcode,ConType,sDate,eDate,vType,isOut,Constant.PAGE_SIZE_MAX, 1,state,isTemp);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        detail[5] = CommonUtils.checkNull(map.get("CONTRACT_TYPE"));
                        detail[6] = CommonUtils.checkNull(map.get("CONTRACT_PRICE"));
                        detail[7] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[8] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[9] = CommonUtils.checkNull(map.get("NAME"));
                        detail[10] = CommonUtils.checkNull(map.get("CONTRACT_SDATE"));
                        detail[11] = CommonUtils.checkNull(map.get("CONTRACT_EDATE"));
                        detail[12] = CommonUtils.checkNull(map.get("STATE"));
                        detail[13] = CommonUtils.checkNull(map.get("ISTEMP"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出基本合同Excel
     */
    public void expPartPurProExcel5() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String[] head = new String[10];
            head[0] = "序号";
            head[1] = "合同号";
            head[2] = "合同类型";
            head[3] = "供应商编码";
            head[4] = "供应商名称";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "合同导入人";
            head[8] = "合同有效期起";
            head[9] = "合同有效期止";
            PageResult<Map<String, Object>> ps = dao.getOTContractQuery(request,Constant.PAGE_SIZE_MAX, 1);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        detail[2] = CommonUtils.checkNull(map.get("CONTRACT_TYPE"));
                        detail[3] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map.get("NAME"));
                        detail[8] = CommonUtils.checkNull(map.get("CONTRACT_SDATE"));
                        detail[9] = CommonUtils.checkNull(map.get("CONTRACT_EDATE"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-6
     * @Title : 导出配件采购属批量修改模板
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("合同编号");
            listHead.add("合同类型(基本合同/订单合同)");
            listHead.add("配件编码");
            listHead.add("供应商编码");
            listHead.add("合同价");
			listHead.add("合同有效期起(日期格式:年-月-日)");
			listHead.add("合同有效期止(日期格式:年-月-日)");
            listHead.add("是否临时");
            listHead.add("备注(来源)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "合同管理批量导入模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出合同管理模板错误");
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
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
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
     * @throws :
     * @Title : 合同管理-> 导入文件
     */
    public void purProUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            String err = "";
            List<Map<String, String>> errorInfo = null;
            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile",9, 3, maxSize);
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
                return;
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                    return;
                }
                //筛选导入文件中是否存在重复数据。
                for (int i=0;i<voList.size();i++){
                    for(int j=voList.size()-1;j>i;j--){
                        Map<String, String> stringStringMap = voList.get(i);
                        Map<String, String> stringStringMap1 = voList.get(j);
                        String conTractNumber =stringStringMap.get("contractNumber");
                        String venderId = stringStringMap.get("venderId");
                        String partId = stringStringMap.get("partId");
                        String conTractNumber1 =stringStringMap1.get("contractNumber");
                        String venderId1 = stringStringMap1.get("venderId");
                        String partId1 = stringStringMap1.get("partId");
                        String partOldcode = stringStringMap1.get("partOldcode");
                        int flag=0;
                        if (StringUtil.notNull(partId)) {
                            if (partId.equals(partId1)) {
                                flag++;
                            }
                        }
                        if(venderId.equals(venderId1)){
                            flag++;
                        }
                        if(conTractNumber.equals(conTractNumber1)){
                            flag++;
                        }
                        if(flag==3){
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "数据重复，重复编码");
                            errormap.put("2", partOldcode);
                            errormap.put("3", "对应同一合同同一供应商");
                            errorInfo.add(errormap);
                        }
                    }
                }
                //20170316添加验证不同供应商合同号不能相同
                for (int i=0;i<voList.size();i++){
                	Map<String, String> stringStringMap = voList.get(i);
                	String conTractNumber =stringStringMap.get("contractNumber");
                    String venderId = stringStringMap.get("venderId");
                    List<Map<String,Object>> checkList=dao.checkVender(conTractNumber,Long.parseLong(venderId));
                    if(checkList.size()>0){
                    	 Map<String, String> errormap = new HashMap<String, String>();
                         errormap.put("1", "合同号");
                         errormap.put("2", conTractNumber);
                         errormap.put("3", "已存在");
                         errorInfo.add(errormap);
                    }
                }
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                    return;
                }
                savePartPlanner(voList);
            }
            act.setForword(PART_CONTRACT_QUERY);
            act.setOutData("success","success");
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setOutData("success",e1);
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
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo)
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
                parseCells(voList, key, cells, errorInfo);
            }
        }

    }

    private void parseCells(List list, String rowNum, Cell[] cells,
                            List<Map<String, String>> errorInfo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String part_Oldcode = "";//配件编码
        String vender_Code = "";//供应商编码
        String contractNumber = "";//合同编号
        String contractType = "";//合同类型
        String contractPrice = "";//合同价
        String startDate = "";//合同有效期起
        String endDate = "";//合同有效期止
        String isTemp="";//是否临时
        String remark = "";//备注
        for(int k = 0; k < cells.length; k ++)
        {
            switch(k)
            {
                case 0:
                    contractNumber = subCell(cells[0].getContents()).trim();
                    break;
                case 1:
                    contractType =   subCell(cells[1].getContents()).trim();
                    break;
                case 2:
                    part_Oldcode = subCell(cells[2].getContents()).trim().toUpperCase();
                    break;
                case 3:
                    vender_Code = subCell(cells[3].getContents()).trim();
                    break;
                case 4:
                    contractPrice = subCell(cells[4].getContents()).trim();
                    break;
                case 5:
                    startDate = subCell(cells[5].getContents().trim());
                    break;
                case 6:
                    endDate = subCell(cells[6].getContents().trim());
                    break;
                case 7:
                    isTemp = subCell(cells[7].getContents().trim());
                    break;
                case 8:
                    remark = subCell(cells[8].getContents().trim());
                    break;
                default:
                    break;
            }


        }

        Map<String, String> tempmap = new HashMap<String, String>();
        if ("" == contractNumber) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同编号");
            errormap.put("3", "为空!");
            errorInfo.add(errormap);
        }
        try {
            tempmap.put("contractNumber",contractNumber);
        } catch (Exception e) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同编号");
            errormap.put("3", "数据类型错误!");
            errorInfo.add(errormap);
        }

        if ("" == contractType) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同类型");
            errormap.put("3", "不能为空!");
            errorInfo.add(errormap);
        } else {
            if (contractType.equals("基本合同")){
                tempmap.put("contractType",Constant.CONTRACT_TYPE_01.toString());
            }else if(contractType.equals("订单合同")){
                tempmap.put("contractType",Constant.CONTRACT_TYPE_02.toString());
            }else{
                Map<String, String> errormap = new HashMap<String, String>();
                errormap.put("1", "第" + rowNum + "行");
                errormap.put("2", "合同类型");
                errormap.put("3", "输入错误!");
                errorInfo.add(errormap);
            }

        }

        if (contractType.equals("订单合同")){
            if (""==contractPrice) {
                Map<String, String> errormap = new HashMap<String, String>();
                errormap.put("1", "第" + rowNum + "行");
                errormap.put("2", "合同价");
                errormap.put("3", "不能为空!");
                errorInfo.add(errormap);
            }
        }

        try {
            tempmap.put("contractPrice",contractPrice);
        } catch (Exception e) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同价");
            errormap.put("3", "数据类型错误!");
            errorInfo.add(errormap);
        }
        Long partId=null;
        if (contractType.equals("订单合同")){
            if ("" == part_Oldcode) {
                Map<String, String> errormap = new HashMap<String, String>();
                errormap.put("1", "第" + rowNum + "行");
                errormap.put("2", "配件编码");
                errormap.put("3", "不能为空!");
                errorInfo.add(errormap);
            }

            List<Map<String, Object>> maps = partPlannerQueryDao.getInstance().checkOldCode(part_Oldcode);
            if (maps.size()==0) {
                Map<String, String> errormap = new HashMap<String, String>();
                errormap.put("1", "第" + rowNum + "行");
                errormap.put("2", "配件编码");
                errormap.put("3", "不存在!");
                errorInfo.add(errormap);
            }else{
                partId = ((BigDecimal) maps.get(0).get("PART_ID")).longValue();
                tempmap.put("partId",partId.toString());
                tempmap.put("partOldcode",part_Oldcode);
            }
        }

        Long venderId = null;
        //供应商验证
        if (vender_Code != "") {
            List<Map<String, Object>> partCheck = dao.checkVenderCode(vender_Code);
            if (partCheck.size() == 0) {
                Map<String, String> errormap = new HashMap<String, String>();
                errormap.put("1", "第" + rowNum + "行");
                errormap.put("2", "供应商编码");
                errormap.put("3", "不存在!");
                errorInfo.add(errormap);
            } else {
                venderId = ((BigDecimal) partCheck.get(0).get("VENDER_ID")).longValue();
                tempmap.put("venderId",venderId.toString());
                tempmap.put("venderCode",vender_Code);
            }


        }else{
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "供应商编码");
            errormap.put("3", "不能为空!");
            errorInfo.add(errormap);
        }
        if (""==startDate) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同有效期起");
            errormap.put("3", "不能为空!");
            errorInfo.add(errormap);
        }
        
        if (""==endDate) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同有效期止");
            errormap.put("3", "不能为空!");
            errorInfo.add(errormap);
        }
        SimpleDateFormat sdf=null;
        if (startDate.indexOf("-") > 0) {
            String[] split = startDate.split("-");
            if (split[0].length() < 3) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                //20170104 日期格式是yy-mm-dd 时，系统自动添加 20，变成2017-01-01
                startDate = "20"+startDate;
                endDate = "20"+endDate;
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
        } else {
            sdf = new SimpleDateFormat("yyyy/MM/dd");
        }
        try {
            tempmap.put("startDate",startDate);
        } catch (Exception e) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同有效期起");
            errormap.put("3", "数据类型错误!");
            errorInfo.add(errormap);
        }
        TtPartContractDefinePO cpo=new TtPartContractDefinePO();
        cpo.setPartId(partId);
        cpo.setVenderId(venderId);
        cpo.setState(Constant.STATUS_ENABLE);
        if(contractType.equals("订单合同")){
            cpo.setContractType(Constant.CONTRACT_TYPE_02);
        }else{
            cpo.setContractType(Constant.CONTRACT_TYPE_01);
        }
        String tempMark="0";
        if (isTemp!="") {
            if(isTemp.equals("是")){
                tempmap.put("istemp",Constant.IF_TYPE_YES.toString());
                tempMark="1";
            }else{
                tempmap.put("istemp",Constant.IF_TYPE_NO.toString());
            }
        }else{
            tempmap.put("istemp",Constant.IF_TYPE_NO.toString());
        }
//        if(tempMark.equals("1")){
//            cpo.setIstemp(Constant.IF_TYPE_YES);
//        }else{
//            cpo.setIstemp(Constant.IF_TYPE_NO);
//        }
        List<TtPartContractDefinePO> list3 = dao.select(cpo);
        boolean   flag=true;
        if (list3.size() > 0) {
            for (int j = 0; j < list3.size(); j++) {
                Date contractEdate = list3.get(j).getContractEdate();
                Date contractSdate = list3.get(j).getContractSdate();
                if ((sdf.parse(startDate).getTime() <= contractSdate.getTime() && sdf.parse(endDate).getTime() >= contractEdate.getTime())||(sdf.parse(startDate).getTime() <= contractEdate.getTime() && sdf.parse(startDate).getTime() >= contractSdate.getTime())
                        || (sdf.parse(startDate).getTime() >= contractSdate.getTime() && sdf.parse(endDate).getTime() <= contractEdate.getTime()) ||
                        (sdf.parse(endDate).getTime() <= contractEdate.getTime() && sdf.parse(endDate).getTime() >= contractSdate.getTime())) {
                    flag = false;
                    break;
                }
            }
        }
        if(!flag){
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "批量导入合同日期");
            errormap.put("3", "重叠!");
            errorInfo.add(errormap);
        }
        try {
            tempmap.put("endDate",endDate);
        } catch (Exception e) {
            Map<String, String> errormap = new HashMap<String, String>();
            errormap.put("1", "第" + rowNum + "行");
            errormap.put("2", "合同有效期止");
            errormap.put("3", "数据类型错误!");
            errorInfo.add(errormap);
        }
        tempmap.put("remark",remark);
        list.add(tempmap);
    }


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
     * @param : @param relList
     * @return :
     * @throws :
     * @Title : 批量导入
     */
    public void savePartPlanner(List<Map<String, String>> relList) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (null != relList && relList.size() > 0) {
                Long userId = logonUser.getUserId();
                Date date = new Date();
                int listSize = relList.size();
                for (int i = 0; i < listSize; i++) {
                    TtPartContractDefinePO cdpo = new TtPartContractDefinePO();
                    TtPartContractDefineLogPO lpo = new TtPartContractDefineLogPO();
                    long defid = Long.parseLong(SequenceManager.getSequence(""));

                    SimpleDateFormat sdf = null; //
                    //   SimpleDateFormat
                    cdpo.setDefId(defid);
                    String startDate = relList.get(i).get("startDate");
                    String endDate = relList.get(i).get("endDate");
                    String conTractNumber = relList.get(i).get("contractNumber");
                    String conTracttype = relList.get(i).get("contractType");
                    String venderId = relList.get(i).get("venderId");
                    String partId = relList.get(i).get("partId");
                    String conTractPrice = relList.get(i).get("contractPrice");
                    String istemp = relList.get(i).get("istemp");
                    String remark = relList.get(i).get("remark");
                    if (startDate.indexOf("-") > 0) {
                        String[] split = startDate.split("-");
                        if (split[0].length() < 3) {
                            sdf = new SimpleDateFormat("yy-MM-dd");
                        } else {
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                        }
                    } else {
                        sdf = new SimpleDateFormat("yyyy/MM/dd");
                    }
//                    TtPartContractDefinePO checkPo=new TtPartContractDefinePO();
//                    checkPo.setContractNumber(conTractNumber);
//                    checkPo.setContractType(Integer.parseInt(conTracttype));
//                    List<TtPartContractDefinePO> list1 = dao.select(checkPo);
//                    if(list1.size()>0){
//                        BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "合同号已存在");
//                        throw e1;
//                    }

                    cdpo.setContractSdate(sdf.parse(startDate));
                    cdpo.setContractEdate(sdf.parse(endDate));
                    cdpo.setContractNumber(conTractNumber);
                    cdpo.setContractType(Integer.parseInt(conTracttype));
                    cdpo.setState(Constant.STATUS_ENABLE);
                    cdpo.setCreateBy(logonUser.getUserId());
                    cdpo.setCreateDate(date);
                    if (null != istemp && !"".equals(istemp)) {
                        cdpo.setIstemp(Integer.parseInt(istemp));
                    }
                    cdpo.setRemark(remark);
                    cdpo.setVenderId(Long.parseLong(venderId));
                    if (null != partId && !"".equals(partId)) {
                        cdpo.setPartId(Long.parseLong(partId));
                    }
                    if (null != partId && !"".equals(partId)) {
                        cdpo.setContractPrice(Double.parseDouble(conTractPrice));
                    }
                    dao.insert(cdpo);
                    lpo.setDefId(defid);
                    lpo.setContractSdate(sdf.parse(startDate));
                    lpo.setContractEdate(sdf.parse(endDate));
                    lpo.setContractNumber(conTractNumber);
                    lpo.setContractType(Integer.parseInt(conTracttype));
                    lpo.setCreateBy(logonUser.getUserId());
                    lpo.setCreateDate(date);
                    lpo.setVenderId(Long.parseLong(venderId));
                    if (null != partId && !"".equals(partId)) {
                        lpo.setPartId(Long.parseLong(partId));
                    }
                    if (null != partId && !"".equals(partId)) {
                        lpo.setContractPrice(Double.parseDouble(conTractPrice));
                    }
                    lpo.setRemark(remark);
                    if (null != istemp && !"".equals(istemp)) {
                        lpo.setIstemp(Integer.parseInt(istemp));
                    }
                    dao.insert(lpo);
                    Date date2 = new Date();
                    Date stDate = sdf.parse(startDate);
                    Date eDate = sdf.parse(endDate);
                    if (conTracttype.equals(Constant.CONTRACT_TYPE_02.toString())) {
                        TtPartBuyPricePO bppo = new TtPartBuyPricePO();
                        TtPartBuyPricePO updtePo = new TtPartBuyPricePO();
                        TtPartBuyPriceHistoryPO Hipo = new TtPartBuyPriceHistoryPO();
                        bppo.setVenderId(Long.parseLong(venderId));
                        bppo.setPartId(Long.parseLong(partId));
                        List<TtPartBuyPricePO> select = dao.select(bppo);
                        Date parse = sdf.parse(startDate);
                            if (select.size() > 0 ) {
                                if(stDate.getTime() <= date2.getTime()&&eDate.getTime()>=date2.getTime()) {
                                    updtePo.setBuyPrice(Double.parseDouble(conTractPrice));
                                    updtePo.setUpdateBy(logonUser.getUserId());
                                    String format = sdf.format(new Date());
                                    Date parse1 = sdf.parse(format);
                                    Date parse2 = sdf.parse(endDate);
                                    if (parse2.getTime() < parse1.getTime()) {
                                        updtePo.setIsGuard(Constant.IF_TYPE_YES);
                                        Hipo.setIsGuard(Constant.IF_TYPE_YES);
                                    } else {
                                        updtePo.setIsGuard(Constant.IF_TYPE_NO);
                                        Hipo.setIsGuard(Constant.IF_TYPE_NO);
                                    }
                                    updtePo.setUpdateDate(date);
                                    updtePo.setContractNumber(conTractNumber);
                                    updtePo.setContractSdate(sdf.parse(startDate));
                                    updtePo.setContractEdate(sdf.parse(endDate));
                                    TtPartBuyPricePO ttPartBuyPricePO = select.get(0);
                                    Hipo.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                                    Hipo.setPriceId(ttPartBuyPricePO.getPriceId());
                                    Hipo.setPartId(ttPartBuyPricePO.getPartId());
                                    Hipo.setOldBuyPrice(ttPartBuyPricePO.getBuyPrice());
                                    Hipo.setBuyPrice(Double.parseDouble(conTractPrice));
                                    Hipo.setCreateBy(logonUser.getUserId());
                                    Hipo.setCreateDate(new Date());
                                    Hipo.setVenderId(ttPartBuyPricePO.getVenderId());
                                    dao.update(bppo, updtePo);
                                    dao.insert(Hipo);
                                }
                            } else {
                                long priceId = Long.parseLong(SequenceManager.getSequence(""));
                                bppo.setPriceId(priceId);
                                bppo.setCreateBy(logonUser.getUserId());
                                bppo.setCreateDate(date);
                                String format = sdf.format(new Date());
                                Date parse1 = sdf.parse(format);
                                Date parse2 = sdf.parse(endDate);
                                if (parse2.getTime() < parse1.getTime()) {
                                    bppo.setIsGuard(Constant.IF_TYPE_YES);
                                    Hipo.setIsGuard(Constant.IF_TYPE_YES);
                                } else {
                                    bppo.setIsGuard(Constant.IF_TYPE_NO);
                                    Hipo.setIsGuard(Constant.IF_TYPE_NO);
                                }
                                bppo.setContractNumber(conTractNumber);
                                bppo.setBuyPrice(Double.parseDouble(conTractPrice));
                                bppo.setContractSdate(sdf.parse(startDate));
                                bppo.setContractEdate(sdf.parse(endDate));
                                bppo.setBuyRatio(1F);
                                dao.insert(bppo);
                                Hipo.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                                Hipo.setPriceId(priceId);
                                Hipo.setPartId(Long.parseLong(partId));
                                Hipo.setOldBuyPrice(Double.parseDouble(conTractPrice));
                                Hipo.setBuyPrice(Double.parseDouble(conTractPrice));
                                Hipo.setCreateBy(logonUser.getUserId());
                                Hipo.setCreateDate(new Date());
                                Hipo.setVenderId(Long.parseLong(venderId));
                                dao.insert(Hipo);
                            }
                        }
                    }
            }
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 =new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "批量导入失败!");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 有效
     */
    public void enablePartPlanner() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String defId = CommonUtils.checkNull(request.getParamValue("contractnum")); //合同号
            Integer state = Constant.STATUS_ENABLE;
            Long userId = logonUser.getUserId();//操作用户ID
            TtPartContractDefinePO cdpo=new TtPartContractDefinePO();
            TtPartContractDefinePO updatepo=new TtPartContractDefinePO();
            cdpo.setContractNumber(defId);
            List<TtPartContractDefinePO> select = dao.select(cdpo);
            for (int i=0;i<select.size();i++){
                TtPartContractDefinePO ttPartContractDefinePO = select.get(i);
                TtPartContractDefineLogPO lgPo=new TtPartContractDefineLogPO();
                Integer contractType = ttPartContractDefinePO.getContractType();
                long defid = Long.parseLong(SequenceManager.getSequence(""));
                lgPo.setDefId(defid);
                lgPo.setContractNumber(ttPartContractDefinePO.getContractNumber());
                lgPo.setVenderId(ttPartContractDefinePO.getVenderId());
                if(contractType.toString().equals(Constant.CONTRACT_TYPE_02.toString())) {
                    lgPo.setPartId(ttPartContractDefinePO.getPartId());
                    lgPo.setContractPrice(ttPartContractDefinePO.getContractPrice());
                }
                lgPo.setContractType(contractType);
                lgPo.setContractEdate(ttPartContractDefinePO.getContractEdate());
                lgPo.setContractSdate(ttPartContractDefinePO.getContractSdate());
                lgPo.setState(state);
                lgPo.setUpdateBy(userId);
                lgPo.setUpdateDate(new Date());
                dao.insert(lgPo);
            }
            updatepo.setState(state);
            updatepo.setUpdateBy(userId);
            updatepo.setUpdateDate(new Date());
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.update(cdpo, updatepo);
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "有效合同管理");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 无效
     */
    public void celPartPlanner() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String defId = CommonUtils.checkNull(request.getParamValue("contractnum")); //合同号
            Integer state = Constant.STATUS_DISABLE;
            Long userId = logonUser.getUserId();//操作用户ID
            TtPartContractDefinePO cdpo=new TtPartContractDefinePO();
            TtPartContractDefinePO updatepo=new TtPartContractDefinePO();
            cdpo.setContractNumber(defId);
            List<TtPartContractDefinePO> select = dao.select(cdpo);
            for (int i=0;i<select.size();i++){
                TtPartContractDefinePO ttPartContractDefinePO = select.get(i);
                TtPartContractDefineLogPO lgPo=new TtPartContractDefineLogPO();
                Integer contractType = ttPartContractDefinePO.getContractType();
                long defid = Long.parseLong(SequenceManager.getSequence(""));
                lgPo.setDefId(defid);
                lgPo.setContractNumber(ttPartContractDefinePO.getContractNumber());
                lgPo.setVenderId(ttPartContractDefinePO.getVenderId());
                if(contractType.toString().equals(Constant.CONTRACT_TYPE_02.toString())) {
                    lgPo.setPartId(ttPartContractDefinePO.getPartId());
                    lgPo.setContractPrice(ttPartContractDefinePO.getContractPrice());
                }
                lgPo.setContractType(contractType);
                lgPo.setContractEdate(ttPartContractDefinePO.getContractEdate());
                lgPo.setContractSdate(ttPartContractDefinePO.getContractSdate());
                lgPo.setState(state);
                lgPo.setUpdateBy(userId);
                lgPo.setUpdateDate(new Date());
                dao.insert(lgPo);
            }
            updatepo.setState(state);
            updatepo.setUpdateBy(userId);
            updatepo.setUpdateDate(new Date());

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.update(cdpo, updatepo);
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "无效合同管理");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 修改初始化
     */
    public void updateByIdInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String ConNum = CommonUtils.checkNull(request.getParamValue("contractnum")); //合同号
            TtPartContractDefinePO TDPO=new TtPartContractDefinePO();
            TDPO.setContractNumber(ConNum);
            List<TtPartContractDefinePO> select = dao.select(TDPO);
            String contractType = select.get(0).getContractType().toString();
            Map<String, Object> stringObjectMap = dao.getContractList(ConNum,contractType);
            act.setOutData("stringObjectMap",stringObjectMap);
            act.setForword("/jsp/parts/baseManager/PartContractManager/updateContract.jsp");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "无效合同管理");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title : 保存配件采购属性
     */
    public void saveConTract() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String newConNum = CommonUtils.checkNull(request.getParamValue("NCONTRACT_NUMBER")); //新合同号
            String ConNum = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //合同号
            String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate")); //开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate")); //结束时间
            String remark = CommonUtils.checkNull(request.getParamValue("remark")); //合同号
            String Contype = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE1")); //合同类型
            String istemp=CommonUtils.checkNull(request.getParamValue("ISTEMP")); //是否临时
            TtPartContractDefinePO CDPO=new TtPartContractDefinePO();
            CDPO.setContractNumber(ConNum);
            List<TtPartContractDefinePO> select = dao.select(CDPO);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            TtPartContractDefinePO sePo=new TtPartContractDefinePO();
            Long partId = select.get(0).getPartId();
            Long venderId = select.get(0).getVenderId();
            Integer contractType = select.get(0).getContractType();
            List<Map<String, Object>> select1 = dao.getContractById(partId.toString(), venderId.toString(), contractType.toString(), ConNum);
            boolean flag1 = true;
            if (select1.size() > 0) {
                    String edate = select1.get(0).get("CONTRACT_EDATE").toString();
                    String sdate = select1.get(0).get("CONTRACT_SDATE").toString();
                     Date contractSdate=sdf.parse(sdate);
                     Date contractEdate=sdf.parse(edate);
                    if ((sdf.parse(startDate).getTime() <= contractSdate.getTime() && sdf.parse(endDate).getTime() >= contractEdate.getTime())||(sdf.parse(startDate).getTime() <= contractEdate.getTime() && sdf.parse(startDate).getTime() >= contractSdate.getTime())
                            || (sdf.parse(startDate).getTime() >= contractSdate.getTime() && sdf.parse(endDate).getTime() <= contractEdate.getTime()) ||
                            (sdf.parse(endDate).getTime() <= contractEdate.getTime() && sdf.parse(endDate).getTime() >= contractSdate.getTime())) {
                     flag1 = false;
                }
            }
            if (!flag1) {
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "合同管理新增合同日期重叠");
                throw e1;
            }
            for (int i=0;i<select.size();i++){
                TtPartContractDefineLogPO lpo=new TtPartContractDefineLogPO();
                TtPartContractDefinePO ttPartContractDefinePO = select.get(i);
                TtPartContractDefinePO CD=new TtPartContractDefinePO();
                TtPartContractDefinePO UP=new TtPartContractDefinePO();
                TtPartContractDefineLogPO DL=new TtPartContractDefineLogPO();
                String price = CommonUtils.checkNull(request.getParamValue("CONTRACT_PRICE"+ttPartContractDefinePO.getDefId()+"")); //合同价
                CD.setDefId(ttPartContractDefinePO.getDefId());
                UP.setContractNumber(newConNum);
                if(Contype.equals(Constant.CONTRACT_TYPE_02.toString())){
                        UP.setContractPrice(Double.parseDouble(price));
                }
                UP.setContractType(Integer.parseInt(Contype));
                UP.setRemark(remark);
                UP.setIstemp(Integer.valueOf(istemp));
                boolean flag=true;
                TtPartBuyPricePO bp=new TtPartBuyPricePO();
                TtPartBuyPricePO bu=new TtPartBuyPricePO();
                if(!ConNum.equals(newConNum)){
                    DL.setContractNumber(newConNum);
                    bu.setContractNumber(newConNum);
                    flag=false;
                }else{
                    DL.setContractNumber(ConNum);
                }
                if(Contype.equals(Constant.CONTRACT_TYPE_02.toString())) {
                    if (Double.parseDouble(price) != ttPartContractDefinePO.getContractPrice()) {
                        DL.setContractPrice(Double.parseDouble(price));
                        bp.setPartId(ttPartContractDefinePO.getPartId());
                        bp.setVenderId(ttPartContractDefinePO.getVenderId());
                        bu.setBuyPrice(Double.parseDouble(price));
                        bu.setUpdateBy(logonUser.getUserId());
                        bu.setUpdateDate(new Date());
                        dao.update(bp, bu);
                        lpo.setContractPrice(Double.parseDouble(price));
                        flag = false;
                    } else {
                        DL.setContractPrice(ttPartContractDefinePO.getContractPrice());
                    }
                }
                if(!flag){
                    DL.setContractType(ttPartContractDefinePO.getContractType());
                    DL.setDefId(Long.parseLong(SequenceManager.getSequence("")));
                    DL.setCreateBy(logonUser.getUserId());
                    DL.setCreateDate(new Date());
                    DL.setPartId(ttPartContractDefinePO.getPartId());
                    DL.setVenderId(ttPartContractDefinePO.getVenderId());
                    DL.setContractEdate(sdf.parse(startDate));
                    DL.setContractSdate(sdf.parse(endDate));
                    dao.insert(DL);
                }
                UP.setUpdateBy(logonUser.getUserId());
                UP.setUpdateDate(new Date());
                UP.setContractEdate(sdf.parse(endDate));
                UP.setContractSdate(sdf.parse(startDate));
                dao.update(CD,UP);
                lpo.setRemark(remark);
                lpo.setDefId(ttPartContractDefinePO.getDefId());
                lpo.setContractSdate(sdf.parse(startDate));
                lpo.setContractEdate(sdf.parse(startDate));
                lpo.setContractNumber(ConNum);
                lpo.setContractType(Integer.parseInt(Contype));
                lpo.setCreateBy(logonUser.getUserId());
                lpo.setCreateDate(new Date());
                lpo.setVenderId(ttPartContractDefinePO.getVenderId());
                lpo.setPartId(ttPartContractDefinePO.getPartId());
                dao.insert(lpo);
            }

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
            partContractQueryInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "保存合同管理");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :    json
     * @throws : LastDate    : 2013-4-6
     * @Title : 合同管理-查询
     */
    public void updateByContract() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String contractNumber = CommonUtils.checkNull(request.getParamValue("CONTRACT_NUMBER")); //供应商编码
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.getContractQueryInit(contractNumber,Constant.PAGE_SIZE_MAX, curPage);
           // Constant.PAGE_SIZE
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "合同管理查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 返回配件信息
     */
    public void getVenderIsSign() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String contractType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); // 合同类型
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); // 供应商CODE
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); // 配件名称
            String planName = CommonUtils.checkNull(request.getParamValue("PLAN_NAME")); // 计划员
            String buyName = CommonUtils.checkNull(request.getParamValue("BUY_NAME")); // 采购员
            String venderType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); // 供应商类型
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartVender(contractType,partName,planName,buyName,venderType,venderCode, 10, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @Title : 返回配件信息
     */
    public void getVenderIsSignSim() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String contractType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); // 合同类型
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); // 供应商CODE
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); // 配件名称
            String planName = CommonUtils.checkNull(request.getParamValue("PLAN_NAME")); // 计划员
            String buyName = CommonUtils.checkNull(request.getParamValue("BUY_NAME")); // 采购员
            String venderType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); // 供应商类型
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartVender(contractType,venderType,venderCode, 10, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title : 导出配件采购属性Excel
     */
    public void expPartContractExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String contractType = CommonUtils.checkNull(request.getParamValue("CONTRACT_TYPE")); // 合同类型
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE")); // 供应商CODE
            String partName = CommonUtils.checkNull(request.getParamValue("PART_NAME")); // 配件名称
            String planName = CommonUtils.checkNull(request.getParamValue("PLAN_NAME")); // 计划员
            String buyName = CommonUtils.checkNull(request.getParamValue("BUY_NAME")); // 采购员
            String venderType = CommonUtils.checkNull(request.getParamValue("VENDER_TYPE")); // 供应商类型
            String[] head = new String[7];
            head[0] = "序号";
            head[1] = "供应商编码";
            head[2] = "供应商名称";
            head[3] = "配件编码";
            head[4] = "配件名称";
            head[5] = "计划员";
            head[6] = "采购员";
            PageResult<Map<String, Object>> ps = dao.queryPartVender(contractType,partName,planName,buyName,venderType,venderCode, Constant.PAGE_SIZE_MAX, 1);
            List list1 = new ArrayList();
            if (ps.getRecords() != null && ps.getRecords().size() != 0) {
                for (int i = 0; i < ps.getRecords().size(); i++) {
                    Map map = (Map) ps.getRecords().get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[7];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("VENDER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PLANNER_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("BUY_NAME"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "合同相关信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出合同管理Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    public void partInit(){
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商ID
            act.setOutData("VENDER_ID",venderId);
            act.setForword(CONTRACT_PART_INIT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }


    public void PartContractDeatilList(){
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商ID
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.queryPartVender3(venderId, 10, curPage);
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
