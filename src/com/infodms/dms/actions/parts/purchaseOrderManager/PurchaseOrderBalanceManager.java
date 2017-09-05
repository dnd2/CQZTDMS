package com.infodms.dms.actions.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartBuyPriceDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderBalanceDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderChkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-4-17
 * @ClassName : PurchaseOrderBalanceManager
 * @Description : 采购订单结算
 */
public class PurchaseOrderBalanceManager implements PTConstants {

    public Logger logger = Logger.getLogger(PurchaseOrderBalanceManager.class);
    private PurchaseOrderBalanceDao dao = PurchaseOrderBalanceDao.getInstance();
    private static final Integer PRINT_SIZE = 40;

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list,String name)
            throws Exception {

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
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 查询初始化, 转到需要结算的入库单页面
     */
    public void purchaseOrderInStockInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String balCode = CommonUtils.checkNull(request.getParamValue("balCode")); //结算单号
            String state = CommonUtils.checkNull(request.getParamValue("state")); //结算状态
            if(state.equals(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06.toString())){
            	TtPartPoBalancePO po = new TtPartPoBalancePO();
            	po.setBalanceCode(balCode);
            	po = (TtPartPoBalancePO) dao.select(po).get(0);
            	act.setOutData("remark1", po.getRemark1());
            	act.setOutData("rejectFlag", 1);
            }
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            act.setOutData("wareHouses", list);
            act.setOutData("BALANCE_CODE1", balCode);
            act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL2);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 修改初始化
     */
    public void purOrderBalUpdateInit() {
    	
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
    	try {
    		String balCode = CommonUtils.checkNull(request.getParamValue("balCode")); //结算单号
    		String invoNo = CommonUtils.checkNull(request.getParamValue("invoNo")); //发票号
    		List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
    		
    		
    		List<Map<String, Object>> detailList = dao.getBalInfo4Update(balCode);
    		
    		Map<String, Object> map = detailList.get(0);
    		
    		act.setOutData("balanceCode", balCode);
    		act.setOutData("invoNo", invoNo);
    		act.setOutData("balanceDate", map.get("BALANCE_DATE"));
    		act.setOutData("createDate", map.get("CREATE_DATE"));
    		if(map.get("BALANCE_TYPE")!=null){
    			act.setOutData("balanceType", ((BigDecimal)map.get("BALANCE_TYPE")).intValue());
    		}else{
    			act.setOutData("balanceType", 0);
    		}
    		act.setOutData("balancer", map.get("NAME"));
    		act.setOutData("curUserId", logonUser.getUserId());
			act.setOutData("planerList", planerList);
			act.setOutData("detailList", detailList);
    		act.setForword(PART_PURCHASEORDERBALANCE_UPDATE_URL);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    }

    /**
     * 
     * @Title      : 
     * @Description: 修改结算单 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-10-12
     */
    public void updateBalanceOrder(){
    	 ActionContext act = ActionContext.getContext();
         RequestWrapper request = act.getRequest();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
         try {
             String[] inIds = request.getParamValues("cb");//获取采购入库id

             String errors = "";//错误信息
             String success = "";//成功信息
             
             //结算单号
             String balanceCode = CommonUtils.checkNull(request.getParamValue("balanceCode"));
             String invoNo = CommonUtils.checkNull(request.getParamValue("invoNo"));//发票号
             String createDate = CommonUtils.checkNull(request.getParamValue("createDate"));//结算日期
             String balanceType = CommonUtils.checkNull(request.getParamValue("PART_BALANCE_TYPE"));//结算类型
             
             TtPartPoBalancePO balancePO1 = new TtPartPoBalancePO();
        	 balancePO1.setBalanceCode(balanceCode);
        	 List<TtPartPoBalancePO> baList = dao.select(balancePO1);
             
             for (int i = 0; i < baList.size(); i++) {
            	 TtPartPoBalancePO balPO = baList.get(i);
            	 long inId =balPO.getInId();
            	 //更新对应入库明细的状态和已结算数量、待结算数量
                 dao.updatePOIn(inId,balPO.getBalQty());
             }
             
             //删除该结算单下的结算明细
             dao.updateBalanceOrder(balanceCode);
             
             for (int i = 0; i < inIds.length; i++) {
                 long inId = CommonUtils.parseLong(inIds[i]);

                 Long balQty = CommonUtils.parseLong(request.getParamValue("BAL_QTY1"+inId));//结算数量
                 
             	//查询入库单信息
                 TtPartPoInPO po = new TtPartPoInPO();
                 po.setInId(inId);
                 
                 po = (TtPartPoInPO) dao.select(po).get(0);

                 //验证结算数量是否大于待结算数量
                 if(balQty>(po.getInQty()-po.getReturnQty()-po.getBalQty())){
                 	errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】的结算数量大于待结算数量("+(po.getInQty()-po.getReturnQty()-po.getBalQty())+")!";
        		    POContext.endTxn(false);//回滚事务
                    POContext.cleanTxn();
        		    break;
                 }
                 
                 //预更新入库明细,如果还有剩余数量没有结算,那么该入库明细就还可以结算,不改变其状态
                 TtPartPoInPO updatepo = new TtPartPoInPO();
                 updatepo.setInId(po.getInId());
                 updatepo.setBalQty(po.getBalQty()+balQty);
                 updatepo.setSpareQty(po.getInQty()-po.getReturnQty()-updatepo.getBalQty());
                 updatepo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//更新入库单状态为结算中

                 //获取当前版本信息
                 int ver = po.getVer();

                 TtPartPoBalancePO balancePO = new TtPartPoBalancePO();//将每次结算信息保存到采购结算明细表中
                 balancePO.setBalanceId((CommonUtils.parseLong(SequenceManager.getSequence(""))));
                 balancePO.setBalanceCode(balanceCode);//结算单号
                 if(!"null".equals(invoNo)){
                	 balancePO.setInvoNo(invoNo);
                 }
                 if(!"".equals(balanceType)){
                	 balancePO.setBalanceType(CommonUtils.parseInteger(balanceType));;
                 }
                 balancePO.setInId(po.getInId());
                 balancePO.setInCode(po.getInCode());
                 balancePO.setInType(po.getInType());
                 balancePO.setCheckId(po.getCheckId());//验收单ID
                 balancePO.setCheckCode(po.getCheckCode());//验收单号
                 balancePO.setPoId(po.getPoId());
                 balancePO.setOrderCode(po.getOrderCode());
                 balancePO.setPlanCode(po.getPlanCode());
                 balancePO.setBuyerId(po.getBuyerId());
                 balancePO.setPartType(po.getPartType());
                 balancePO.setPartId(po.getPartId());
                 balancePO.setPartCode(po.getPartCode());
                 balancePO.setPartOldcode(po.getPartOldcode());
                 balancePO.setPartCname(po.getPartCname());
                 balancePO.setUnit(po.getUnit());
                 balancePO.setVenderId(po.getVenderId());
                 balancePO.setVenderCode(po.getVenderCode());
                 balancePO.setVenderName(po.getVenderName());
                 balancePO.setBuyPrice(po.getBuyPrice());
                 balancePO.setBuyPriceNotax(po.getBuyPriceNotax());
                 balancePO.setInAmount(po.getInAmount());
                 balancePO.setInAmountNotax(po.getInAmountNotax());
                 
                 //获取采购单价(入库单中的单价是计划单价)
                 TtPartBuyPricePO selbuyPricePO = new TtPartBuyPricePO();
                 selbuyPricePO.setPartId(po.getPartId());
                 selbuyPricePO.setVenderId(po.getVenderId());
                 selbuyPricePO.setState(Constant.STATUS_ENABLE);
                 selbuyPricePO.setStatus(1);
                 if (dao.select(selbuyPricePO).size() > 1) {
                     errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】对应的采购单价不唯一，请确认!";
                     POContext.endTxn(false);//回滚事务
                     POContext.cleanTxn();
                     break;
                 } else if (dao.select(selbuyPricePO).size() == 0) {//如果当前供应商下的配件对应的采购价格已经无效
                     errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】在当前供应商下对应的采购单价不存在或已失效，请设置!";
                     POContext.endTxn(false);//回滚事务
                     POContext.cleanTxn();
                     break;
                 } else {
                     TtPartBuyPricePO buyPricePO = (TtPartBuyPricePO) dao.select(selbuyPricePO).get(0);
                     balancePO.setBuyPrice1(buyPricePO.getBuyPrice());//含税采购单价
                     balancePO.setBuyQty(po.getBuyQty());
                     balancePO.setCheckQty(po.getCheckQty());
                     balancePO.setInQty(po.getInQty());//入库数量
                     balancePO.setReturnQty(po.getReturnQty());//退货数量
                     balancePO.setBalQty(balQty);//结算数量
                     balancePO.setBalAmount(Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1()), 2));//含税金额(结算金额)
                     balancePO.setTaxRate(Constant.PART_TAX_RATE);//税率
                     balancePO.setBuyPrice1Notax(Arith.mul(balancePO.getBuyPrice1(), (1 - Constant.PART_TAX_RATE)));//无税采购单价=含税单价*（1-税率）
                     balancePO.setBalAmountNotax(Arith.round(Arith.mul(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1()), (1 - Constant.PART_TAX_RATE)), 2));//无税结算金额=含税总金额*(1-税率)
                     //end modify
                     balancePO.setWhId(po.getWhId());
                     balancePO.setWhName(po.getWhName());
                     balancePO.setBatchNo(po.getBatchNo());
                     balancePO.setLocId(po.getLocId());
                     balancePO.setLocCode(po.getLocCode());
                     balancePO.setBuyGroup(po.getBuyGroup());
                     balancePO.setFcvderId(po.getFcvderId());
                     balancePO.setFcvderrCode(po.getFcvderrCode());
                     balancePO.setFtvenderId(po.getFtvenderId());
                     balancePO.setIsGauge(po.getIsGauge());
                     balancePO.setRemark(CommonUtils.checkNull(request.getParamValue("REMARK"+inId)));
                     balancePO.setInDate(po.getInDate());
                     balancePO.setInBy(po.getInBy());
                     balancePO.setCheckDate(po.getCheckDate());
                     balancePO.setCheckBy(po.getCheckBy());
                     balancePO.setIsCheck(po.getIsCheck());
                     balancePO.setCreateDate(CommonUtils.parseDateTime(createDate));
                     balancePO.setCreateBy(logonUser.getUserId());
                     balancePO.setOrgId(logonUser.getOrgId());
                     balancePO.setBalanceDate(CommonUtils.parseDateTime(createDate));
                     balancePO.setBalanceBy(logonUser.getUserId());
                     balancePO.setIsBalances(Constant.IF_TYPE_NO);//生成结算明细的时候状态为未结算,与发票号关联之后状态变为已结算
                     balancePO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//结算中
                     balancePO.setMakerId(po.getMakerId());
                     balancePO.setOriginType(po.getOriginType());

                     dao.update(updatepo);//更新采购订单入库表,此时将会锁定该记录,直到事务提交才释放
                     updatepo.setVer(ver + 1);//让版本号加1

                     //提交数据之前需要比较开始读取的版本号(ver)与此时读取的版本号(ver1),如果发现ver1<=ver就结算失败,并回滚数据
                     Map map = dao.getVerByInId(po.getInId());//通过入库id获取入库表版本号
                     int ver1 = ((BigDecimal) map.get("VER")).intValue();
                     if (updatepo.getVer() > ver1) {
                         dao.insert(balancePO);//新增订单结算信息
                         dao.updateVer(updatepo);//把当前最新版本号更新到数据库
                     } else {
                         errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】正在结算,请稍后再试!<br>";
                         POContext.endTxn(false);//回滚事务
                         POContext.cleanTxn();
                         break;
                     }

                 }
                 
             }
             if("".equals(errors)){
             	success = "操作成功!";
             }
             act.setOutData("success", success);
             act.setOutData("error", errors);
         } catch (Exception e) {//异常方法
             BizException e1 = new BizException(act, e,
                     ErrorCodeConstant.SPECIAL_MEG, "采购订单结算修改失败,请联系管理员!");
             logger.error(logonUser, e1);
             act.setException(e1);
         }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 查询初始化, 转到需要结算的入库单页面
     */
    public void purchaseOrderBalanceQueryInit() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
        try {
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);
            act.setOutData("wareHouses", list);
            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setOutData("now",CommonUtils.getDate());
            act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 入库单结算管理
     */
    public void queryOrderBalanceMng() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderBalanceMngList(curPage, Constant.PAGE_SIZE_MIDDLE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 提交结算单到财务
     */
    public void submitBalanceOrder() {
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
    		//结算单号
            String balanceCode = CommonUtils.checkNull(request.getParamValue("balCode"));
            Long userId=logonUser.getUserId();
            dao.updateState2(balanceCode,userId);//把入库单的状态和结算单的状态修改为已提交
            
            act.setOutData("success", "提交成功!");
            act.setOutData("curPage", curPage);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "结算单提交失败,请联系管理员!");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    	
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 作废
     */
    public void delBalanceOrder() {
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		String curPage = CommonUtils.checkNull(request
    				.getParamValue("curPage"));// 当前页
    		if ("".equals(curPage)) {
    			curPage = "1";
    		}
    		//结算单号
    		String balanceCode = CommonUtils.checkNull(request.getParamValue("balCode"));
    		
    		TtPartPoBalancePO balancePO1 = new TtPartPoBalancePO();
	       	balancePO1.setBalanceCode(balanceCode);
	       	List<TtPartPoBalancePO> baList = dao.select(balancePO1);
            
            for (int i = 0; i < baList.size(); i++) {
	           	TtPartPoBalancePO balPO = baList.get(i);
	           	long inId =balPO.getInId();
           	    //更新对应入库明细的状态为待结算、更新已结算数量、待结算数量
                dao.updatePOIn(inId,balPO.getBalQty());
            }
    		
    		dao.updateState3(balanceCode);//把结算单的状态修改为已作废
    		
    		act.setOutData("success", "作废成功!");
    		act.setOutData("curPage", curPage);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "结算单作废失败,请联系管理员!");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    	
    	
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 入库单结算信息查询
     */
    public void queryOrderBalanceInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
           
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderBalanceList(request,curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("wareHouses", list);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 入库单信息查询
     */
    public void queryPurOrderInInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            String page = CommonUtils.checkNull(request.getParamValue("page"));//每页行数
            int pageSize = 50;
            if (!(page == null || page.equals(""))) {
                pageSize = Integer.parseInt(page);
            }
            PageResult<Map<String, Object>> ps = dao.queryPurOrderInList(request, curPage, pageSize, logonUser);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }


    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-17
     * @Title :
     * @Description: 获取当前配件对应的供应商
     */
    public void queryVenderInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("PART_ID"));
            String inId = CommonUtils.checkNull(request.getParamValue("inId"));
            String curVenderId = CommonUtils.checkNull(request.getParamValue("curVenderId"));
            List venders = dao.queryVenderInfoByPartId(CommonUtils.parseLong(partId));
            act.setOutData("venders", venders);
            act.setOutData("inId", inId);
            act.setOutData("curVenderId", curVenderId);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 通过供应商id来修改该入库单中配件的采购价格
     */
    @SuppressWarnings("unchecked")
    public void updateBuyPrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage) || curPage == "undefined") {
                curPage = "1";
            }
            String str_inId = CommonUtils.checkNull(request.getParamValue("IN_ID"));//入库ID
            String str_venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String str_partId = CommonUtils.checkNull(request.getParamValue("PART_ID"));//配件id
            if (!"".equals(str_inId) && !"".equals(str_venderId) && !"".equals(str_partId)) {
                //通过供应商id、配件id查询其对应的价格
                Map map = dao.queryNewPrice(CommonUtils.parseLong(str_venderId), CommonUtils.parseLong(str_partId));

                TtPartPoInPO spo = new TtPartPoInPO();
                spo.setInId(CommonUtils.parseLong(str_inId));
                TtPartPoInPO po = new TtPartPoInPO();
                po.setInId(CommonUtils.parseLong(str_inId));
                po = (TtPartPoInPO) dao.select(po).get(0);

                TtPartPoInPO inPO = new TtPartPoInPO();
                inPO.setBuyPrice(((BigDecimal) map.get("BUY_PRICE")).doubleValue());
                inPO.setVenderId(((BigDecimal) map.get("VENDER_ID")).longValue());
                inPO.setVenderCode((String) map.get("VENDER_CODE"));
                inPO.setVenderName((String) map.get("VENDER_NAME"));
                inPO.setInAmount(Arith.round((Arith.mul(po.getBuyPrice(), po.getInQty())), 2));
                inPO.setBuyPriceNotax(Arith.round(Arith.mul(po.getBuyPrice(), Arith.sub(1, Constant.PART_TAX_RATE)), 2));
                inPO.setInAmountNotax(Arith.round((Arith.mul(po.getBuyPriceNotax(), po.getInQty())), 2));
                inPO.setUpdateDate(new Date());
                inPO.setUpdateBy(logonUser.getUserId());
                inPO.setMakerId(0l);//修改供应商的时候直接将制造商id设为0

                //修改入库表中的采购价格
                dao.update(spo, inPO);
            }
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "采购结算供应商");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 订单结算
     */
    @SuppressWarnings("unchecked")
    public void balanceOrder() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String errors = "";//错误信息
        try {


            String success = "";//成功信息

            Integer balanceType = 92771001;//CommonUtils.parseInteger(request.getParamValue("PART_BALANCE_TYPE"));//结算类型
            Map<Object, Object> checkMap = new HashMap<Object, Object>();//校验Map
            String balanceCode = "";//验收单
            Long batNo = CommonUtils.parseLong(SequenceManager.getSequence(""));
            int tz = 0;
            String all = CommonUtils.checkNull(request.getParamValue("all"));
            if (!all.equals("") && all != null && all.equals("all")) {
                PageResult<Map<String, Object>> ps = dao.queryPurOrderInList(request, 1, Constant.PAGE_SIZE_MAX, logonUser);
                for (int i = 0; i < ps.getTotalRecords(); i++) {
                    String temp = ((BigDecimal) ps.getRecords().get(i).get("IN_ID")).toString();
                    long inId = CommonUtils.parseLong(temp);
                    Long balQty = CommonUtils.parseLong(((BigDecimal) ps.getRecords().get(i).get("IN_QTY")).toString());//结算数量

                    //判断该当前入库明细是否结算
                    TtPartPoInPO poInPO = new TtPartPoInPO();
                    poInPO.setInId(inId);
                    poInPO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);

                    if (dao.select(poInPO).size() > 0) {
                        poInPO = ((TtPartPoInPO) dao.select(poInPO).get(0));
                        errors = "入库单【" + poInPO.getInCode() + "】中的配件【" + poInPO.getPartOldcode() + "】已经结算!";
                     /*   POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    TtPartVenderDefinePO vpo = new TtPartVenderDefinePO();
                    vpo.setVenderId(Long.parseLong(((BigDecimal) ps.getRecords().get(i).get("VENDER_ID1")).toString()));
                    vpo = (TtPartVenderDefinePO) dao.select(vpo).get(0);
                    if (vpo.getIsSuspend().equals("10041001")) {
                        errors = "供应商" + vpo.getVenderName() + "已经暂停业务，无法进行结算！";
                       /* POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    //查询入库单信息
                    TtPartPoInPO po = new TtPartPoInPO();
                    po.setInId(inId);

                    po = (TtPartPoInPO) dao.select(po).get(0);
                    TtPartDefinePO partPo = new TtPartDefinePO();
                    partPo.setPartId(po.getPartId());
                    partPo = (TtPartDefinePO) dao.select(partPo).get(0);

                    //如果采购员,财务供应商,采购组织不相同则从新生成结算单
                    //结算单号
                    balanceCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_05);
                    //验证结算数量是否大于待结算数量
                    if (po.getInQty() > 0) {
                        if (balQty > (po.getInQty() - po.getReturnQty() - po.getBalQty())) {
                            errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】的结算数量大于待结算数量(" + (po.getInQty() - po.getReturnQty() - po.getBalQty()) + ")!";
                           /* POContext.endTxn(false);//回滚事务
                             POContext.cleanTxn();
                            break;*/
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                            throw e1;
                        }
                    }

                    if (po.getInQty() < 0) {
                        if (Math.abs(balQty) > Math.abs(po.getInQty() + po.getReturnQty() - po.getBalQty())) {
                            errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】的结算数量大于待结算数量(" + (po.getInQty() - po.getReturnQty() - po.getBalQty()) + ")!";
                            /* POContext.endTxn(false);//回滚事务
                            POContext.cleanTxn();
                             break;*/
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                            throw e1;
                        }
                    }
                    //预更新入库明细,如果还有剩余数量没有结算,那么该入库明细就还可以结算,不改变其状态
                    TtPartPoInPO updatepo = new TtPartPoInPO();
                    updatepo.setInId(po.getInId());
                    updatepo.setBalQty(po.getBalQty() + balQty);
                    updatepo.setSpareQty(po.getInQty() - po.getReturnQty() - updatepo.getBalQty());
                    updatepo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//更新入库单状态为结算中

                    //获取当前版本信息
                    int ver = po.getVer();

                    TtPartPoBalancePO balancePO = new TtPartPoBalancePO();//将每次结算信息保存到采购结算明细表中
                    balancePO.setBalanceId((CommonUtils.parseLong(SequenceManager.getSequence(""))));
                    balancePO.setBalanceCode(balanceCode);//结算单号
                    balancePO.setInId(po.getInId());
                    balancePO.setProduceFac(Long.valueOf(po.getProduceFac()));
                    balancePO.setProduceState(po.getProduceState());
                    balancePO.setSuperiorPurchasing(po.getSuperiorPurchasing());
                    balancePO.setInCode(po.getInCode());
                    balancePO.setInType(po.getInType());
                    balancePO.setCheckId(po.getCheckId());//验收单ID
                    balancePO.setCheckCode(po.getCheckCode());//验收单号
                    balancePO.setPoId(po.getPoId());
                    balancePO.setOrderCode(po.getOrderCode());
                    balancePO.setPlanCode(po.getPlanCode());
                    balancePO.setBuyerId(po.getBuyerId());
                    balancePO.setPartType(po.getPartType());
                    balancePO.setPartId(po.getPartId());
                    balancePO.setPartCode(po.getPartCode());
                    balancePO.setPartOldcode(po.getPartOldcode());
                    balancePO.setPartCname(po.getPartCname());
                    balancePO.setUnit(po.getUnit());
                    balancePO.setVenderId(po.getVenderId());
                    balancePO.setVenderCode(po.getVenderCode());
                    balancePO.setVenderName(po.getVenderName());
                    balancePO.setBuyPrice(po.getBuyPrice());
                    balancePO.setBuyPriceNotax(po.getBuyPriceNotax());
                    balancePO.setInAmount(po.getInAmount());
                    balancePO.setInAmountNotax(po.getInAmountNotax());
                    List<Map<String, Object>> priceList = new ArrayList();
                    Double price = dao.getBuyPrice(po.getPartId(), po.getFcvderId(), DateUtil.format(po.getInDate(), "yyyy-MM-dd"), Long.valueOf(po.getSuperiorPurchasing()));
                    if (price == 0D) {
                        errors = "配件" + po.getPartCode() + "与财务供应商没有签订合同或没有采购价格<br>";
                       /* POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    balancePO.setBuyPrice1(price);//入库确认已经确认过财务供应商，不在需要重新获取供应商价格
                    balancePO.setBuyQty(po.getBuyQty());
                    balancePO.setCheckQty(po.getCheckQty());
                    balancePO.setInQty(po.getInQty());//入库数量
                    balancePO.setReturnQty(po.getReturnQty());//退货数量
                    balancePO.setBalQty(balQty);//结算数量
                    balancePO.setBalAmount(Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1()), 2));//含税金额(结算金额)
                    balancePO.setTaxRate(Constant.PART_TAX_RATE);//税率
                    balancePO.setBuyPrice1Notax(Arith.round(Arith.div(balancePO.getBuyPrice1(), Arith.add(1, Constant.PART_TAX_RATE)), 6));//无税采购单价=含税单价/（1+税率）
                    balancePO.setBalAmountNotax(Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1Notax()), 2));//无税结算金额=无税采购单价*结算数量
                    //end modify
                    balancePO.setWhId(po.getWhId());
                    balancePO.setWhName(po.getWhName());
                    balancePO.setBatchNo(po.getBatchNo());
                    balancePO.setLocId(po.getLocId());
                    balancePO.setLocCode(po.getLocCode());
                    balancePO.setBuyGroup(Long.valueOf(po.getProduceFac()));
                    balancePO.setFcvderId(po.getFcvderId());
                    balancePO.setFcvderrCode(po.getFcvderrCode());
                    balancePO.setFtvenderId(po.getFtvenderId());
                    balancePO.setIsGauge(po.getIsGauge());
                    balancePO.setInDate(po.getInDate());
                    balancePO.setInBy(po.getInBy());
                    balancePO.setCheckDate(po.getCheckDate());
                    balancePO.setCheckBy(po.getCheckBy());
                    balancePO.setIsCheck(po.getIsCheck());
                    balancePO.setCreateDate(new Date());
                    balancePO.setCreateBy(logonUser.getUserId());
                    balancePO.setOrgId(logonUser.getOrgId());
                    balancePO.setBalanceDate(new Date());
                    balancePO.setBalanceBy(logonUser.getUserId());
                    balancePO.setIsBalances(Constant.IF_TYPE_NO);//生成结算明细的时候状态为未结算,与发票号关联之后状态变为已结算
                    balancePO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//结算中
                    balancePO.setMakerId(po.getMakerId());
                    balancePO.setOriginType(po.getOriginType());
                    balancePO.setBalanceType(balanceType);
                    balancePO.setBatId(batNo);
                    dao.update(updatepo);//更新采购订单入库表,此时将会锁定该记录,直到事务提交才释放
                    int isGauge = po.getIsGauge();
                    String check = CommonDAO.getPara("60161001");
                    DecimalFormat df = new DecimalFormat("#.000000");

                    if ((Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1Notax()), 2) - Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPriceNotax()), 2)) != 0 && !check.equals("10041002")) {
                        tz += 1;
                        TtPartStockChgDtlPO dtlPo = new TtPartStockChgDtlPO();//成本调整副表
                        dtlPo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
                        //dtlPo.setChgId(changeId);
                        dtlPo.setPartId(po.getPartId());
                        dtlPo.setPartCname(po.getPartCname());
                        dtlPo.setPartCode(po.getPartCode());
//                        dtlPo.setBatId(batNo);
                        dtlPo.setPartOldcode(po.getPartOldcode());
                        dtlPo.setInpartId(po.getPartId());
                        dtlPo.setInpartCname(po.getPartCname());
                        dtlPo.setInpartCode(po.getPartCode());
                        dtlPo.setInpartOldcode(po.getPartOldcode());
                        dtlPo.setAdjustAmount(Arith.round(balancePO.getBuyPrice1Notax() * balancePO.getBalQty(), 2) - Arith.round(balancePO.getBuyPriceNotax() * balancePO.getBalQty(), 2));
                        dtlPo.setSumQty(balancePO.getBalQty());
                        PartBuyPriceDao priceDao = PartBuyPriceDao.getInstance();
                        TtPartDefinePO pPo = new TtPartDefinePO();
                        pPo.setPartOldcode(po.getPartOldcode());
                        PageResult<Map<String, Object>> ps1 = priceDao.queryPartInfoList1(pPo, "", 1, 10);
                        String yccb = ps1.getRecords().get(0).get("YCCB").toString();
                        String qcamount = ps1.getRecords().get(0).get("QCAMOUNT").toString();
                        dtlPo.setCostPrice(Double.parseDouble(yccb));
                        dtlPo.setChgId(1L);
                        dtlPo.setInCost(Double.parseDouble(qcamount));
                        dtlPo.setCreateDate(new Date());
                        dtlPo.setCreateBy(logonUser.getUserId());
                        dtlPo.setVenderId(po.getFcvderId());
                        dtlPo.setWhId(po.getWhId());
                        dtlPo.setTowhId(po.getWhId());
                        dtlPo.setBalanceId(balancePO.getBalanceId());
                        dao.insert(dtlPo);
                    }
                    updatepo.setVer(ver + 1);//让版本号加1
//                    dao.insertMain(batNo);
                    //提交数据之前需要比较开始读取的版本号(ver)与此时读取的版本号(ver1),如果发现ver1<=ver就结算失败,并回滚数据
                    Map map = dao.getVerByInId(po.getInId());//通过入库id获取入库表版本号
                    int ver1 = ((BigDecimal) map.get("VER")).intValue();
                    if (updatepo.getVer() > ver1) {
                        dao.insert(balancePO);//新增订单结算信息
                        dao.updateVer(updatepo);//把当前最新版本号更新到数据库
                    } else {
                        errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】正在结算,请稍后再试!<br>";
                       /* POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                }
            } else {
                String[] inIds = request.getParamValues("cb");//获取采购入库id
                for (int i = 0; i < inIds.length; i++) {

                    long inId = CommonUtils.parseLong(inIds[i]);
                    Long balQty = CommonUtils.parseLong(request.getParamValue("BAL_QTY1" + inId));//结算数量

                    //判断该当前入库明细是否结算
                    TtPartPoInPO poInPO = new TtPartPoInPO();
                    poInPO.setInId(inId);
                    poInPO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);

                    if (dao.select(poInPO).size() > 0) {
                        poInPO = ((TtPartPoInPO) dao.select(poInPO).get(0));
                        errors = "入库单【" + poInPO.getInCode() + "】中的配件【" + poInPO.getPartOldcode() + "】已经结算!";
                      /*  POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    TtPartVenderDefinePO vpo = new TtPartVenderDefinePO();
                    vpo.setVenderId(Long.parseLong(request.getParamValue("VENDER_ID" + inId)));
                    vpo = (TtPartVenderDefinePO) dao.select(vpo).get(0);
                    if (vpo.getIsSuspend().equals("10041001")) {
                        errors = "供应商" + vpo.getVenderName() + "已经暂停业务，无法进行结算！";
                      /*  POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    //查询入库单信息
                    TtPartPoInPO po = new TtPartPoInPO();
                    po.setInId(inId);

                    po = (TtPartPoInPO) dao.select(po).get(0);
                    TtPartDefinePO partPo = new TtPartDefinePO();
                    partPo.setPartId(po.getPartId());
                    partPo = (TtPartDefinePO) dao.select(partPo).get(0);

                    //如果采购员,财务供应商,采购组织不相同则从新生成结算单
                    if (!checkMap.containsValue(po.getWhName() + request.getParamValue("buyName" + inId) + request.getParamValue("VENDER_NAME2" + inId) + ":" + partPo.getProduceFac())) {
                        checkMap.put(po.getWhName() + request.getParamValue("buyName" + inId) + request.getParamValue("VENDER_NAME2" + inId) + ":" + partPo.getProduceFac(), po.getWhName() + request.getParamValue("buyName" + inId) + request.getParamValue("VENDER_NAME2" + inId) + ":" + partPo.getProduceFac());//供应商+采购组织
                        //结算单号
                        balanceCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_05);
                    }
                    //验证结算数量是否大于待结算数量
                    if (po.getInQty() > 0) {
                        if (balQty > (po.getInQty() - po.getReturnQty() - po.getBalQty())) {
                            errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】的结算数量大于待结算数量(" + (po.getInQty() - po.getReturnQty() - po.getBalQty()) + ")!";
                           /* POContext.endTxn(false);//回滚事务
                            POContext.cleanTxn();
                                 break;*/
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                            throw e1;
                        }
                    }
                    if (po.getInQty() < 0) {
                        if (Math.abs(balQty) > Math.abs(po.getInQty() + po.getReturnQty() - po.getBalQty())) {
                            errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】的结算数量大于待结算数量(" + (po.getInQty() - po.getReturnQty() - po.getBalQty()) + ")!";
                           /* POContext.endTxn(false);//回滚事务
                            POContext.cleanTxn();
                            break;*/
                            BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                            throw e1;
                        }
                    }
                    //预更新入库明细,如果还有剩余数量没有结算,那么该入库明细就还可以结算,不改变其状态
                    TtPartPoInPO updatepo = new TtPartPoInPO();
                    updatepo.setInId(po.getInId());
                    updatepo.setBalQty(po.getBalQty() + balQty);
                    updatepo.setSpareQty(po.getInQty() - po.getReturnQty() - updatepo.getBalQty());
                    updatepo.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//更新入库单状态为结算中

                    //获取当前版本信息
                    int ver = po.getVer();
                    TtPartPoBalancePO balancePO = new TtPartPoBalancePO();//将每次结算信息保存到采购结算明细表中
                    balancePO.setBalanceId((CommonUtils.parseLong(SequenceManager.getSequence(""))));
                    balancePO.setBalanceCode(balanceCode);//结算单号
                    balancePO.setInId(po.getInId());
                    balancePO.setProduceFac(Long.valueOf(po.getProduceFac()));
                    balancePO.setProduceState(po.getProduceState());
                    balancePO.setSuperiorPurchasing(po.getSuperiorPurchasing());
                    balancePO.setInCode(po.getInCode());
                    balancePO.setInType(po.getInType());
                    balancePO.setCheckId(po.getCheckId());//验收单ID
                    balancePO.setCheckCode(po.getCheckCode());//验收单号
                    balancePO.setPoId(po.getPoId());
                    balancePO.setOrderCode(po.getOrderCode());
                    balancePO.setPlanCode(po.getPlanCode());
                    balancePO.setBuyerId(po.getBuyerId());
                    balancePO.setPartType(po.getPartType());
                    balancePO.setPartId(po.getPartId());
                    balancePO.setPartCode(po.getPartCode());
                    balancePO.setPartOldcode(po.getPartOldcode());
                    balancePO.setPartCname(po.getPartCname());
                    balancePO.setUnit(po.getUnit());
                    balancePO.setVenderId(po.getVenderId());
                    balancePO.setVenderCode(po.getVenderCode());
                    balancePO.setVenderName(po.getVenderName());
                    balancePO.setBuyPrice(po.getBuyPrice());
                    balancePO.setBuyPriceNotax(po.getBuyPriceNotax());
                    balancePO.setInAmount(po.getInAmount());
                    balancePO.setInAmountNotax(po.getInAmountNotax());
                    List<Map<String, Object>> priceList = new ArrayList();
                    Double price = dao.getBuyPrice(po.getPartId(), po.getFcvderId(), DateUtil.format(po.getInDate(), "yyyy-MM-dd"), Long.valueOf(po.getSuperiorPurchasing()));
                    if (price == 0D) {
                        errors = "配件" + po.getPartCode() + "与财务供应商没有签订合同或没有采购价格<br>";
                       /* POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }
                    balancePO.setBuyPrice1(price);//入库确认已经确认过财务供应商，不在需要重新获取供应商价格
                    balancePO.setBuyQty(po.getBuyQty());
                    balancePO.setCheckQty(po.getCheckQty());
                    balancePO.setInQty(po.getInQty());//入库数量
                    balancePO.setReturnQty(po.getReturnQty());//退货数量
                    balancePO.setBalQty(balQty);//结算数量
                    balancePO.setBalAmount(Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1()), 2));//含税金额(结算金额)
                    balancePO.setTaxRate(Constant.PART_TAX_RATE);//税率
                    balancePO.setBuyPrice1Notax(Arith.round(Arith.div(balancePO.getBuyPrice1(), Arith.add(1, Constant.PART_TAX_RATE)), 6));//无税采购单价=含税单价/（1+税率）
                    balancePO.setBalAmountNotax(Arith.round(Arith.mul(balancePO.getBalQty(), balancePO.getBuyPrice1Notax()), 2));//无税结算金额=无税采购单价*结算数量
                    //end modify
                    balancePO.setWhId(po.getWhId());
                    balancePO.setWhName(po.getWhName());
                    balancePO.setBatchNo(po.getBatchNo());
                    balancePO.setLocId(po.getLocId());
                    balancePO.setLocCode(po.getLocCode());
                    balancePO.setBuyGroup(Long.valueOf(po.getProduceFac()));
                    balancePO.setFcvderId(po.getFcvderId());
                    balancePO.setFcvderrCode(po.getFcvderrCode());
                    balancePO.setFtvenderId(po.getFtvenderId());
                    balancePO.setIsGauge(po.getIsGauge());
                    balancePO.setRemark(CommonUtils.checkNull(request.getParamValue("REMARK" + inId)));
                    balancePO.setInDate(po.getInDate());
                    balancePO.setInBy(po.getInBy());
                    balancePO.setCheckDate(po.getCheckDate());
                    balancePO.setCheckBy(po.getCheckBy());
                    balancePO.setIsCheck(po.getIsCheck());
                    balancePO.setCreateDate(new Date());
                    balancePO.setCreateBy(logonUser.getUserId());
                    balancePO.setOrgId(logonUser.getOrgId());
                    balancePO.setBalanceDate(new Date());
                    balancePO.setBalanceBy(logonUser.getUserId());
                    balancePO.setIsBalances(Constant.IF_TYPE_NO);//生成结算明细的时候状态为未结算,与发票号关联之后状态变为已结算
                    balancePO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);//结算中
                    balancePO.setMakerId(po.getMakerId());
                    balancePO.setOriginType(po.getOriginType());
                    balancePO.setBalanceType(balanceType);
                    balancePO.setBatId(batNo);
                    dao.update(updatepo);//更新采购订单入库表,此时将会锁定该记录,直到事务提交才释放
                    int isGauge = po.getIsGauge();
                    String check ="10041002"; //CommonDAO.getPara("60161001");//暂时不知业务逻辑这个是什么
                    if ((Arith.round(balancePO.getBuyPrice1Notax() * balancePO.getBalQty(), 2) - Arith.round(balancePO.getBuyPriceNotax() * balancePO.getBalQty(), 2)) != 0 && !check.equals("10041002")) {
                        tz += 1;
                        TtPartStockChgDtlPO dtlPo = new TtPartStockChgDtlPO();//成本调整副表
                        dtlPo.setDtlId(Long.parseLong(SequenceManager.getSequence("")));
                        //dtlPo.setChgId(changeId);
                        dtlPo.setPartId(po.getPartId());
                        dtlPo.setPartCname(po.getPartCname());
                        dtlPo.setPartCode(po.getPartCode());
//                        dtlPo.setBatId(batNo);
                        dtlPo.setPartOldcode(po.getPartOldcode());
                        dtlPo.setInpartId(po.getPartId());
                        dtlPo.setInpartCname(po.getPartCname());
                        dtlPo.setInpartCode(po.getPartCode());
                        dtlPo.setInpartOldcode(po.getPartOldcode());
                        dtlPo.setAdjustAmount(Arith.round(balancePO.getBuyPrice1Notax() * balancePO.getBalQty(), 2) - Arith.round(balancePO.getBuyPriceNotax() * balancePO.getBalQty(), 2));
                        dtlPo.setSumQty(balancePO.getBalQty());
                        PartBuyPriceDao priceDao = PartBuyPriceDao.getInstance();
                        TtPartDefinePO pPo = new TtPartDefinePO();
                        pPo.setPartOldcode(po.getPartOldcode());
                        PageResult<Map<String, Object>> ps = priceDao.queryPartInfoList1(pPo, "", 1, 10);
                        String yccb = ps.getRecords().get(0).get("YCCB").toString();
                        String qcamount = ps.getRecords().get(0).get("QCAMOUNT").toString();
                        dtlPo.setCostPrice(Double.parseDouble(yccb));
                        dtlPo.setChgId(1L);
                        dtlPo.setInCost(Double.parseDouble(qcamount));
                        dtlPo.setCreateDate(new Date());
                        dtlPo.setCreateBy(logonUser.getUserId());
                        dtlPo.setVenderId(po.getFcvderId());
                        dtlPo.setWhId(po.getWhId());
                        dtlPo.setTowhId(po.getWhId());
                        dtlPo.setBalanceId(balancePO.getBalanceId());
                        dao.insert(dtlPo);
                    }
                    updatepo.setVer(ver + 1);//让版本号加1

                    //提交数据之前需要比较开始读取的版本号(ver)与此时读取的版本号(ver1),如果发现ver1<=ver就结算失败,并回滚数据
                    Map map = dao.getVerByInId(po.getInId());//通过入库id获取入库表版本号
                    int ver1 = ((BigDecimal) map.get("VER")).intValue();
                    if (updatepo.getVer() > ver1) {
                        dao.insert(balancePO);//新增订单结算信息
                        dao.updateVer(updatepo);//把当前最新版本号更新到数据库
                    } else {
                        errors = "入库单【" + po.getInCode() + "】中的配件【" + po.getPartOldcode() + "】正在结算,请稍后再试!<br>";
                       /* POContext.endTxn(false);//回滚事务
                        POContext.cleanTxn();
                        break;*/
                        BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, errors);
                        throw e1;
                    }

                }

            }
            dao.insertMain(batNo);
            dao.changeBalanceCode(batNo);
            if ("".equals(errors)) {
                success = "生成结算单成功!";
            }
            act.setOutData("success", success);
            act.setOutData("changeId", batNo);
            act.setOutData("tz", tz);
            act.setOutData("error", errors);
            this.purchaseOrderBalanceQueryInit();
//            act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, errors);
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }
    /**
     * @param :
     * @return :
     * @throws :BizException LastDate    : 2013-4-18
     * @Title :
     * @Description: 采购订单查询初始化
     */
    public void purchaseOrderBalanceDetailQueryInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
        try {
            List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
            request.setAttribute("planerList", planerList);
            act.setOutData("wareHouses", list);
            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("now",CommonUtils.getDate());
            act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
            act.setForword(PART_PURCHASEORDERBALANCEDETAIL_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
        
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 结算明细查询
     */
    public void queryOrderBalanceDetailInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE2"));//采购订单号
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//接收单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID2"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String balanceBeginTime = CommonUtils.checkNull(request.getParamValue("balanceBeginTime"));//结算开始时间
            String balanceEndTime = CommonUtils.checkNull(request.getParamValue("balanceEndTime"));//结算结束时间
            String balanceName = CommonUtils.checkNull(request.getParamValue("BALANCE_NAME"));//结算人员
            String is_balances = CommonUtils.checkNull(request.getParamValue("IS_BALANCES"));//是否已结算
            String invo= CommonUtils.checkNull(request.getParamValue("INVO_NO"));//是否已结算
            String planerId= CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员

            TtPartPoBalancePO po = new TtPartPoBalancePO();

            po.setOrderCode(orderCode);
            po.setCheckCode(chkCode);
            if (!"".equals(whId)) {
                po.setWhId(CommonUtils.parseLong(whId));
            }
            if (!"".equals(partType)) {
                po.setPartType(CommonUtils.parseInteger(partType));
            }
            if (!"".equals(venderId)) {
                po.setVenderId(CommonUtils.parseLong(venderId));
            }
            if (!"".equals(is_balances)) {
                po.setIsBalances(CommonUtils.parseInteger(is_balances));
            }
            if (!"".equals(invo)) {
                po.setInvoNo(invo);
            }
            po.setPartOldcode(partOldCode);
            po.setPartCname(partName);
            po.setPartCode(partCode);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderBalanceDetailList(po,planerId, beginTime, endTime, balanceBeginTime, balanceEndTime,
                    balanceName, inCode, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 设置发票号
     */
    public void setBalanceOrderInvoNo() {


        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BAL_CODE"));
            String invoNo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
            String inAmountNotax = CommonUtils.checkNull(request.getParamValue("IN_AMOUNT_NOTAX"));

            	//设置发票号
                TtPartPoBalancePO spo = new TtPartPoBalancePO();
                spo.setBalanceCode(balanceCode);
                TtPartPoBalancePO po = new TtPartPoBalancePO();
                po.setInvoNo(invoNo);
                po.setInAmountNotax(Double.valueOf(inAmountNotax).doubleValue());
                dao.update(spo, po);//更新采购订单结算表
                act.setOutData("success", "1");
                act.setOutData("balanceCode", balanceCode);
            
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发票号设置失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-20
     * @Title :
     * @Description: 查询制造商信息
     */
    public void queryMakerInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curMakerId = CommonUtils.checkNull(request.getParamValue("curMakerId"));
            String inId = CommonUtils.checkNull(request.getParamValue("inId"));
            String curVenderId = CommonUtils.checkNull(request.getParamValue("curVenderId"));
            PurchaseOrderChkDao chkDao = PurchaseOrderChkDao.getInstance();
            List makers = chkDao.queryMakerInfo(CommonUtils.parseLong(curVenderId));
            act.setOutData("makers", makers);
            act.setOutData("inId", inId);
            act.setOutData("curMakerId", curMakerId);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购订单结算");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title :
     * @Description: 结算删除
     */
    public void deletePo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
            String inId = CommonUtils.checkNull(request
                    .getParamValue("inId")); // 入库单Id
            String checkId = CommonUtils.checkNull(request
                    .getParamValue("checkId")); // 验收单Id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            TtPartPoInPO inPO = new TtPartPoInPO();
            inPO.setInId(CommonUtils.parseLong(inId));
            inPO = (TtPartPoInPO) dao.select(inPO).get(0);

            //删除该入库单
            TtPartPoInPO inPO1 = new TtPartPoInPO();
            inPO1.setInId(CommonUtils.parseLong(inId));
            TtPartPoInPO inPO2 = new TtPartPoInPO();
            inPO2.setStatus(0);


            //删除入库单之后需要把验收单中的中的待入库数量增加,同时调用出库逻辑，如果该验收订单的状态为入库完成,就要将其状态改为入库中
            TtPartPoChkPO chkPO = new TtPartPoChkPO();
            chkPO.setCheckId(CommonUtils.parseLong(checkId));
            chkPO = (TtPartPoChkPO) dao.select(chkPO).get(0);

            //删除该验收单
            TtPartPoChkPO chkPO1 = new TtPartPoChkPO();
            chkPO1.setCheckId(CommonUtils.parseLong(checkId));
            TtPartPoChkPO chkPO2 = new TtPartPoChkPO();
            chkPO2.setSpareQty(chkPO.getSpareQty() + (inPO.getInQty() - inPO.getReturnQty()));
            chkPO2.setInQty(chkPO.getInQty() - (inPO.getInQty() - inPO.getReturnQty()));

            if (chkPO.getState().intValue() == Constant.PART_PURCHASE_ORDERCIN_STATUS_02.intValue()) {//如果验收单状态为入库完成,就改为入库中
                chkPO2.setState(Constant.PART_PURCHASE_ORDERCIN_STATUS_01);
            }

            //插入数据到出入库记录表

            TtPartRecordPO ttPartRecordPO = new TtPartRecordPO();
            ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
            ttPartRecordPO.setAddFlag(2);//出库标记
            ttPartRecordPO.setState(1);//正常出库
            ttPartRecordPO.setPartNum(inPO.getInQty() - inPO.getReturnQty());//出库数量
            ttPartRecordPO.setTranstypeId(0l);//默认0
            ttPartRecordPO.setPartId(inPO.getPartId());//配件ID
            ttPartRecordPO.setPartCode(inPO.getPartCode());//配件件号
            ttPartRecordPO.setPartOldcode(inPO.getPartOldcode());//配件编码
            ttPartRecordPO.setPartName(inPO.getPartCname());//配件名称
            ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
            ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
            ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_09));//出库单
            ttPartRecordPO.setOrderId(CommonUtils.parseLong(inId));//出库单ID
            ttPartRecordPO.setOrderCode(inPO.getInCode());//出库单编码
            //ttPartRecordPO.setLineId();
            List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
            ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
            ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
            ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
            ttPartRecordPO.setWhId(inPO.getWhId());
            //ttPartRecordPO.setWhName(po.getWhName());

            TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
            loactionDefinePO.setWhId(inPO.getWhId());
            loactionDefinePO.setPartId(inPO.getPartId());
            loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
            loactionDefinePO.setState(Constant.STATUS_ENABLE);
            loactionDefinePO.setStatus(1);
            loactionDefinePO = (TtPartLoactionDefinePO) dao.select(loactionDefinePO).get(0);

            ttPartRecordPO.setLocId(loactionDefinePO.getLocId());
            ttPartRecordPO.setLocCode(loactionDefinePO.getLocCode());
            ttPartRecordPO.setOptDate(new Date());
            ttPartRecordPO.setCreateDate(new Date());
            ttPartRecordPO.setPersonId(logonUser.getUserId());
            ttPartRecordPO.setPersonName(logonUser.getName());
            ttPartRecordPO.setPartState(1);

            dao.insert(ttPartRecordPO);
            dao.update(inPO1, inPO2);
            dao.update(chkPO1, chkPO2);
            //调用出库逻辑
            List ins = new LinkedList<Object>();
            ins.add(0, CommonUtils.parseLong(inId));
            ins.add(1, Constant.PART_CODE_RELATION_09);
            ins.add(2, 0);//0表示先前未占用(默认),1表示先前已占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
            act.setOutData("success", "关闭成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "删除失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-21
     * @Title :
     * @Description: 结算明细删除
     */
    public void deleteDtlPo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);

        try {
          /*  String inId = CommonUtils.checkNull(request
                    .getParamValue("inId")); // 入库单Id
            String balanceId = CommonUtils.checkNull(request
                    .getParamValue("balanceId")); // 结算Id*/
            String chkId = CommonUtils.checkNull(request.getParamValue("chkId")); // 验收Id
            String curPage = CommonUtils.checkNull(request
                    .getParamValue("curPage"));// 当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }

            //删除结算明细
            TtPartPoBalancePO balancePO1 = new TtPartPoBalancePO();
            //TtPartPoBalancePO balancePO2 = new TtPartPoBalancePO();
            //关闭验收单相关的所有结算单 modify by yuan 20130720
            //balancePO1.setBalanceId(CommonUtils.parseLong(balanceId));
            balancePO1.setCheckId(CommonUtils.parseLong(chkId));
            // balancePO2.setStatus(0);
            // balancePO2.setUpdateBy(logonUser.getUserId());
            // balancePO2.setUpdateDate(new Date());

            //删除之后需要更新入库单状态为待结算
            TtPartPoInPO inPO1 = new TtPartPoInPO();
            TtPartPoInPO inPO2 = new TtPartPoInPO();
            //更新验收单相关的所有入库单 modify by yuan 20130720
            //inPO1.setInId(CommonUtils.parseLong(inId));
            inPO1.setCheckId(CommonUtils.parseLong(chkId));
            inPO2.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
            //inPO2.setMakerId(0l);
            inPO2.setUpdateBy(logonUser.getUserId());
            inPO2.setUpdateDate(new Date());

            // dao.update(balancePO1, balancePO2);
            dao.delete(balancePO1);
            dao.update(inPO1, inPO2);

            act.setOutData("success", "删除成功!");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "删除失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 
     * @Title      : 
     * @Description: 结算单打印查询初始化 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void purOrderBalancePrintQueryInit(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_PURCHASEORDERBALANCE_PRINT_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算凭证打印");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算单汇总查询初始化
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void purOrderBalAllQueryInit(){
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		act.setForword(PART_PURCHASEORDERBALANCE_ALL_URL);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购结算汇总");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算单明细查询初始化
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void purOrderBalDtlQueryInit(){
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		List wareHouses = new ArrayList();
    		wareHouses = dao.getPartWareHouseList(logonUser);//获取配件库房信息
    		act.setOutData("wareHouses", wareHouses);
    		act.setForword(PART_PURCHASEORDERBALANCE_DTL_URL);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "采购结算明细");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }
    
    
    
    /**
     * 
     * @Title      : 
     * @Description: 结算单打印查询 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void queryOrderBalancePrint(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String invoNo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));//发票号
            String isConf = CommonUtils.checkNull(request.getParamValue("isConf"));//是否是财务确认查询

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryOrderBalancePrintList(balanceCode, venderName, invoNo,
            		isConf,curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
     * 
     * @Title      : 
     * @Description: 结算单汇总查询 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void queryOrderBalanceAll(){
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		//分页方法 begin
    		Integer curPage = request.getParamValue("curPage") != null ? Integer
    				.parseInt(request.getParamValue("curPage"))
    				: 1; // 处理当前页
    				PageResult<Map<String, Object>> ps = dao.queryOrderBalAllList(request,curPage, Constant.PAGE_SIZE);
    				//分页方法 end
    				act.setOutData("ps", ps);
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 查看打印明细 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void purOrderBalancePrintDtlInit(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        	String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
        	String flag = CommonUtils.checkNull(request.getParamValue("flag"));//返回标志
        	List list = dao.getPartWareHouseList(logonUser);//获取配件库房信息
            act.setOutData("wareHouses", list);
            act.setForword(PART_PURCHASEORDERBALANCE_PRINTDTL_URL);
            request.setAttribute("BALANCE_CODE", balanceCode);
            request.setAttribute("flag", flag);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算凭证打印明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description:结算单打印明细查询 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void queryOrderBalanceDtlInfo(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
        	String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
        	String checkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//结算单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String produceState = CommonUtils.checkNull(request.getParamValue("PRODUCE_STATE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
            String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
            
            TtPartPoBalancePO po = new TtPartPoBalancePO();
            po.setCheckCode(checkCode);
            if (!"".equals(whId)) {
                po.setWhId(CommonUtils.parseLong(whId));
            }
            if (!"".equals(produceState)) {
//                po.setPartType(CommonUtils.parseInteger(partType));
                po.setProduceState(CommonUtils.parseInteger(produceState));
            }
            if (!"".equals(venderId)) {
                po.setVenderId(CommonUtils.parseLong(venderId));
            }
            po.setPartOldcode(partOldCode);
            po.setPartCname(partName);
            po.setPartCode(partCode);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartOrderBalanceDtlList(po, beginTime, endTime, inBeginTime, inEndTime,
                    inCode, curPage, Constant.PAGE_SIZE, balanceCode);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算凭证打印明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算单打印 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-22
     */
    public void purOrderBalancePrint(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			 RequestWrapper request = act.getRequest();
			 
			 String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
			 
			 Map<String,Object> map  = dao.queryBalanceInfoByCode(balanceCode);
			 
			 if(map!=null)
			 {
				 TtPartBillDefinePO definePO = new TtPartBillDefinePO();
				 Long venderId = ((BigDecimal)map.get("VENDER_ID")).longValue();
				 definePO.setDealerId(venderId);
				 if(dao.select(definePO).size()>0){
					 definePO = (TtPartBillDefinePO) dao.select(definePO).get(0);
				 }else{
					 request.setAttribute("error", "当前付款凭证没有设置开票信息,如需设置,请联系管理员!");
				 }
				 
                 String AMOUNT_SUM= String.valueOf(CommonUtils.getDataFromMap(map,"IN_AMOUNT"));
				 
                 String NO = balanceCode.substring(4);
                 
				 String asum= AMOUNT_SUM.substring(0,AMOUNT_SUM.indexOf("."));
				 String asum1 = AMOUNT_SUM.substring(AMOUNT_SUM.indexOf(".")+1);
				 char[] r = asum1.toCharArray();
			     char[] c = asum.replace(",","").toCharArray();
			     int k = 1;
			     for(int j = 10 ;j > c.length; j--)
			     {
			    	 act.setOutData("m"+k,"");
			    	 k++;
			     }
			     int b = 0;
				 for (int i = c.length; i >= 1; i--) {
					act.setOutData("m"+k,Integer.parseInt(String.valueOf(c[b])));
					k++;
					b++;
				 }
				 
				 act.setOutData("m11",String.valueOf(r[0]));
				 act.setOutData("m12",String.valueOf(r[1]));
				 act.setOutData("map", map);
				 act.setOutData("definePO", definePO);
				 act.setOutData("NO", NO);
			 }
			 act.setForword(PART_PURORDERBALANCE_PRINT_URL);
			 
			
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"结算凭证打印失败,请联系管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算确认查询初始化 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-26
     */
    public void purOrderBalanceConfirmQueryInit(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_PURCHASEORDERBALANCE_CONFIRM_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算确认");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 配件结算确认 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-26
     */
    public void confirmBalance(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] codes = request.getParamValues("ck");//结算单号
            if(codes!=null&&codes.length>0){
            	for(String code:codes){
            		dao.updateState(code,logonUser.getUserId());//把入库单的状态和结算单的状态修改为财务已确认
            	}
            }
            act.setOutData("success", "确认成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件结算确认失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
     * 
     * @Title      : 
     * @Description: 配件结算驳回
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-26
     */
    public void rejectBalanceOrder(){
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
    	try {
    		String[] codes = request.getParamValues("ck");//结算单号
    		String rejectReason = CommonUtils.checkNull(request.getParamValue("rejectReason"));//驳回原因
    		if(codes!=null&&codes.length>0){
    			for(String code:codes){
    				 dao.updateState1(code, rejectReason, logonUser.getUserId().toString());//把入库单的状态和结算单的状态修改为已驳回
    			}
    		}
    		act.setOutData("success", "驳回成功!");
    	} catch (Exception e) {//异常方法
    		BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件结算驳回失败,请联系管理员!");
    		logger.error(logonUser, e1);
    		act.setException(e1);
    	}
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算确认明细查询初始化 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-26
     */
    public void queryBalanceConfirmDtlInit(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_PURCHASEORDERBALANCE_CONFIRMDTL_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "结算确认明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * 
     * @Title      : 
     * @Description: 结算确认明细查询 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-26
     */
    public void queryBalanceConfirmDtl(){
    	ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String invoNo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));//发票号

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryBalanceConfDtlList(balanceCode, venderName, invoNo,
            		curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    
    /**
	 * 
	 * @Title      : 新增初始化
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     : luole
	 * LastDate    : 2013-4-15
	 */
	public void addInit(){
		ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
		try {
			//获取结算单号
			String balanceCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_05);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String balanceDate = format.format(new Date());
			
			List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
			act.setOutData("curUserId", logonUser.getUserId());
			act.setOutData("planerList", planerList);
            
			act.setOutData("balanceCode", balanceCode);
			act.setOutData("balanceDate", balanceDate);
			act.setOutData("balancer", logonUser.getName());
			
			act.setForword(PART_PURCHASEORDERBALANCE_ADD_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "采购订单结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询制造商信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013年12月11日
	 */
	public void queryPartMakerInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String makerCode = CommonUtils.checkNull(request.getParamValue("MAKER_CODE"));// 制造商代码
			makerCode = makerCode.toUpperCase();
			String makerName = CommonUtils.checkNull(request.getParamValue("MAKER_NAME"));// 制造商名称
			TtPartMakerDefinePO bean = new TtPartMakerDefinePO();
			bean.setMakerCode(makerCode);
			bean.setMakerName(makerName);

			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartMakerList(bean, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "制造商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	 /**
	 * 
	 * @Title      : 
	 * @Description: 打印 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-1
	 */
	@SuppressWarnings("unchecked")
	public void opPrintHtml(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
	try{
		
		String balCode = CommonUtils.checkNull(request.getParamValue("balCode"));
		List allList = new ArrayList();
		Map<String,Object> map = new HashMap<String, Object>();
		
		map = dao.queryPurOrderBalMainByCode(balCode);
		
		List<Map<String,Object>> detailList = dao.queryPurOrderBalListByCode(balCode);
		
		for(int i=0;i<detailList.size();){
			List subList = detailList.subList(i, i+PRINT_SIZE>detailList.size()?detailList.size():i+PRINT_SIZE);
			i=i+PRINT_SIZE;
			allList.add(subList);
		}
		
		//取出最后一个元素
		List lastList = (List) allList.get(allList.size()-1);
		
		if(lastList.size()==PRINT_SIZE){//如果最后一个list的大小刚好等于打印中每页允许的最大记录数,就需要取出其中最后一个元素放到另一个list中
			List newList = new ArrayList();
			Map map2 = (Map) lastList.get(lastList.size()-1);
			newList.add(map2);
			allList.remove(lastList);
			List list1 = lastList.subList(0, lastList.size()-1);
			allList.add(list1);
			allList.add(newList);
		}
		
		act.setOutData("mainMap", map);
		act.setOutData("detailList", detailList);
		act.setOutData("allList", allList);
		act.setForword(PURCHASEORDERBAL_PRINT_URL);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"进货单打印错误,请联系管理员!");
		logger.error(loginUser,e1);
		act.setException(e1);
	}
  }
	
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出
     */
    public void exportOrderBalExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List wareHouses = new ArrayList();
        try {
            String[] head = new String[23];
            head[0] = "验收单号";
            head[1] = "入库单号";
            head[2] = "结算单号";
            head[3] = "采购员";
            head[4] = "配件类型";
            head[5] = "配件编码";
            head[6] = "配件名称";
            head[7] = "配件件号";
            head[8] = "供应商名称";
            head[9] = "制造商名称";
            head[10] = "入库库房";
            head[11] = "入库人员";
            head[12] = "入库数量";
            head[13] = "退货数量";
            head[14] = "结算数量";
            head[15] = "结算人员";
            head[16] = "计划价";
            head[17] = "计划金额";
            head[18] = "采购价";
            head[19] = "采购金额";
            head[20] = "状态";
            head[21] = "发票号";
            List<Map<String, Object>> list = dao.queryPurchaseOrderBal(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[23];
                        detail[0] = CommonUtils.checkNull(map.get("CHECK_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("IN_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("BALANCE_CODE"));
                        detail[3] = CommonUtils
                        .checkNull(map.get("BUYER"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PART_TYPE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[8] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[9] = CommonUtils.checkNull(map.get("MAKER_NAME"));
                        detail[10] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[11] = CommonUtils.checkNull(map.get("IN_NAME"));
                        detail[12] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[13] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                        detail[14] = CommonUtils.checkNull(map.get("BAL_QTY"));
                        detail[15] = CommonUtils.checkNull(map.get("NAME"));
                        detail[16] = CommonUtils.checkNull(map.get("PLAN_PRICE"));
                        detail[17] = CommonUtils.checkNull(map.get("PLAN_AMOUNT"));
                        detail[18] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[19] = CommonUtils.checkNull(map.get("BAL_AMOUNT"));
                        detail[20] = CommonUtils.checkNull(map.get("STATE"));
                        detail[21] = CommonUtils.checkNull(map.get("INVO_NO"));
                        list1.add(detail);
                    }
                }
                
            } 
            this.exportEx(ActionContext.getContext().getResponse(),
                    request, head, list1, "订单结算.xls");
        } catch (Exception e) {
        	String backFlag = CommonUtils.checkNull(request.getParamValue("backFlag"));
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("wareHouses", wareHouses);
            if("1".equals(backFlag)){
            	act.setForword(PART_PURCHASEORDERBALANCE_DTL_URL);
            }else{
            	act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL);
            }
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出
     */
    public void exportOrderBalDifExcel() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	List wareHouses = new ArrayList();
    	try {
    		RequestWrapper request = act.getRequest();
    		String[] head = new String[9];
    		head[0] = "结算单号";
    		head[1] = "配件类型";
    		head[2] = "配件编码";
    		head[3] = "配件名称";
    		head[4] = "配件件号";
    		head[5] = "计划价";
    		head[6] = "采购价";
    		head[7] = "价格差异";
    		List<Map<String, Object>> list = dao.queryPurchaseOrderBal(request);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[9];
    					detail[0] = CommonUtils
    					.checkNull(map.get("BALANCE_CODE"));
    					detail[1] = CommonUtils.checkNull(map
    							.get("PART_TYPE"));
    					detail[2] = CommonUtils.checkNull(map
    							.get("PART_OLDCODE"));
    					detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
    					detail[4] = CommonUtils.checkNull(map.get("PART_CODE"));
    					detail[5] = CommonUtils.checkNull(map.get("PLAN_PRICE"));
    					detail[6] = CommonUtils.checkNull(map.get("BUY_PRICE"));
    					detail[7] = CommonUtils.checkNull(map.get("DIF_PRICE"));
    					list1.add(detail);
    				}
    			}
    			
    		} 
    		this.exportEx(ActionContext.getContext().getResponse(),
					request, head, list1, "结算差异信息.xls");
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
    		act.setOutData("wareHouses", wareHouses);
    		act.setForword(PART_PURCHASEORDERBALANCEDETAIL_QUERY_URL);
    	}
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出
     */
    public void exportPurOrder() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	try {
    		RequestWrapper request = act.getRequest();
    		String balCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));
    		String invo_no = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
    		String vender_name = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));
    		String[] head = new String[18];
    		head[0] = "验收单号";
    		head[1] = "入库单号";
    		head[2] = "入库日期";
    		head[3] = "配件类型";
    		head[4] = "配件编码";
    		head[5] = "配件名称";
    		head[6] = "配件件号";
    		head[7] = "入库库房";
    		head[8] = "入库数量";
    		head[9] = "退货数量";
    		head[10] = "结算数量";
    		head[11] = "供应商名称";
    		head[12] = "制造商名称";
    		head[13] = "计划价";
    		head[14] = "计划金额";
    		head[15] = "采购价";
    		head[16] = "采购金额";
    		List<Map<String, Object>> list = dao.queryPurOrderDtl(balCode,invo_no,vender_name);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[18];
    					detail[0] = CommonUtils
    							.checkNull(map.get("CHECK_CODE"));
    					detail[1] = CommonUtils.checkNull(map
    							.get("IN_CODE"));
    					detail[2] = CommonUtils.checkNull(map
    							.get("IN_DATE"));
    					detail[3] = CommonUtils.checkNull(map.get("PART_TYPE"));
    					detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
    					detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
    					detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
    					detail[7] = CommonUtils.checkNull(map.get("WH_NAME"));
    					detail[8] = CommonUtils.checkNull(map.get("IN_QTY"));
    					detail[9] = CommonUtils.checkNull(map.get("RETURN_QTY"));
    					detail[10] = CommonUtils.checkNull(map.get("BAL_QTY"));
    					detail[11] = CommonUtils.checkNull(map.get("VENDER_NAME"));
    					detail[12] = CommonUtils.checkNull(map.get("MAKER_NAME"));
    					detail[13] = CommonUtils.checkNull(map.get("PLAN_PRICE"));
    					detail[14] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
    					detail[15] = CommonUtils.checkNull(map.get("BUY_PRICE"));
    					detail[16] = CommonUtils.checkNull(map.get("BAL_AMOUNT"));
    					list1.add(detail);
    				}
    			}
    		}
            this.exportEx(ActionContext.getContext().getResponse(),
                    request, head, list1, "采购订单结算明细.xls");

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
    		act.setForword(PART_PURCHASEORDERBALANCE_CONFIRM_URL);
    	}
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出 结算明细
     */
    public void exportOrderBalDtlExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List wareHouses = new ArrayList();
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[22];
            head[0] = "验收单号";
            head[1] = "结算单号";
            head[2] = "采购员";
            head[3] = "配件类型";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "配件件号";
            head[7] = "入库库房";
            head[8] = "入库数量";
            head[9] = "退货数量";
            head[10] = "结算数量";
            head[11] = "供应商名称";
            head[12] = "制造商名称";
            head[13] = "采购价";
            head[14] = "采购金额";
            head[15] = "结算金额";
            head[16] = "结算人员";
            head[17] = "入库人员";
            head[18] = "验收人员";
            head[19] = "结算时间";
            head[20] = "发票号";
            List<Map<String, Object>> list = dao.queryPurchaseOrderBalDtl(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[22];
                        detail[0] = CommonUtils.checkNull(map.get("CHECK_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("BALANCE_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                        		.get("BUYER"));
                        int partType = ((BigDecimal)map.get("PART_TYPE")).intValue();
                        if(partType==Constant.PART_BASE_PART_TYPES_SELF_MADE.intValue()){
                        	detail[3]="自制件";
                        }else if(partType==Constant.PART_BASE_PART_TYPES_PURCHASE.intValue()){
                        	detail[3]="国产件";
                        }else if(partType==Constant.PART_BASE_PART_TYPES_ENTRANCE.intValue()){
                        	detail[3]="进口件";
                        }else{
                        	detail[3]="";
                        }
                        detail[4] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("WH_NAME"));
                        detail[8] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("BALANCE_QTY"));
                        detail[11] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                        detail[12] = CommonUtils.checkNull(map.get("MAKER_NAME"));
                        detail[13] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                        detail[14] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
                        detail[15] = CommonUtils.checkNull(map.get("BALANCE_AMOUNT"));
                        detail[16] = CommonUtils.checkNull(map.get("BALANCE_NAME"));
                        detail[17] = CommonUtils.checkNull(map.get("IN_NAME"));
                        detail[18] = CommonUtils.checkNull(map.get("CHECK_NAME"));
                        detail[19] = CommonUtils.checkNull(map.get("BALANCE_DATE"));
                        detail[20] = CommonUtils.checkNull(map.get("INVO_NO"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, "采购订单结算明细.xls");
            } else {
                wareHouses = dao.getPartWareHouseList(logonUser);//获取配件库房信息
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
            act.setOutData("wareHouses", wareHouses);
            act.setForword(PART_PURCHASEORDERBALANCEDETAIL_QUERY_URL);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出待结算明细
     */
    public void expSpareBalDtl() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	List wareHouses = new ArrayList();
    	try {
    		RequestWrapper request = act.getRequest();
    		String[] head = new String[16];
    		head[0] = "验收单号";
    		head[1] = "配件编码";
    		head[2] = "配件名称";
    		head[3] = "件号";
    		head[4] = "单位";
    		head[5] = "供应商名称";
    		head[6] = "制造商名称";
    		head[7] = "待结算数量";
    		head[8] = "入库数量";
    		head[9] = "退货数量";
    		head[10] = "已结算数量";
    		head[11] = "入库单号";
    		head[12] = "订单单号";
    		head[13] = "入库时间";
    		head[14] = "验收日期";
    		head[15] = "备注";
    		List<Map<String, Object>> list = dao.querySpareBalDtl(request);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[16];
    					detail[0] = CommonUtils.checkNull(map.get("CHECK_CODE"));
    					detail[1] = CommonUtils.checkNull(map
    							.get("PART_OLDCODE"));
    					detail[2] = CommonUtils.checkNull(map
    							.get("PART_CNAME"));
    					detail[3] = CommonUtils.checkNull(map
    							.get("PART_CODE"));
    					detail[4] = CommonUtils.checkNull(map
    							.get("UNIT"));
    					detail[5] = CommonUtils.checkNull(map
    							.get("VENDER_NAME"));
    					detail[6] = CommonUtils.checkNull(map.get("MAKER_NAME"));
    					detail[7] = CommonUtils.checkNull(map.get("SPARE_QTY"));
    					detail[8] = CommonUtils.checkNull(map.get("IN_QTY"));
    					detail[9] = CommonUtils.checkNull(map.get("RETURN_QTY"));
    					detail[10] = CommonUtils.checkNull(map.get("BAL_QTY"));
    					detail[11] = CommonUtils.checkNull(map.get("IN_CODE"));
    					detail[12] = CommonUtils.checkNull(map.get("ORDER_CODE"));
    					detail[13] = CommonUtils.checkNull(map.get("IN_DATE"));
    					detail[14] = CommonUtils.checkNull(map.get("CREATE_DATE"));
    					detail[15] = CommonUtils.checkNull(map.get("REMARK"));
    					list1.add(detail);
    				}
    			}
    			this.exportEx(ActionContext.getContext().getResponse(),
    					request, head, list1, "待结算明细.xls");
    		}else {
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
    		 PurchasePlanSettingDao dao1 = PurchasePlanSettingDao.getInstance();
             List<Map<String, Object>> planerList = dao1.getUserPoseLise(1, null);
             act.setOutData("planerList", planerList);
             act.setOutData("curUserId", logonUser.getUserId());
             act.setOutData("old",CommonUtils.getPreviousXMonthFirst(-3));
             act.setOutData("now",CommonUtils.getDate());
             
    		logger.error(logonUser, e1);
    		act.setException(e1);
    		act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL);
    	}
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出 结算汇总
     */
    public void exportOrderBalAllExcel() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	List wareHouses = new ArrayList();
    	try {
    		RequestWrapper request = act.getRequest();
    		String[] head = new String[5];
    		head[0] = "发票号";
    		head[1] = "配件类型";
    		head[2] = "计划金额";
    		head[3] = "采购金额";
    		List<Map<String, Object>> list = dao.queryPurOrderBalAll(request);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[5];
    					detail[0] = CommonUtils.checkNull(map.get("INVO_NO"));
    					detail[1] = CommonUtils.checkNull(map
    							.get("PART_TYPE"));
    					detail[2] = CommonUtils.checkNull(map
    							.get("IN_AMOUNT"));
    					detail[3] = CommonUtils.checkNull(map.get("BAL_AMOUNT"));
    					list1.add(detail);
    				}
    			}
    			
    		} 
    		this.exportEx(ActionContext.getContext().getResponse(),
					request, head, list1, "采购结算汇总信息.xls");
    		
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
    		act.setOutData("wareHouses", wareHouses);
    		act.setForword(PART_PURCHASEORDERBALANCE_ALL_URL);
    	}
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出 结算确认明细
     */
    public void expBalanceConfDtlExcel() {
    	ActionContext act = ActionContext.getContext();
    	AclUserBean logonUser = (AclUserBean) act.getSession().get(
    			Constant.LOGON_USER);
    	List wareHouses = new ArrayList();
    	try {
    		RequestWrapper request = act.getRequest();
    		String[] head = new String[14];
    		head[0] = "验收单号";
    		head[1] = "入库单号";
    		head[2] = "结算单号";
    		head[3] = "发票号";
    		head[4] = "配件类型";
    		head[5] = "配件编码";
    		head[6] = "配件名称";
    		head[7] = "供应商名称";
    		head[8] = "制造商名称";
    		head[9] = "结算金额（含税）";
    		head[10] = "结算金额（无税）";
    		head[11] = "结算人员";
    		head[12] = "结算时间";
    		List<Map<String, Object>> list = dao.queryBalanceConfirmDtl(request);
    		List list1 = new ArrayList();
    		if (list != null && list.size() != 0) {
    			for (int i = 0; i < list.size(); i++) {
    				Map map = (Map) list.get(i);
    				if (map != null && map.size() != 0) {
    					String[] detail = new String[14];
    					detail[0] = CommonUtils.checkNull(map.get("CHECK_CODE"));
    					detail[1] = CommonUtils.checkNull(map
    							.get("IN_CODE"));
    					detail[2] = CommonUtils
    					.checkNull(map.get("BALANCE_CODE"));
    					detail[3] = CommonUtils.checkNull(map
    							.get("INVO_NO"));
    					detail[4] = CommonUtils.checkNull(map
    							.get("PART_TYPE"));
    					detail[5] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
    					detail[6] = CommonUtils.checkNull(map.get("PART_CNAME"));
    					detail[7] = CommonUtils.checkNull(map.get("VENDER_NAME"));
    					detail[8] = CommonUtils.checkNull(map.get("MAKER_NAME"));
    					detail[9] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
    					detail[10] = CommonUtils.checkNull(map.get("IN_AMOUNT_NOTAX"));
    					detail[11] = CommonUtils.checkNull(map.get("BALANCE_NAME"));
    					detail[12] = CommonUtils.checkNull(map.get("BALANCE_DATE"));
    					list1.add(detail);
    				}
    			}
    			this.exportEx(ActionContext.getContext().getResponse(),
    					request, head, list1, "订单结算明细.xls");
    		} else {
    			wareHouses = dao.getPartWareHouseList(logonUser);//获取配件库房信息
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
    		act.setOutData("wareHouses", wareHouses);
    		act.setForword(PART_PURCHASEORDERBALANCE_CONFIRMDTL_URL);
    	}
    }
    
    
    /**
     * 
     * @Title      : 
     * @Description: 导出预计到货信息 
     * @param      :       
     * @return     :    
     * @throws     :
     * LastDate    : 2013-7-31
     */
    public void exportForeCastExcel(){
    	 ActionContext act = ActionContext.getContext();
         AclUserBean logonUser = (AclUserBean) act.getSession().get(
                 Constant.LOGON_USER);
         try {
             RequestWrapper request = act.getRequest();
             String[] head = new String[7];
             head[0] = "配件编码";
             head[1] = "配件名称";
             head[2] = "配件件号";
             head[3] = "计划数量";
             head[4] = "包装尺寸";
             head[5] = "预计到货日期";
             List<Map<String, Object>> list = dao.queryForeCastInfo(request);
             List list1 = new ArrayList();
             if (list != null && list.size() != 0) {
                 for (int i = 0; i < list.size(); i++) {
                     Map map = (Map) list.get(i);
                     if (map != null && map.size() != 0) {
                         String[] detail = new String[7];
                         detail[0] = CommonUtils
                                 .checkNull(map.get("PART_OLDCODE"));
                         detail[1] = CommonUtils.checkNull(map
                                 .get("PART_CNAME"));
                         detail[2] = CommonUtils.checkNull(map
                                 .get("PART_CODE"));
                         detail[3] = CommonUtils.checkNull(map
                        		 .get("PLAN_QTY"));
                         detail[4] = CommonUtils.checkNull(map
                        		 .get("PKG_SIZE"));
                         detail[5] = CommonUtils.checkNull(map.get("FORECAST_DATE"));
                         list1.add(detail);
                     }
                 }
                 this.exportEx(ActionContext.getContext().getResponse(),
                         request, head, list1, "配件预计到货信息.xls");
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
             act.setForword(foreCastPartQueryUrl);
         }
    }
    
    //zhumingwei add 2013-10-25 导出明细
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            
            String[] head = new String[22];
            head[0] = "序号";
            head[1] = "验收单号";
            head[2] = "结算员";
            head[3] = "配件类型";
            head[4] = "配件编码";
            head[5] = "配件名称";
            head[6] = "配件件号";
            head[7] = "入库数量";
            head[8] = "退货数量";
            head[9] = "结算数量";
            head[10] = "供应商名称";
            head[11] = "制造商名称";
            head[12] = "入库库房";
            head[13] = "入库人员";
            head[14] = "计划价";
            head[15] = "计划金额";
            head[16] = "采购价";
            head[17] = "采购金额";
            head[18] = "入库单号";
            head[19] = "结算单号";
            head[20] = "采购员";
            head[21] = "状态";
            
            List<Map<String, Object>> list = dao.queryPartPlan(request);
            List list1 = new ArrayList();

            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = new String[22];
                    detail[0] = CommonUtils.checkNull(i+1);
                    detail[1] = CommonUtils.checkNull(map.get("CHECK_CODE"));
                    detail[2] = CommonUtils.checkNull(map.get("NAME"));
                    detail[3] = CommonUtils.checkNull(map.get("PART_TYPE"));
                    detail[4] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                    detail[5] = CommonUtils.checkNull(map.get("PART_CNAME"));
                    detail[6] = CommonUtils.checkNull(map.get("PART_CODE"));
                    detail[7] = CommonUtils.checkNull(map.get("IN_QTY"));
                    detail[8] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                    detail[9] = CommonUtils.checkNull(map.get("BAL_QTY"));
                    detail[10] = CommonUtils.checkNull(map.get("VENDER_NAME"));
                    detail[11] = CommonUtils.checkNull(map.get("MAKER_NAME"));
                    detail[12] = CommonUtils.checkNull(map.get("WH_NAME"));
                    detail[13] = CommonUtils.checkNull(map.get("IN_NAME"));
                    detail[14] = CommonUtils.checkNull(map.get("PLAN_PRICE"));
                    detail[15] = CommonUtils.checkNull(map.get("PLAN_AMOUNT"));
                    detail[16] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                    detail[17] = CommonUtils.checkNull(map.get("BAL_AMOUNT"));
                    detail[18] = CommonUtils.checkNull(map.get("IN_CODE"));
                    detail[19] = CommonUtils.checkNull(map.get("BALANCE_CODE"));
                    detail[20] = CommonUtils.checkNull(map.get("BUYER"));
                    detail[21] = CommonUtils.checkNull(map.get("STATE"));
                    
                    list1.add(detail);
                }
            }

            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1,"采购订单结算明细.xls");
            act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL2);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL2);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购计划确认");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL2);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-27
     * @Title :
     * @Description: 导出
     */
    public void exportOrderBalExcel1() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        List wareHouses = new ArrayList();
        try {
            String[] head = new String[16];

            head[0] = "结算单号";
            head[1] = "配件编码";
            head[2] = "配件件号";
            head[3] = "配件名称";
            head[4] = "交货期间";
            head[5] = "结算数量";
            head[6] = "单位";
            head[7] = "无税单价";
            head[8] = "无税总金额";
            head[9] = "税费";
            head[10] = "价税合计";
            head[11] = "状态";
            head[12] = "合同号";
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            List<Map<String, Object>> list = dao.queryPartOrderBalanceList(request, curPage, Constant.PAGE_SIZE_MAX).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[16];
                        detail[0] = CommonUtils.checkNull(map.get("BALANCE_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[4] = CommonUtils.checkNull(map.get("IN_DATE"));
                        detail[5] = CommonUtils.checkNull(map.get("BAL_QTY"));
                        detail[6] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[7] = CommonUtils.checkNull(map.get("BUY_PRICE1_NOTAX"));
                        detail[8] = CommonUtils.checkNull(map.get("BAL_AMOUNT_NOTAX"));
                        detail[9] = CommonUtils.checkNull(map.get("TAX"));
                        detail[10] = CommonUtils.checkNull(map.get("BAL_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("STATE"));
                        detail[12] = CommonUtils.checkNull(map.get("CONTRACT_NUMBER"));
                        list1.add(detail);
                    }
                }
            } 
            this.exportEx(ActionContext.getContext().getResponse(),
                    request, head, list1, "采购订单结算明细" + Math.random() * 1000 + ".xls");
        } catch (Exception e) {
            String backFlag = CommonUtils.checkNull(request.getParamValue("backFlag"));
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("wareHouses", wareHouses);
            if ("1".equals(backFlag)) {
                act.setForword(PART_PURCHASEORDERBALANCE_DTL_URL);
            } else {
                act.setForword(PART_PURCHASEORDERBALANCE_QUERY_URL);
            }
        }
    }
}
