package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartBuyPriceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBuyPriceHistoryPO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.po.TtPartVenderPO;
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

/**
 * @author : chejunjiang CreateDate : 2013-4-5
 * @ClassName : PartBuyPriceManager
 * @Description : 配件采购价格维护
 */
@SuppressWarnings("unchecked")
public class PartBuyPriceManager extends PartBaseImport implements PTConstants {

    public Logger logger = Logger.getLogger(PartBuyPriceManager.class);
    private PartBuyPriceDao dao = PartBuyPriceDao.getInstance();
    private static int typeYes = Constant.PART_BASE_FLAG_YES;
    private static int typeNo = Constant.PART_BASE_FLAG_NO;

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, String tableName)
            throws Exception {

        String name = tableName + ".xls";
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
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
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
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 查询初始化
     */
    public void partBuyPriceQueryInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            // 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());

            act.setForword(PART_BUYPRICE_QUERY_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 分页查询配件采购价格
     */
    public void queryPartBuyPriceInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));// 件号
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));// 配件编码
            partOldCode = partOldCode.toUpperCase();
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));// 配件名称
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));// 供应商名称
            String venderCode = CommonUtils.checkNull(request.getParamValue("VENDER_CODE"));// 供应商编码
            venderCode = venderCode.toUpperCase();
            String buyerName = CommonUtils.checkNull(request.getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request.getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request.getParamValue("IS_GUARD"));// 是否暂估

            int state = 0;
            int isGuard = 0;
            int base = 0; // 结算基地 艾春 9.11 添加

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryBuyPriceList(
                    partCode, partOldCode, partName, venderCode, venderName, buyerName,
                    state, isGuard, base, curPage, Constant.PAGE_SIZE, logonUser.getPoseBusType());
            // 分页方法 end
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 分页查询配件采购价格历史
     */
    public void queryPartBuyPriceHisInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request
                    .getParamValue("PART_CODE"));// 件号
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request
                    .getParamValue("PART_OLDCODE"));// 配件编码
            partOldCode = partOldCode.toUpperCase();
            String partName = CommonUtils.checkNull(request
                    .getParamValue("PART_CNAME"));// 配件名称
            String venderName = CommonUtils.checkNull(request
                    .getParamValue("VENDER_NAME"));// 供应商名称
            String venderCode = CommonUtils.checkNull(request
                    .getParamValue("VENDER_CODE"));// 供应商编码
            venderCode = venderCode.toUpperCase();
            String buyerName = CommonUtils.checkNull(request
                    .getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 是否暂估

            int state = 0;
            int isGuard = 0;

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryBuyPriceHisList(
                    partCode, partOldCode, partName, venderCode, venderName, buyerName,
                    state, isGuard, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 分页查询配件对应的制造商信息
     */
    public void queryPartMakerInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request
                    .getParamValue("PART_CODE"));// 件号
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request
                    .getParamValue("PART_OLDCODE"));// 配件编码
            partOldCode = partOldCode.toUpperCase();
            String partName = CommonUtils.checkNull(request
                    .getParamValue("PART_CNAME"));// 配件名称
            /*String venderName = CommonUtils.checkNull(request
                    .getParamValue("VENDER_NAME"));// 供应商名称
            String venderCode = CommonUtils.checkNull(request
                    .getParamValue("VENDER_CODE"));// 供应商编码
            venderCode = venderCode.toUpperCase();*/
            String buyerName = CommonUtils.checkNull(request
                    .getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 是否暂估
            String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地 

            int state = 0;
            int isGuard = 0;

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartMakerList(
                    partCode, partOldCode, partName, str_isChange, buyerName,
                    state, isGuard, curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 添加采购价格初始化
     */
    public void addPartBuyPriceInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_BUYPRICE_ADD_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "采购价格添加");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 配件供应商制造商关系维护
     */
    public void partVenderMakerInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String sqlStr = " AND PD.PART_ID = '" + partId + "' ";
            List<Map<String, Object>> bpList = dao.getPartInfo(sqlStr);
            Map<String, Object> bpMap = null;
            if (null != bpList && bpList.size() == 1) {
                bpMap = bpList.get(0);
            }
            
            List<Map<String, Object>> venderList = dao.getPrtPrcList(sqlStr);

            act.setOutData("bpMap", bpMap);
            act.setOutData("venderList", venderList);
            act.setForword(PART_VENDER_MAKER);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件供应商制造商关系维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 配件供应商制造商关系维护查询
     */
    public void relationSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));// 配件ID
//            String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));// 供应商ID

            StringBuffer sbStr = new StringBuffer();

            if (!"".equals(partId)) {
                sbStr.append(" AND RL.PART_ID = '" + partId + "' ");
            }
           /* if (!"".equals(venderId)) {
                sbStr.append(" AND RL.VENDER_ID = '" + venderId + "' ");
            }*/
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.relationSearch(sbStr.toString(), curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件供应商制造商关系维护查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 设置配件造商关系
     */
    public void saveRelation() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
            String newMakerId = CommonUtils.checkNull(request.getParamValue("checkedOption")); //新默认制造商ID
            String prvMakerId = CommonUtils.checkNull(request.getParamValue("prvMakerId"));//前默认制造商ID

            int typeYes = Constant.PART_BASE_FLAG_YES;
            int typeNo = Constant.PART_BASE_FLAG_NO;

            Long userId = logonUser.getUserId();

            TtPartMakerRelationPO selMPo = null;
            TtPartMakerRelationPO updMPo = null;

            Date date = new Date();

            if (null != prvMakerId && !"".equals(prvMakerId) && !newMakerId.equals(prvMakerId)) {
                selMPo = new TtPartMakerRelationPO();
                updMPo = new TtPartMakerRelationPO();

                selMPo.setPartId(Long.parseLong(partId));
                selMPo.setMakerId(Long.parseLong(prvMakerId));

                updMPo.setIsDefault(typeNo);
                updMPo.setUpdateBy(userId);
                updMPo.setUpdateDate(date);

                dao.update(selMPo, updMPo);

                selMPo = new TtPartMakerRelationPO();
                updMPo = new TtPartMakerRelationPO();

                selMPo.setPartId(Long.parseLong(partId));
                selMPo.setMakerId(Long.parseLong(newMakerId));

                updMPo.setIsDefault(typeYes);
                updMPo.setUpdateBy(userId);
                updMPo.setUpdateDate(date);

                dao.update(selMPo, updMPo);

            }

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "设置默认供应商及最小包装量失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 有效或失效配件制造商关系
     */
    public void partVderMkerState() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
            String makerId = CommonUtils.checkNull(request.getParamValue("makerId"));//制造商ID
            String option = CommonUtils.checkNull(request.getParamValue("option")); //操作类型
            String success = "有效操作成功!";

            Long userId = logonUser.getUserId();

            TtPartMakerRelationPO selRPo = null;
            TtPartMakerRelationPO updRPo = null;

            Date date = new Date();

            selRPo = new TtPartMakerRelationPO();
            updRPo = new TtPartMakerRelationPO();

            selRPo.setPartId(Long.parseLong(partId));
            selRPo.setMakerId(Long.parseLong(makerId));

            if ("disable".equals(option)) {
                updRPo.setState(Constant.STATUS_DISABLE);
//				updRPo.setDfMaker(Constant.IF_TYPE_NO);
                success = "失效操作成功!";
            } else {
                updRPo.setState(Constant.STATUS_ENABLE);
            }

            updRPo.setUpdateBy(userId);
            updRPo.setUpdateDate(date);

            dao.update(selRPo, updRPo);

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            act.setOutData("success", success);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "设置默认供应商及最小包装量失败!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 制造商选择初始化
     */
    public void queryMakerInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id

            act.setOutData("partId", partId);
            act.setForword(SEL_MAKER_FOR_MOD);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件供应商制造商关系维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 选择制造商初始化
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-10-29
     */
    public void queryVenderInit() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id

            act.setOutData("partId", partId);
            act.setForword(SEL_VENDER_FOR_MOD);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件供应商制造商关系维护");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 获取制造商信息
     */
    public void queryMakersDialog() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String makerCode = CommonUtils.checkNull(request.getParamValue("makerCode")); // 制造商编码
            String makerName = CommonUtils.checkNull(request.getParamValue("makerName")); // 制造商名称

            StringBuffer sql = new StringBuffer();

            if (null != makerCode && !"".equals(makerCode)) {
                sql.append(" AND UPPER(MD.MAKER_CODE) LIKE '%" + makerCode.trim().toUpperCase() + "%' ");
            }

            if (null != makerName && !"".equals(makerName)) {
                sql.append(" AND MD.MAKER_NAME LIKE '%" + makerName.trim() + "%' ");
            }

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = dao.getMakers(sql.toString(), 13, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询人员信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-8-5
     * @Title : 新增配件制造商关系
     */
    public void insertRelation() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
            String buyPrice = "0"; //采购价格
            String makers = CommonUtils.checkNull(request.getParamValue("makers")); //新增制造商
            
            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();

            TtPartMakerRelationPO insPMRPo = null;
            TtPartMakerRelationPO selPMRPo = null;
            TtPartMakerRelationPO updPMRPo = null;

            String makersArr[] = makers.split(",");
            for (int i = 0; i < makersArr.length; i++) {
                List<Map<String, Object>> existlist = dao.partMakerCheck(makersArr[i].toString(), partId);
                if (null != existlist && existlist.size() > 0) {
                    errorExist += existlist.get(0).get("MAKER_CODE") + ", ";
                }

            }
            if ("".equals(errorExist)) {
            	String sqlStr = " AND BP.PART_ID = '" + partId + "' ";
                sqlStr += " AND BP.IS_DEFAULT = '"+ Constant.IF_TYPE_YES +"' ";
                
                List<Map<String, Object>> bpList = dao.getPartBuyPriceList(sqlStr);
                
                if(null != bpList && bpList.size() > 0)
                {
                	buyPrice = bpList.get(0).get("BUY_PRICE").toString();
                }
                
                for (int i = 0; i < makersArr.length; i++) {
                    String makerId = makersArr[i];

                    insPMRPo = new TtPartMakerRelationPO();
                    insPMRPo.setRelaionId(Long.parseLong(SequenceManager.getSequence("")));
                    insPMRPo.setPartId(Long.parseLong(partId));
                    insPMRPo.setMakerId(Long.parseLong(makerId));
                    insPMRPo.setCreateDate(date);
                    insPMRPo.setCreateBy(userId);
                    insPMRPo.setClaimPrice(Double.parseDouble(buyPrice));
                    insPMRPo.setState(Constant.STATUS_ENABLE);

                    dao.insert(insPMRPo);
                }
            }

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增制造商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 新增配件供应商关系
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-10-29
     */
    public void insertPartVenderRelation() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        String errorExist = "";
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
            String buyPrice = "0"; //采购价格
            String minPkg = "1";///最小包装量
            String venders = CommonUtils.checkNull(request.getParamValue("makers")); //新增供应商
            
            Long userId = logonUser.getUserId();// 用户ID
            Date date = new Date();

            TtPartBuyPricePO insPMRPo = null;

            String vendersArr[] = venders.split(",");
            for (int i = 0; i < vendersArr.length; i++) {
            	String sqlStr = " AND BP.PART_ID = '"+ partId +"' AND BP.VENDER_ID = '"+ vendersArr[i].toString() +"' ";
                List<Map<String, Object>> existlist = dao.getPartBuyPriceList(sqlStr);
                if (null != existlist && existlist.size() > 0) {
                    errorExist += existlist.get(0).get("VENDER_CODE") + ", ";
                }

            }
            if ("".equals(errorExist)) {
            	String sqlStr = " AND BP.PART_ID = '" + partId + "' ";
                sqlStr += " AND BP.IS_DEFAULT = '"+ Constant.IF_TYPE_YES +"' ";
                
                List<Map<String, Object>> bpList = dao.getPartBuyPriceList(sqlStr);
                
                if(null != bpList && bpList.size() > 0)
                {
                	buyPrice = bpList.get(0).get("BUY_PRICE").toString();
                	minPkg = bpList.get(0).get("MIN_PACKAGE").toString();
                }
                
                for (int i = 0; i < vendersArr.length; i++) {
                    String venderId = vendersArr[i];

                    insPMRPo = new TtPartBuyPricePO();
                    insPMRPo.setPriceId(Long.parseLong(SequenceManager.getSequence("")));
                    insPMRPo.setPartId(Long.parseLong(partId));
                    insPMRPo.setVenderId(Long.parseLong(venderId));
                    insPMRPo.setBuyPrice(Double.parseDouble(buyPrice));
                    insPMRPo.setClaimPrice(Double.parseDouble(buyPrice));
                    insPMRPo.setMinPackage(Long.parseLong(minPkg));
                    insPMRPo.setIsDefault(Constant.IF_TYPE_NO);
                    insPMRPo.setIsGuard(Constant.IS_GUARD_NO);
                    insPMRPo.setCreateDate(date);
                    insPMRPo.setCreateBy(userId);
                    insPMRPo.setStatus(1);
                    insPMRPo.setState(Constant.STATUS_ENABLE);

                    dao.insert(insPMRPo);
                }
            }

            act.setOutData("errorExist", errorExist);// 关系记录存在
            act.setOutData("success", "true");

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "新增供应商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-5
     * @Title :
     * @Description: 分页查询配件信息
     */
    public void queryPartInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldCode"));// 配件代码
            partOldcode = partOldcode.toUpperCase();
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));// 配件名称
            String isChanghe = "";// 结算基地
            if (null != request.getParamValue("PART_IS_CHANGHE") && !"".equals(request.getParamValue("PART_IS_CHANGHE"))) {
                isChanghe = request.getParamValue("PART_IS_CHANGHE");
            }

            TtPartDefinePO bean = new TtPartDefinePO();
            bean.setPartOldcode(partOldcode);
            bean.setPartCname(partCname);
            if (!"".equals(isChanghe)) {
                bean.setPartIsChanghe(Integer.valueOf(isChanghe));
            }
            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartInfoList(bean, curPage, 10);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    /**
     * 分页查询索赔的单号
     */
    public void queryPartFirstInfo() {
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	String CLAIM_NO = CommonUtils.checkNull(request.getParamValue("CLAIM_NO"));//索赔单号
        String VIN = CommonUtils.checkNull(request.getParamValue("VIN"));// VIN
    	try {
    		// 分页方法 begin
    		Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
    		PageResult<Map<String, Object>> ps = dao.queryPartFirstInfoList(logonUser.getDealerId(),CLAIM_NO,VIN, curPage, 100);
    		// 分页方法 end
    		act.setOutData("ps", ps);
    	} catch (Exception e) {// 异常方法
    		BizException e1 = new BizException(act, e,
    				ErrorCodeConstant.QUERY_FAILURE_CODE, "配件信息");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-6
     * @Title :
     * @Description: 分页查询供应商
     */
    public void queryPartVenderInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String venderCode = CommonUtils.checkNull(request
                    .getParamValue("venderCode"));// 供应商代码
            String venderName = CommonUtils.checkNull(request
                    .getParamValue("venderName"));// 供应商名称
            String notExistsPartId = CommonUtils.checkNull(request
                    .getParamValue("notExistsPartId"));// 不包含的配件
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("venderCode", venderCode);
            paramMap.put("venderName", venderName);
            paramMap.put("notExistsPartId", notExistsPartId);
//            TtPartVenderDefinePO bean = new TtPartVenderDefinePO();
//            bean.setVenderCode(venderCode);
//            bean.setVenderName(venderName);

            // 分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartVenderList(paramMap,
                    curPage, Constant.PAGE_SIZE);
            // 分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-6
     * @Title :
     * @Description: 添加采购价格 ,同时也需要往价格历史表中插入数据,如果已经存在配件与供应商对应的价格,则提示已经存在
     */
    public void addPartBuyPriceInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            String str_partId = CommonUtils.checkNull(request
                    .getParamValue("PART_ID"));// 获取配件id
            String str_venderId = CommonUtils.checkNull(request
                    .getParamValue("VENDER_ID"));// 获取供应商id
            String str_buyPrice = CommonUtils.checkNull(request
                    .getParamValue("BUY_PRICE"));// 获取采购价格
//			String str_planPrice = CommonUtils.checkNull(request
//					.getParamValue("PLAN_PRICE"));// 获取计划价格
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 获取是否暂估

            Map<String, Object> map = dao.checkPartIdAndVenderId(
                    Long.parseLong(str_partId), Long.parseLong(str_venderId));

            if (map != null) {// 已经存在
                act.setOutData("error", "该配件与供应商对应的价格已经存在,请重新选择!");
                return;
            }

            TtPartBuyPricePO ttPartBuyPricePO = new TtPartBuyPricePO();
            ttPartBuyPricePO.setPriceId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttPartBuyPricePO.setBuyPrice(CommonUtils.parseDouble(str_buyPrice));
            ttPartBuyPricePO.setClaimPrice(CommonUtils.parseDouble(str_buyPrice));
//			ttPartBuyPricePO.setPlanPrice(CommonUtils.parseDouble(str_planPrice));
            ttPartBuyPricePO.setIsGuard(CommonUtils.parseInteger(str_isGuard));
            ttPartBuyPricePO.setCreateDate(new Date());
            ttPartBuyPricePO.setCreateBy(logonUser.getUserId());
            ttPartBuyPricePO.setState(Constant.STATUS_ENABLE);
            ttPartBuyPricePO.setStatus(1);
            ttPartBuyPricePO.setVenderId(CommonUtils.parseLong(str_venderId));
            ttPartBuyPricePO.setPartId(CommonUtils.parseLong(str_partId));

			/*// 保存到价格历史表
            TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
			ttPartBuyPriceHistoryPO.setHistoryId(CommonUtils
					.parseLong(SequenceManager.getSequence("")));
			ttPartBuyPriceHistoryPO.setBuyPrice(CommonUtils
					.parseDouble(str_buyPrice));
			ttPartBuyPriceHistoryPO.setIsGuard(CommonUtils
					.parseInteger(str_isGuard));
			ttPartBuyPriceHistoryPO.setCreateDate(new Date());
			ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
			ttPartBuyPriceHistoryPO
					.setPartId(CommonUtils.parseLong(str_partId));
			ttPartBuyPriceHistoryPO.setStatus(1);
			ttPartBuyPriceHistoryPO.setVenderId(CommonUtils
					.parseLong(str_venderId));
			ttPartBuyPriceHistoryPO.setState(Constant.STATUS_ENABLE);
*/
            dao.insert(ttPartBuyPricePO);
            //dao.insert(ttPartBuyPriceHistoryPO);
            
            TtPartVenderPO vpPO = new TtPartVenderPO();
            vpPO.setPartId(ttPartBuyPricePO.getPartId());
            vpPO.setVenderId(ttPartBuyPricePO.getVenderId());
            List<TtPartVenderPO> vpPOList = dao.select(vpPO);
            if(vpPOList.size() == 0){
                vpPO.setSvId(Long.valueOf(SequenceManager.getSequence("")));
                vpPO.setCreateDate(new Date());
                vpPO.setCreateUser(logonUser.getUserId());
                vpPO.setStatus(1L);
                vpPO.setIsDefult(1);
                vpPO.setState(Constant.IF_TYPE_YES);
                dao.insert(vpPO);
            }
            
            act.setOutData("success", "添加成功!");

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.ADD_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : 修改时需要保存一份最新的记录到配件价格历史表中
     * @return :
     * @throws : LastDate : 2013-4-6
     * @Title :
     * @Description: 修改配件价格信息
     */
    public void updatePartBuyPrice() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String str_buyPriceId = CommonUtils.checkNull(request
                    .getParamValue("buyPriceId"));// 获取配件价格id
            String str_partId = CommonUtils.checkNull(request
                    .getParamValue("partId"));// 获取配件id
            String str_buyPrice = CommonUtils.checkNull(request
                    .getParamValue("buyPrice"));// 获取配件采购价格
            str_buyPrice = str_buyPrice.replaceAll(",", "");
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("isGuard"));// 获取是否暂估
            
            String minPkg = request.getParamValue("minPkg");// 最小包装量
            String prvDefBuyPriceId = request.getParamValue("prvDefBuyPriceId");// 旧配件默认采购
            String newDefBuyPriceId = request.getParamValue("newDefBuyPriceId");// 新配件默认采购

            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            
            //默认供应商处理
            if(null != prvDefBuyPriceId && !"".equals(prvDefBuyPriceId) && null != newDefBuyPriceId && !"".equals(newDefBuyPriceId))
            {
            	TtPartBuyPricePO selPo = null;
            	TtPartBuyPricePO updPo = null;
            	TtPartBuyPricePO selPo1 = null;
            	TtPartBuyPricePO updPo1 = null;
            	long userId = logonUser.getUserId();
            	Date date = new Date();
            	if(!prvDefBuyPriceId.equals(newDefBuyPriceId))
            	{
            		selPo = new TtPartBuyPricePO();
            		updPo = new TtPartBuyPricePO();
            		
            		selPo.setPriceId(CommonUtils.parseLong(prvDefBuyPriceId));
            		
            		updPo.setIsDefault(Constant.IF_TYPE_NO);
            		updPo.setUpdateBy(userId);
            		updPo.setUpdateDate(date);
            		
            		dao.update(selPo, updPo);
            		
            		selPo1 = new TtPartBuyPricePO();
            		updPo1 = new TtPartBuyPricePO();
            		
            		selPo1.setPriceId(CommonUtils.parseLong(newDefBuyPriceId));
            		
            		updPo1.setIsDefault(Constant.IF_TYPE_YES);
            		updPo1.setUpdateBy(userId);
            		updPo1.setUpdateDate(date);
            		
            		dao.update(selPo1, updPo1);
            	}
            }
            else if((null == prvDefBuyPriceId || "".equals(prvDefBuyPriceId)) && null != newDefBuyPriceId && !"".equals(newDefBuyPriceId))
            {
            	TtPartBuyPricePO selPo1 = null;
            	TtPartBuyPricePO updPo1 = null;
            	
            	long userId = logonUser.getUserId();
            	Date date = new Date();
            	
            	selPo1 = new TtPartBuyPricePO();
        		updPo1 = new TtPartBuyPricePO();
        		
        		selPo1.setPriceId(CommonUtils.parseLong(newDefBuyPriceId));
        		
        		updPo1.setIsDefault(Constant.IF_TYPE_YES);
        		updPo1.setUpdateBy(userId);
        		updPo1.setUpdateDate(date);
        		
        		dao.update(selPo1, updPo1);
            	
            }

            TtPartBuyPricePO spo = new TtPartBuyPricePO();// 源po
            spo.setPriceId(CommonUtils.parseLong(str_buyPriceId));

            TtPartBuyPricePO po = new TtPartBuyPricePO();// 更新po
            po.setBuyPrice(CommonUtils.parseDouble(str_buyPrice));
            //po.setPlanPrice(CommonUtils.parseDouble(str_planPrice));//此处不再需要更新 modify by yuan 20130701
            po.setClaimPrice(CommonUtils.parseDouble(str_buyPrice));
            po.setIsGuard(CommonUtils.parseInteger(str_isGuard));
            if(null != minPkg && !"".equals(minPkg))
            {
            	po.setMinPackage(Long.parseLong(minPkg));
            }
            po.setUpdateBy(logonUser.getUserId());
            po.setUpdateDate(new Date());

            TtPartBuyPricePO src = new TtPartBuyPricePO();
            src.setPriceId(CommonUtils.parseLong(str_buyPriceId));
            List<TtPartBuyPricePO> lp = dao.select(src);
            src = lp.get(0);

            TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
            ttPartBuyPriceHistoryPO.setHistoryId(CommonUtils
                    .parseLong(SequenceManager.getSequence("")));
            ttPartBuyPriceHistoryPO.setPriceId(CommonUtils.parseLong(str_buyPriceId));
            ttPartBuyPriceHistoryPO.setBuyPrice(CommonUtils
                    .parseDouble(str_buyPrice));
            /*ttPartBuyPriceHistoryPO.setPlanPrice(CommonUtils.parseDouble(str_planPrice));*/
            ttPartBuyPriceHistoryPO.setOldBuyPrice(src.getBuyPrice());
            ttPartBuyPriceHistoryPO.setIsGuard(CommonUtils
                    .parseInteger(str_isGuard));
            ttPartBuyPriceHistoryPO.setCreateDate(new Date());
            ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
            ttPartBuyPriceHistoryPO
                    .setPartId(CommonUtils.parseLong(str_partId));
            ttPartBuyPriceHistoryPO.setState(Constant.STATUS_ENABLE);
            ttPartBuyPriceHistoryPO.setStatus(1);
            ttPartBuyPriceHistoryPO.setVenderId(src.getVenderId());
            ttPartBuyPriceHistoryPO.setPartId(src.getPartId());
            dao.update(spo, po);
            dao.insert(ttPartBuyPriceHistoryPO);
            act.setOutData("success", "保存成功!");
            act.setOutData("curPage", curPage);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件采购价格");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-6
     * @Title :
     * @Description: 让配件价格失效
     */
    public void celPartBuyPrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            String buyPriceId = CommonUtils.checkNull(request.getParamValue("buyPriceId")); // 采购价格Id
            String str_partId = CommonUtils.checkNull(request.getParamValue("partId"));// 获取配件id
            String str_buyPrice = CommonUtils.checkNull(request.getParamValue("buyPrice"));// 获取配件采购价格
            String str_isGuard = CommonUtils.checkNull(request.getParamValue("isGuard"));// 获取是否暂估
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));// 当前页
            
            if(StringUtil.notNull(str_buyPrice)){
                str_buyPrice = str_buyPrice.replaceAll(",", "");
            }

            TtPartBuyPricePO src = new TtPartBuyPricePO();
            src.setPriceId(CommonUtils.parseLong(buyPriceId));
            List<TtPartBuyPricePO> lp = dao.select(src);
            src = lp.get(0);

            TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
            ttPartBuyPriceHistoryPO.setHistoryId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttPartBuyPriceHistoryPO.setPriceId(CommonUtils.parseLong(buyPriceId));
            ttPartBuyPriceHistoryPO.setBuyPrice(CommonUtils.parseDouble(str_buyPrice));
            /*ttPartBuyPriceHistoryPO.setPlanPrice(CommonUtils.parseDouble(str_planPrice));*/
            ttPartBuyPriceHistoryPO.setOldBuyPrice(src.getBuyPrice());
            ttPartBuyPriceHistoryPO.setIsGuard(CommonUtils.parseInteger(str_isGuard));
            ttPartBuyPriceHistoryPO.setCreateDate(new Date());
            ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
            ttPartBuyPriceHistoryPO.setPartId(CommonUtils.parseLong(str_partId));
            ttPartBuyPriceHistoryPO.setState(Constant.STATUS_DISABLE);
            ttPartBuyPriceHistoryPO.setStatus(1);
            ttPartBuyPriceHistoryPO.setVenderId(src.getVenderId());
            ttPartBuyPriceHistoryPO.setPartId(src.getPartId());
            dao.insert(ttPartBuyPriceHistoryPO);

            TtPartBuyPricePO spo = new TtPartBuyPricePO();// 源po
            spo.setPriceId(CommonUtils.parseLong(buyPriceId));

            TtPartBuyPricePO po = new TtPartBuyPricePO();// 更新po
            po.setState(Constant.STATUS_DISABLE);
            po.setDisableBy((logonUser.getUserId()));
            po.setDisableDate(new Date());

            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.update(spo, po);
            act.setOutData("success", "设置无效成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.UPDATE_FAILURE_CODE, "采购价格失效");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: 让采购价格有效
     */
    public void selPartBuyPrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            String buyPriceId = CommonUtils.checkNull(request
                    .getParamValue("buyPriceId")); // 采购价格Id
            String str_partId = CommonUtils.checkNull(request
                    .getParamValue("partId"));// 获取配件id
            String str_buyPrice = CommonUtils.checkNull(request
                    .getParamValue("buyPrice"));// 获取配件采购价格
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("isGuard"));// 获取是否暂估
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            
            if(StringUtil.notNull(str_buyPrice)){
                str_buyPrice = str_buyPrice.replaceAll(",", "");
            }

            TtPartBuyPricePO src = new TtPartBuyPricePO();
            src.setPriceId(CommonUtils.parseLong(buyPriceId));
            List<TtPartBuyPricePO> lp = dao.select(src);
            src = lp.get(0);

            TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
            ttPartBuyPriceHistoryPO.setHistoryId(CommonUtils
                    .parseLong(SequenceManager.getSequence("")));
            ttPartBuyPriceHistoryPO.setPriceId(CommonUtils.parseLong(buyPriceId));
            ttPartBuyPriceHistoryPO.setBuyPrice(CommonUtils
                    .parseDouble(str_buyPrice));
            /*ttPartBuyPriceHistoryPO.setPlanPrice(CommonUtils.parseDouble(str_planPrice));*/
            ttPartBuyPriceHistoryPO.setOldBuyPrice(src.getBuyPrice());
            ttPartBuyPriceHistoryPO.setIsGuard(CommonUtils
                    .parseInteger(str_isGuard));
            ttPartBuyPriceHistoryPO.setCreateDate(new Date());
            ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
            ttPartBuyPriceHistoryPO
                    .setPartId(CommonUtils.parseLong(str_partId));
            ttPartBuyPriceHistoryPO.setState(Constant.STATUS_ENABLE);
            ttPartBuyPriceHistoryPO.setStatus(1);
            ttPartBuyPriceHistoryPO.setVenderId(src.getVenderId());
            ttPartBuyPriceHistoryPO.setPartId(src.getPartId());
            dao.insert(ttPartBuyPriceHistoryPO);

            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.selPartBuyPrice(buyPriceId);
            act.setOutData("success", "设置有效成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.UPDATE_FAILURE_CODE, "采购价格有效");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: 下载采购价格模板
     */
    public void exportBuyPriceTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            // 用于下载传参的集合
            List<List<Object>> list = new LinkedList<List<Object>>();

            // 标题
            List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
            listHead.add("配件编码");
            listHead.add("供应商编码");
            listHead.add("供应商名称");
//            listHead.add("制造商编码");
//            listHead.add("制造商名称");
            listHead.add("采购价格");
//            listHead.add("是否暂估价（1:是；0：否）");
//            listHead.add("最小包装量");
//            listHead.add("是否默认供应商(1:是；0：否)");
//            listHead.add("是否默认制造商(1:是；0：否)");

            list.add(listHead);
            // 导出的文件名
            String fileName = "配件采购价格维护模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename="
                    + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
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
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: 导出采购价格数据
     */
    public void exportPartBuyPriceExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            RequestWrapper request = act.getRequest();
            List<Object> headList = new ArrayList<Object>();
            headList.add("配件编码");
            headList.add("配件名称");
            headList.add("件号");
            headList.add("供应商编码");
            headList.add("供应商名称");
            headList.add("采购价格(元)");
            headList.add("最小包装量");
            headList.add("是否暂估");
            headList.add("是否有效");
            
            List<Map<String, Object>> list = dao.queryPartBuyPrice(request);
            List<List<Object>> expExcelList = new ArrayList<List<Object>>();
            expExcelList.add(headList);
            
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    if (map != null && map.size() != 0) {
                        List<Object> bodyList = new ArrayList<Object>();
                        
                        bodyList.add(CommonUtils.checkNull(map.get("PART_OLDCODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_CNAME")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_CODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("VENDER_CODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("VENDER_NAME")));
                        bodyList.add(CommonUtils.checkNull(map.get("BUY_PRICE")));
                        bodyList.add(CommonUtils.checkNull(map.get("MIN_PACKAGE")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_GUARD")));
                        bodyList.add(CommonUtils.checkNull(map.get("STATE")));

                        expExcelList.add(bodyList);
                    }
                }
            } else {
                BizException e1 = new BizException(act,  ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

            String fileName = "配件采购价格信息.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(expExcelList, os);
            os.flush();
            
        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BUYPRICE_QUERY_URL);
        }

    }
    
    /**
     * 
     * @Title      : 制造商信息导出
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-12-6
     */
    public void exportMakersExcel() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            RequestWrapper request = act.getRequest();
            String partCode = CommonUtils.checkNull(request
                    .getParamValue("PART_CODE"));// 件号
            partCode = partCode.toUpperCase();
            String partOldCode = CommonUtils.checkNull(request
                    .getParamValue("PART_OLDCODE"));// 配件编码
            partOldCode = partOldCode.toUpperCase();
            String partName = CommonUtils.checkNull(request
                    .getParamValue("PART_CNAME"));// 配件名称
            /*String venderName = CommonUtils.checkNull(request
                    .getParamValue("VENDER_NAME"));// 供应商名称
            String venderCode = CommonUtils.checkNull(request
                    .getParamValue("VENDER_CODE"));// 供应商编码
            venderCode = venderCode.toUpperCase();*/
            String buyerName = CommonUtils.checkNull(request
                    .getParamValue("BUYER_NAME"));// 采购员
            String str_state = CommonUtils.checkNull(request
                    .getParamValue("STATE"));// 是否有效
            String str_isGuard = CommonUtils.checkNull(request
                    .getParamValue("IS_GUARD"));// 是否暂估
            String str_isChange = CommonUtils.checkNull(request.getParamValue("PART_IS_CHANGHE"));// 结算基地 

            int state = 0;
            int isGuard = 0;

            if (!"".equals(str_state)) {
                state = CommonUtils.parseInteger(str_state);
            }
            if (!"".equals(str_isGuard)) {
                isGuard = CommonUtils.parseInteger(str_isGuard);
            }
            
            String[] head = new String[15];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            head[3] = "制造商编码";
            head[4] = "制造商名称";
            head[5] = "是否默认";
            head[6] = "是否有效";
            
            PageResult<Map<String, Object>> ps = dao.queryPartMakerList(
                    partCode, partOldCode, partName, str_isChange, buyerName,
                    state, isGuard, 1, Constant.PAGE_SIZE_MAX);
            List<Map<String, Object>> list = ps.getRecords();
            
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[0] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[1] = CommonUtils
                                .checkNull(map.get("PART_CNAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("MAKER_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("MAKER_NAME"));
                        Object isdft = map.get("IS_DEFAULT");
                        Object isUsable = map.get("STATE");
                        if(null != isdft && (Constant.IF_TYPE_YES + "").equals(isdft.toString()))
                        {
                        	detail[5] = "是";
                        }
                        else
                        {
                        	detail[5] = "否";
                        }
                        
                        if(null != isUsable && (Constant.STATUS_ENABLE + "").equals(isUsable.toString()))
                        {
                        	detail[6] = "有效";
                        }
                        else
                        {
                        	detail[6] = "无效";
                        }

                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "制造商信息");
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BUYPRICE_QUERY_URL);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: 导入采购价格
     */
    public void uploadBuyPriceExcel() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 4, 0, maxSize);

            String err = "";

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多,请修改后再上传!";
                        break;
                    case 2:
                        err += "不能有空行,请修改后再上传!";
                        break;
                    case 3:
                        err += "文件内容不能为空,请修改后再上传!";
                        break;
                    case 4:
                        err += "文件类型错误,请重新上传!";
                        break;
                    case 5:
                        err += "文件不能大于" + maxSize + ",请修改后再上传";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {
                List<Map> list = getMapList();
                for(int i = 0; i < list.size(); i++){
                    Map map = list.get(i);
                    if(map == null){
                        map = new HashMap<String, Cell[]>();
                    }
                    Set<String> keys = map.keySet();
                    Iterator<String> it = keys.iterator();
                    String key = "";
                    while (it.hasNext()) {
                        key = (String) it.next();
                        Cell[] cells = (Cell[]) map.get(key);
                        TtPartBuyPricePO ttPartBuyPricePO = new TtPartBuyPricePO();
                        
                        //配件编码  供应商编码   供应商名称   采购价格    是否暂估价（1:是；0：否）
                        String partCode = cells[0].getContents().trim();
                        if(CommonUtils.isEmpty(partCode)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行配件编码错误！");
                            break;
                        }
                        // 获取配件id
                        Map<String, Object> partMap = dao.validatePartOldcode(partCode);
                        if (partMap == null || CommonUtils.isEmpty(partMap.get("PART_ID").toString())) {
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行的配件编码不存在,请修改后再上传!");
                            return;
                        }
                        ttPartBuyPricePO.setPartId(((BigDecimal) partMap.get("PART_ID")).longValue()); 
                        
                        // 供应商编码
                        String venderCode = cells[1].getContents().trim();
                        if(CommonUtils.isEmpty(venderCode)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行供应商编码错误！");
                            break;
                        }
                        
                        // 供应商名称
                        String venderName = cells[2].getContents().trim();
                        if(CommonUtils.isEmpty(venderName)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行供应商名称错误！");
                            break;
                        }
                        
                        //供应商验证
                        Long venderId = null;
                        if (venderName != "" && venderCode != "") {
                            map = dao.validatePartVenderName(venderName);
                            if (map == null) {
                                //新增Vender
                                TtPartVenderDefinePO insVDPo = new TtPartVenderDefinePO();

                                insVDPo.setVenderId(Long.parseLong(SequenceManager.getSequence("")));
                                insVDPo.setVenderCode(venderCode);
                                insVDPo.setVenderName(venderName);
                                insVDPo.setVenderType(Constant.PARTVENDER_INNER);
                                insVDPo.setIsAbroad(Constant.PARTVENDER_INNER);
                                insVDPo.setCreateBy(Long.parseLong((-1) + ""));
                                insVDPo.setCreateDate(new Date());

                                dao.insert(insVDPo);

                                map = dao.validatePartVenderName(venderName);
                                venderId = ((BigDecimal) map.get("VENDER_ID")).longValue();
                            } else {
                                venderId = ((BigDecimal) map.get("VENDER_ID")).longValue();
                            }

                            ttPartBuyPricePO.setVenderId(venderId);
                        }
                        
                        
                        // 采购价格
                        String buyPrice = cells[3].getContents().trim();
                        if(CommonUtils.isEmpty(buyPrice)){
                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行采购价格错误！");
                            break;
                        }
                        
                        // 是否暂估价
//                        String isGurad = cells[4].getContents().trim();
//                        if(CommonUtils.isEmpty(isGurad)){
//                            errorInfo.append("第" + (Integer.parseInt(key) - 1) + "行是否暂估价错误！");
//                            break;
//                        }
//                        if ("1".equals(isGurad)) {
//                            ttPartBuyPricePO.setIsGuard(Constant.PART_BASE_FLAG_YES);
//                        } else {
//                            ttPartBuyPricePO.setIsGuard(Constant.PART_BASE_FLAG_NO);
//                        }

                        Date date = new Date();
                        //采购价格更新
                        if (ttPartBuyPricePO.getPartId() != null && ttPartBuyPricePO.getVenderId() != null) {
                            Map<String, Object> map1 = dao.checkPartIdAndVenderId(ttPartBuyPricePO.getPartId(), ttPartBuyPricePO.getVenderId());
                            // 如果已经存在该配件与供应商对应的价格，则更新
                            if (map1 != null) {
                                
                                TtPartBuyPricePO upPo = new TtPartBuyPricePO();
                                upPo.setBuyPrice(Double.parseDouble(buyPrice));
                                upPo.setUpdateBy(logonUser.getUserId());
                                upPo.setUpdateDate(new Date());
                                dao.update(ttPartBuyPricePO, upPo);
                                
//                                dao.updateBuyPrice(ttPartBuyPricePO.getPartId(),
//                                        ttPartBuyPricePO.getVenderId(),
//                                        ttPartBuyPricePO.getBuyPrice(),
//                                        ttPartBuyPricePO.getIsDefault(), logonUser);

                                //更新的时候需要把数据保存到采购价格历史表中
                                TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
                                ttPartBuyPriceHistoryPO.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                                ttPartBuyPriceHistoryPO.setPriceId(((BigDecimal) map1.get("PRICE_ID")).longValue());
                                ttPartBuyPriceHistoryPO.setVenderId(ttPartBuyPricePO.getVenderId());
                                ttPartBuyPriceHistoryPO.setBuyPrice(upPo.getBuyPrice());
//                                ttPartBuyPriceHistoryPO.setPlanPrice(Double.parseDouble(map1.get("PLAN_PRICE").toString()));
                                ttPartBuyPriceHistoryPO.setIsGuard(((BigDecimal) map1.get("IS_GUARD")).intValue());
                                ttPartBuyPriceHistoryPO.setCreateDate(date);
                                ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
                                ttPartBuyPriceHistoryPO.setState(((BigDecimal) map1.get("STATE")).intValue());
                                ttPartBuyPriceHistoryPO.setStatus(1);
                                ttPartBuyPriceHistoryPO.setPartId(ttPartBuyPricePO.getPartId());
                                dao.insert(ttPartBuyPriceHistoryPO);
                            } else {// 否则新增
                                Long priceId = Long.parseLong(SequenceManager.getSequence(""));
                                ttPartBuyPricePO.setPriceId(priceId);
                                ttPartBuyPricePO.setIsGuard(Constant.IS_GUARD_NO);
                                ttPartBuyPricePO.setCreateDate(date);
                                ttPartBuyPricePO.setCreateBy(logonUser.getUserId());
                                ttPartBuyPricePO.setState(Constant.STATUS_ENABLE);
                                ttPartBuyPricePO.setStatus(1);

                                dao.insert(ttPartBuyPricePO);
                            }
                        }
                    }
                }
                
                
//                List voList = new ArrayList();
//                List mkList = new ArrayList();
//                List pvmList = new ArrayList();
//                loadVoList(voList, mkList, pvmList, list, errorInfo);
//                
//                
//                
//                
//                if (errorInfo.length() > 0) {
//
//                    BizException e1 = new BizException(act,
//                            ErrorCodeConstant.SPECIAL_MEG, errorInfo);
//                    throw e1;
//                }
                //modify by yuan 20131001 start效率太低
                 /* Date date = new Date();
                int typeYes = Constant.PART_BASE_FLAG_YES;
                int typeNo = Constant.PART_BASE_FLAG_NO;

                //TtPartBuyPricePO
              for (int i = 0; i < voList.size(); i++) {
                    TtPartBuyPricePO ttPartBuyPricePO = (TtPartBuyPricePO) voList
                            .get(i);

                    if (typeYes == ttPartBuyPricePO.getIsDefault()) {
                        //取消原来默认
                        TtPartBuyPricePO selBPPo = new TtPartBuyPricePO();
                        TtPartBuyPricePO updBPPo = new TtPartBuyPricePO();

                        selBPPo.setPartId(ttPartBuyPricePO.getPartId());

                        updBPPo.setIsDefault(typeNo);
                        updBPPo.setUpdateBy(logonUser.getUserId());
                        updBPPo.setUpdateDate(date);

                        dao.update(selBPPo, updBPPo);

                        Thread.sleep(50);
                    }

                    Map<String, Object> map1 = dao.checkPartIdAndVenderId(
                            ttPartBuyPricePO.getPartId(),
                            ttPartBuyPricePO.getVenderId());
                    if (map1 != null) {// 如果已经存在该配件与供应商对应的价格，则更新

                        dao.updateBuyPrice(ttPartBuyPricePO.getPartId(),
                                ttPartBuyPricePO.getVenderId(),
                                ttPartBuyPricePO.getBuyPrice(), ttPartBuyPricePO.getIsDefault(), logonUser);

                        //更新的时候需要把数据保存到采购价格历史表中
                        TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
                        ttPartBuyPriceHistoryPO.setHistoryId(Long
                                .parseLong(SequenceManager.getSequence("")));
                        ttPartBuyPriceHistoryPO.setPriceId(((BigDecimal) map1.get("PRICE_ID")).longValue());
                        ttPartBuyPriceHistoryPO.setVenderId(ttPartBuyPricePO.getVenderId());
                        ttPartBuyPriceHistoryPO.setBuyPrice(ttPartBuyPricePO
                                .getBuyPrice());
                        ttPartBuyPriceHistoryPO.setPlanPrice(Double.parseDouble(map1.get("PLAN_PRICE").toString()));
                        ttPartBuyPriceHistoryPO.setIsGuard(Constant.IS_GUARD_NO);
                        ttPartBuyPriceHistoryPO.setCreateDate(date);
                        ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
                        ttPartBuyPriceHistoryPO.setState(ttPartBuyPricePO
                                .getState());
                        ttPartBuyPriceHistoryPO.setStatus(1);
                        ttPartBuyPriceHistoryPO.setPartId(ttPartBuyPricePO
                                .getPartId());
                        dao.insert(ttPartBuyPriceHistoryPO);
                    } else {// 否则新增
                        Long priceId = Long.parseLong(SequenceManager
                                .getSequence(""));
                        ttPartBuyPricePO.setPriceId(priceId);
                        ttPartBuyPricePO.setIsGuard(Constant.IS_GUARD_NO);
                        ttPartBuyPricePO.setCreateDate(date);
                        ttPartBuyPricePO.setCreateBy(logonUser.getUserId());
                        ttPartBuyPricePO.setState(Constant.STATUS_ENABLE);
                        ttPartBuyPricePO.setStatus(1);

                        dao.insert(ttPartBuyPricePO);
                    }*/
                    /*// 同时需要把数据保存到采购价格历史表中
                    TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
					ttPartBuyPriceHistoryPO.setHistoryId(Long
							.parseLong(SequenceManager.getSequence("")));
					ttPartBuyPriceHistoryPO.setVenderId(ttPartBuyPricePO.getVenderId());
					ttPartBuyPriceHistoryPO.setBuyPrice(ttPartBuyPricePO
							.getBuyPrice());
					ttPartBuyPriceHistoryPO.setIsGuard(Constant.IS_GUARD_NO);
					ttPartBuyPriceHistoryPO.setCreateDate(new Date());
					ttPartBuyPriceHistoryPO.setState(ttPartBuyPricePO
							.getState());
					ttPartBuyPriceHistoryPO.setStatus(1);
					ttPartBuyPriceHistoryPO.setPartId(ttPartBuyPricePO
							.getPartId());*/

                //dao.insert(ttPartBuyPriceHistoryPO);

            }

            //TtPartMakerRelationPO
                /*for (int i = 0; i < mkList.size(); i++) {
                    TtPartMakerRelationPO makerRelationPO = (TtPartMakerRelationPO) mkList.get(i);
                    //取消原来默认
                    if (typeYes == makerRelationPO.getIsDefault()) {
                        TtPartMakerRelationPO selMRPo = new TtPartMakerRelationPO();
                        TtPartMakerRelationPO updMRPo = new TtPartMakerRelationPO();

                        selMRPo.setPartId(makerRelationPO.getPartId());

                        updMRPo.setIsDefault(typeNo);
                        updMRPo.setUpdateBy(logonUser.getUserId());
                        updMRPo.setUpdateDate(date);

                        dao.update(selMRPo, updMRPo);

                        Thread.sleep(50);
                    }

                    Map<String, Object> mapMaker = dao.checkPartIdAndMakerId(
                            makerRelationPO.getPartId(),
                            makerRelationPO.getMakerId());
                    if (mapMaker != null) {// 如果已经存在该配件与制造商关系，则更新
                        String relaionId = mapMaker.get("RELAION_ID").toString();

                        TtPartMakerRelationPO selPo = new TtPartMakerRelationPO();
                        TtPartMakerRelationPO updPo = new TtPartMakerRelationPO();

                        selPo.setRelaionId(Long.parseLong(relaionId));

                        updPo.setIsDefault(makerRelationPO.getIsDefault());
                        updPo.setClaimPrice(makerRelationPO.getClaimPrice());
                        updPo.setUpdateBy(logonUser.getUserId());
                        updPo.setUpdateDate(date);

                        dao.update(selPo, updPo);

                    } else {// 否则新增
                        TtPartMakerRelationPO insMRPo = new TtPartMakerRelationPO();

                        insMRPo.setRelaionId(Long.parseLong(SequenceManager.getSequence("")));
                        insMRPo.setPartId(makerRelationPO.getPartId());
                        insMRPo.setMakerId(makerRelationPO.getMakerId());
                        insMRPo.setIsDefault(makerRelationPO.getIsDefault());
                        insMRPo.setClaimPrice(makerRelationPO.getClaimPrice());
                        insMRPo.setCreateBy(logonUser.getUserId());
                        insMRPo.setCreateDate(date);
                        insMRPo.setState(Constant.STATUS_ENABLE);
                        insMRPo.setStatus(1);

                        dao.insert(insMRPo);
                    }
                }*/
            //modify by yuan 20131001不再需要
            //TtPartVenderMakerRelationPO
             /*   for (int i = 0; i < pvmList.size(); i++) {
                    TtPartVenderMakerRelationPO pvmRelationPO = (TtPartVenderMakerRelationPO) pvmList.get(i);
                    TtPartVenderMakerRelationPO selMRPo = null;
                    TtPartVenderMakerRelationPO updMRPo = null;

                    //取消原来默认供应商
                    if (typeYes == pvmRelationPO.getDfVender()) {
                        selMRPo = new TtPartVenderMakerRelationPO();
                        updMRPo = new TtPartVenderMakerRelationPO();

                        selMRPo.setPartId(pvmRelationPO.getPartId());

                        updMRPo.setDfVender(typeNo);
                        updMRPo.setUpdateBy(logonUser.getUserId());
                        updMRPo.setUpdateDate(date);

                        dao.update(selMRPo, updMRPo);

                        Thread.sleep(100);

                        selMRPo.setPartId(pvmRelationPO.getPartId());
                        selMRPo.setVenderId(pvmRelationPO.getVenderId());

                        updMRPo.setDfVender(typeYes);
                        updMRPo.setUpdateBy(logonUser.getUserId());
                        updMRPo.setUpdateDate(date);

                        dao.update(selMRPo, updMRPo);
                    }
                    //取消原来默认制造商
                    if (typeYes == pvmRelationPO.getDfMaker()) {
                        selMRPo = new TtPartVenderMakerRelationPO();
                        updMRPo = new TtPartVenderMakerRelationPO();

                        selMRPo.setPartId(pvmRelationPO.getPartId());
                        selMRPo.setVenderId(pvmRelationPO.getVenderId());

                        updMRPo.setDfMaker(typeNo);
                        updMRPo.setUpdateBy(logonUser.getUserId());
                        updMRPo.setUpdateDate(date);

                        dao.update(selMRPo, updMRPo);

                        Thread.sleep(50);
                    }

                    List<Map<String, Object>> listPVM = dao.relationCheck(pvmRelationPO.getVenderId() + "", pvmRelationPO.getMakerId() + "", pvmRelationPO.getPartId() + "");

                    if (null != listPVM && listPVM.size() == 1) {// 如果已经存在该配件供应商制造商关系，则更新

                        selMRPo = new TtPartVenderMakerRelationPO();
                        updMRPo = new TtPartVenderMakerRelationPO();

                        selMRPo.setPartId(pvmRelationPO.getPartId());
                        selMRPo.setVenderId(pvmRelationPO.getVenderId());
                        selMRPo.setMakerId(pvmRelationPO.getMakerId());

                        updMRPo.setDfVender(pvmRelationPO.getDfVender());
                        updMRPo.setDfMaker(pvmRelationPO.getDfMaker());
                        updMRPo.setState(Constant.STATUS_ENABLE);
                        updMRPo.setUpdateBy(logonUser.getUserId());
                        updMRPo.setUpdateDate(date);

                        dao.update(selMRPo, updMRPo);

                    } else {// 否则新增
                        TtPartVenderMakerRelationPO insMRPo = new TtPartVenderMakerRelationPO();

                        insMRPo.setPartId(pvmRelationPO.getPartId());
                        insMRPo.setVenderId(pvmRelationPO.getVenderId());
                        insMRPo.setMakerId(pvmRelationPO.getMakerId());
                        insMRPo.setDfVender(pvmRelationPO.getDfVender());
                        insMRPo.setDfMaker(pvmRelationPO.getDfMaker());
                        insMRPo.setCreateBy(logonUser.getUserId());
                        insMRPo.setCreateDate(date);
                        insMRPo.setState(Constant.STATUS_ENABLE);
                        insMRPo.setStatus(1);

                        dao.insert(insMRPo);
                    }
                }*/
            act.setForword(PART_BUYPRICE_QUERY_URL);
            //} //end

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BUYPRICE_QUERY_URL);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :
     * @Description: 循环获取cell
     */
    private void loadVoList(List voList, List mkList, List pvmList, List<Map> list, StringBuffer errorInfo)
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
                parseCells(voList, mkList, pvmList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }

    }

    /**
     * 装载VO
     *
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @return :
     * @throws Exception
     * @throws :         LastDate : 2013-4-7
     * @Title :
     * @Description: TODO
     */
    private void parseCells(List list, List mkList, List pvmList, String rowNum, Cell[] cells,
                            StringBuffer errorInfo) throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        TtPartBuyPricePO ttPartBuyPricePO = new TtPartBuyPricePO();
        TtPartMakerRelationPO makerRelationPO = new TtPartMakerRelationPO();

        String part_Oldcode = "";//配件编码
        String vender_Code = "";//供应商编码
        String vender_Cname = "";//供应商名称
        String maker_Code = "";//制造商编码
        String maker_Cname = "";//制造商名称
        String buy_Price = "";//配件采购价格
        String is_guard = "";//是否暂估价
        String min_PKG = "";//最小包装量
        String default_vender = "";//是否默认供应商
        String default_maker = "";//是否默认制造商
        
        for(int k = 0; k < cells.length; k ++)
        {
        	switch(k)
        	{
	        	case 0:
	        		part_Oldcode = subCell(cells[0].getContents().trim()).toUpperCase();//配件编码
	        		break;
	        	case 1:
	        		vender_Code = subCell(cells[1].getContents().trim());//供应商编码
	        		break;
	        	case 2:
	        		vender_Cname = subCell(cells[2].getContents().trim());//供应商名称
	        		break;
	        	case 3:
	        		maker_Code = subCell(cells[3].getContents().trim());//制造商编码
	        		break;
	        	case 4:
	        		maker_Cname = subCell(cells[4].getContents().trim());//制造商名称
	        		break;
	        	case 5:
	        		buy_Price = subCell(cells[5].getContents().trim()) == "" ? "0" : subCell(cells[5].getContents().trim());//配件采购价格
	        		break;
//	        	case 6:
//	        		min_PKG = subCell(cells[6].getContents().trim());//最小包装量
//	        		break;
	        	case 6:
	        		default_vender = subCell(cells[6].getContents().trim());//是否默认供应商
	        		break;
	        	case 7:
	        		default_maker = subCell(cells[7].getContents().trim());//是否默认制造商
                    break;
	        	default:
                    break;
        	}
        	
        	
        }

        if ("" == part_Oldcode) {
            errorInfo.append("第" + rowNum + "行的配件编码不能为空,请修改后再上传!");
            return;
        }
        Map<String, Object> map = null;
        map = dao.validatePartOldcode(part_Oldcode);
        if (map == null) {
            errorInfo.append("第" + rowNum + "行的配件编码不存在,请修改后再上传!");
            return;
        }

        Long partId = ((BigDecimal) map.get("PART_ID")).longValue();
        ttPartBuyPricePO.setPartId(partId);
        makerRelationPO.setPartId(partId);

        Long venderId = null;
        //供应商验证
        if (vender_Cname != "" && vender_Code != "") {
            map = dao.validatePartVenderName(vender_Cname);
            if (map == null) {
                //新增Vender
                TtPartVenderDefinePO insVDPo = new TtPartVenderDefinePO();

                insVDPo.setVenderId(Long.parseLong(SequenceManager.getSequence("")));
                insVDPo.setVenderCode(vender_Code);
                insVDPo.setVenderName(vender_Cname);
                insVDPo.setVenderType(Constant.PARTVENDER_INNER);
                insVDPo.setIsAbroad(Constant.PARTVENDER_INNER);
                insVDPo.setCreateBy(Long.parseLong((-1) + ""));
                insVDPo.setCreateDate(new Date());

                dao.insert(insVDPo);

                map = dao.validatePartVenderName(vender_Cname);
                venderId = ((BigDecimal) map.get("VENDER_ID")).longValue();
            } else {
                venderId = ((BigDecimal) map.get("VENDER_ID")).longValue();
            }

            ttPartBuyPricePO.setVenderId(venderId);
        }

        //制造商验证
        Long makerId = null;
        if (maker_Cname != "" & maker_Code != "") {
            map = dao.validatePartMakerName(maker_Cname);
            if (map == null) {
                //新增Maker
                TtPartMakerDefinePO insMDPo = new TtPartMakerDefinePO();

                insMDPo.setMakerId(Long.parseLong(SequenceManager.getSequence("")));
                insMDPo.setMakerCode(maker_Code);
                insMDPo.setMakerName(maker_Cname);
                insMDPo.setMakerType(Constant.PARTMAKER_INNER);
                insMDPo.setIsAbroad(Constant.PARTMAKER_NO);
                insMDPo.setVenderId(venderId);
                insMDPo.setCreateBy(Long.parseLong((-1) + ""));
                insMDPo.setCreateDate(new Date());

                dao.insert(insMDPo);

                map = dao.validatePartMakerName(maker_Cname);
                makerId = ((BigDecimal) map.get("MAKER_ID")).longValue();
            } else {
                makerId = ((BigDecimal) map.get("MAKER_ID")).longValue();
            }
            makerRelationPO.setMakerId(makerId);
        }

        if ("" == buy_Price) {
            errorInfo.append("第" + rowNum + "行的采购价不能为空,请修改后再上传!");
            return;
        }
        try {
            ttPartBuyPricePO.setBuyPrice(Double.valueOf(buy_Price));
            makerRelationPO.setClaimPrice(Double.valueOf(buy_Price));
        } catch (Exception e) {
            errorInfo.append("第" + rowNum + "行的采购价格数据类型错误,请修改后再上传!");
            return;
        }

//        if ("" == min_PKG) {
//            ttPartBuyPricePO.setMinPackage(Long.parseLong(1 + "")); //默认最小包装量 1
//        } else {
//            String accTemp = min_PKG;
//            String regex = "(^[1-9]+\\d*$)";
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(accTemp);
//
//            if (matcher.find()) {
//                ttPartBuyPricePO.setMinPackage(Long.parseLong(accTemp));
//            } else {
//                errorInfo.append("第" + rowNum + "行的最小包装量数据类型错误,请修改后再上传!");
//                return;
//            }
//        }

        //是否默认供应商
        if ("1".equals(default_vender)) {
            ttPartBuyPricePO.setIsDefault(Constant.PART_BASE_FLAG_YES);
        } else {
            ttPartBuyPricePO.setIsDefault(Constant.PART_BASE_FLAG_NO);
        }
        //是否默认制造商
        if ("1".equals(default_maker)) {
            makerRelationPO.setIsDefault(Constant.PART_BASE_FLAG_YES);
        } else {
            makerRelationPO.setIsDefault(Constant.PART_BASE_FLAG_NO);
        }

        Date date = new Date();
        //采购价格更新
        if (ttPartBuyPricePO.getPartId() != null && ttPartBuyPricePO.getVenderId() != null) {
        	if (typeYes == ttPartBuyPricePO.getIsDefault()) {
                //取消原来默认
                TtPartBuyPricePO selBPPo = new TtPartBuyPricePO();
                TtPartBuyPricePO updBPPo = new TtPartBuyPricePO();

                selBPPo.setPartId(ttPartBuyPricePO.getPartId());

                updBPPo.setIsDefault(typeNo);
                updBPPo.setUpdateBy(logonUser.getUserId());
                updBPPo.setUpdateDate(date);

                dao.update(selBPPo, updBPPo);
            }
            Map<String, Object> map1 = dao.checkPartIdAndVenderId(ttPartBuyPricePO.getPartId(), ttPartBuyPricePO.getVenderId());
            // 如果已经存在该配件与供应商对应的价格，则更新
            if (map1 != null) {
                dao.updateBuyPrice(ttPartBuyPricePO.getPartId(),
                        ttPartBuyPricePO.getVenderId(),
                        ttPartBuyPricePO.getBuyPrice(),
                        ttPartBuyPricePO.getIsDefault(), logonUser);

                //更新的时候需要把数据保存到采购价格历史表中
                TtPartBuyPriceHistoryPO ttPartBuyPriceHistoryPO = new TtPartBuyPriceHistoryPO();
                ttPartBuyPriceHistoryPO.setHistoryId(Long.parseLong(SequenceManager.getSequence("")));
                ttPartBuyPriceHistoryPO.setPriceId(((BigDecimal) map1.get("PRICE_ID")).longValue());
                ttPartBuyPriceHistoryPO.setVenderId(ttPartBuyPricePO.getVenderId());
                ttPartBuyPriceHistoryPO.setBuyPrice(ttPartBuyPricePO.getBuyPrice());
                ttPartBuyPriceHistoryPO.setPlanPrice(Double.parseDouble(map1.get("PLAN_PRICE").toString()));
                ttPartBuyPriceHistoryPO.setIsGuard(Constant.IS_GUARD_NO);
                ttPartBuyPriceHistoryPO.setCreateDate(date);
                ttPartBuyPriceHistoryPO.setCreateBy(logonUser.getUserId());
                ttPartBuyPriceHistoryPO.setState(ttPartBuyPricePO.getState());
                ttPartBuyPriceHistoryPO.setStatus(1);
                ttPartBuyPriceHistoryPO.setPartId(ttPartBuyPricePO.getPartId());
                dao.insert(ttPartBuyPriceHistoryPO);
            } else {// 否则新增
                Long priceId = Long.parseLong(SequenceManager.getSequence(""));
                ttPartBuyPricePO.setPriceId(priceId);
                ttPartBuyPricePO.setIsGuard(Constant.IS_GUARD_NO);
                ttPartBuyPricePO.setCreateDate(date);
                ttPartBuyPricePO.setCreateBy(logonUser.getUserId());
                ttPartBuyPricePO.setState(Constant.STATUS_ENABLE);
                ttPartBuyPricePO.setStatus(1);

                dao.insert(ttPartBuyPricePO);
            }
        }
        //配件与制造商关系更新
        if (makerRelationPO.getMakerId() != null && makerRelationPO.getPartId() != null) {
        	//取消原来默认
            if (typeYes == makerRelationPO.getIsDefault()) {
                TtPartMakerRelationPO selMRPo = new TtPartMakerRelationPO();
                TtPartMakerRelationPO updMRPo = new TtPartMakerRelationPO();

                selMRPo.setPartId(makerRelationPO.getPartId());

                updMRPo.setIsDefault(typeNo);
                updMRPo.setUpdateBy(logonUser.getUserId());
                updMRPo.setUpdateDate(date);

                dao.update(selMRPo, updMRPo);
            }
        	
            Map<String, Object> mapMaker = dao.checkPartIdAndMakerId(
                    makerRelationPO.getPartId(),
                    makerRelationPO.getMakerId());
            // 如果已经存在该配件与制造商关系，则更新
            if (mapMaker != null) {
                String relaionId = mapMaker.get("RELAION_ID").toString();

                TtPartMakerRelationPO selPo = new TtPartMakerRelationPO();
                TtPartMakerRelationPO updPo = new TtPartMakerRelationPO();

                selPo.setRelaionId(Long.parseLong(relaionId));
                selPo.setPartId(makerRelationPO.getPartId());
                selPo.setMakerId(makerRelationPO.getMakerId());

                updPo.setIsDefault(makerRelationPO.getIsDefault());
                updPo.setClaimPrice(makerRelationPO.getClaimPrice());
                updPo.setUpdateBy(logonUser.getUserId());
                updPo.setUpdateDate(date);

                dao.update(selPo, updPo);

            } else {// 否则新增
                TtPartMakerRelationPO insMRPo = new TtPartMakerRelationPO();

                insMRPo.setRelaionId(Long.parseLong(SequenceManager.getSequence("")));
                insMRPo.setPartId(makerRelationPO.getPartId());
                insMRPo.setMakerId(makerRelationPO.getMakerId());
                insMRPo.setIsDefault(makerRelationPO.getIsDefault());
                insMRPo.setClaimPrice(makerRelationPO.getClaimPrice());
                insMRPo.setCreateBy(logonUser.getUserId());
                insMRPo.setCreateDate(date);
                insMRPo.setState(Constant.STATUS_ENABLE);
                insMRPo.setStatus(1);

                dao.insert(insMRPo);
            }
        }
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
}
