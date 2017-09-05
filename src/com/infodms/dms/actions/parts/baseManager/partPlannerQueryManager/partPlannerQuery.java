package com.infodms.dms.actions.parts.baseManager.partPlannerQueryManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartMakerRelationPO;
import com.infodms.dms.po.TtPartVenderDefinePO;
import com.infodms.dms.po.TtPartVenderPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
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
import net.sf.json.JSONArray;

/**
 * @Title: 处理配件采购属性维护业务
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2013-4-6
 *
 * @author huchao 
 * @version 1.0
 * @remark 
 */
@SuppressWarnings("unchecked")
public class partPlannerQuery extends BaseImport implements PTConstants {
	public Logger logger = Logger.getLogger(partPlannerQuery.class);
	private static final partPlannerQueryDao dao = partPlannerQueryDao.getInstance();
	
	private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/partPlannerQueryManager/inputError.jsp";//数据导入出错页面

	private String PART_PLANNER_UPDATE_TYPE = "/jsp/parts/baseManager/partPlannerQueryManager/partPlannerUpdate.jsp";
    
	/**
	 * 
	 * @Title      : 访问配件采购属性维护页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void partPlannerQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
//			List<Map<String, Object>> list = dao.getTcUser();
			String userAct = "";
			String plannerStr = "计划员";
			String purchaserStr = "采购员";
			List<Map<String, Object>> plannersList = dao.getUsers(plannerStr, userAct);
			List<Map<String, Object>> purchasersList = dao.getUsers(purchaserStr, userAct);
			List<Map<String, Object>> warnHouseList = dao.getWarnHouseList(logonUser); // 仓库列表
			act.setOutData("plannersList", JSONArray.fromObject(plannersList));
			act.setOutData("purchasersList", JSONArray.fromObject(purchasersList));
			act.setOutData("warnHouseList", JSONArray.fromObject(warnHouseList));
			act.setForword(PART_PLANNER_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购属性维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title      : 访问供应商最小包装量页面
	 * LastDate    : 2013-4-6
	 */
	public void venderSelectSetingInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("selPartId")); //配件Id
			act.setOutData("partId", partId);
			act.setForword(VENDER_SELECT_SETING_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供应商最小包装量页面跳转");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 获取制造商
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-18
	 */
	public void getMakerInfo() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //服务商Id
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId")); //服务商Id
			String makerId = CommonUtils.checkNull(request.getParamValue("makerId")); //制造商Id
			List<Map<String, Object>> makersList = dao.getMakers(venderId, partId);

			act.setOutData("makersList", makersList);
			act.setOutData("venderId", venderId);
			act.setOutData("makerId", makerId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"获取制造商失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 配件采购属性维护-查询
	 * @param      :       
	 * @return     :    json
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void partPlannerQuerySearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
//			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE")); //件号
//			String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")); //配件编码
//			String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
//			String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID")); //计划员ID
//			String isPlan = CommonUtils.checkNull(request.getParamValue("IS_PLAN")); //是否计划
//			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID")); //默认收货库房
//			String buyerId = CommonUtils.checkNull(request.getParamValue("BUYER_ID")); //采购员
//			String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE")); //配件种类
//			String ownedBase = CommonUtils.checkNull(request.getParamValue("OWNED_BASE")); //所属基地
//			String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID")); //供应商
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("partCode", CommonUtils.checkNull(request.getParamValue("PART_CODE")));//件号
			paramMap.put("partOldcode", CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")));//配件编码
			paramMap.put("partCname", CommonUtils.checkNull(request.getParamValue("PART_CNAME")));//配件名称
			paramMap.put("planerId", CommonUtils.checkNull(request.getParamValue("PLANER_ID")));//计划员ID
			paramMap.put("isPlan", CommonUtils.checkNull(request.getParamValue("IS_PLAN")));//是否计划
			paramMap.put("whId", CommonUtils.checkNull(request.getParamValue("WH_ID")));//默认收货库房
			paramMap.put("buyerId", CommonUtils.checkNull(request.getParamValue("BUYER_ID")));//采购员
			paramMap.put("produceState", CommonUtils.checkNull(request.getParamValue("PRODUCE_STATE")));//配件种类
			paramMap.put("ownedBase", CommonUtils.checkNull(request.getParamValue("OWNED_BASE")));//所属基地
			paramMap.put("produceFac", CommonUtils.checkNull(request.getParamValue("PRODUCE_FAC")));//采购方式
			paramMap.put("venderId", CommonUtils.checkNull(request.getParamValue("VENDER_ID")));//供应商
			
//			String state = CommonUtils.checkNull(request.getParamValue("STATE"));//是否有效
//			String isDirect = CommonUtils.checkNull(request.getParamValue("IS_DIRECT")); //是否直发
//			String isLack = CommonUtils.checkNull(request.getParamValue("IS_LACK"));//是否紧缺
//			String isPlan = CommonUtils.checkNull(request.getParamValue("IS_PLAN")); //是否计划
//			String oemPlan = CommonUtils.checkNull(request.getParamValue("OEM_PLAN")); //是否计划
//			String isReceive = CommonUtils.checkNull(request.getParamValue("IS_RECEIVE"));//是否领用
//			String IS_SPECIAL = CommonUtils.checkNull(request.getParamValue("IS_SPECIAL"));//是否特殊配件 //mod by zhumingwei 2013-09-16
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartPlanner(paramMap, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);		
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购属性维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * <p>
	 * Description: 配件采购方式
	 * </p>
	 */
    public void UpdateProduceWay() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String partId = request.getParamValue("partId");
            TtPartDefinePO po=new TtPartDefinePO();
            po.setPartId(Long.parseLong(partId));
            List<TtPartDefinePO> list = dao.select(po);
            TcCodePO  tc=new TcCodePO();
            tc.setType(Constant.PURCHASE_WAY.toString());
            tc.setStatus(Constant.STATUS_ENABLE);
            List<TcCodePO> list1 = dao.select(tc);
            TcCodePO  tc1=new TcCodePO();
            tc1.setType(Constant.PURCHASE_TYPE.toString());
            tc1.setStatus(Constant.STATUS_ENABLE);
            List<TcCodePO> list2 = dao.select(tc1);
            act.setOutData("TcList",list1);
            act.setOutData("TcList1",list2);
            act.setOutData("PO",list.get(0));
            act.setForword(PART_PLANNER_UPDATE_TYPE);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "修改采购属性");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title : 保存备件采购属性
     */
    public void UpdateProduceFac() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("PART_ID")); //备件Id
            String ProduceWay = CommonUtils.checkNull(request.getParamValue("WAY")); //采购方式
            String SUPERIORPURCHASING = CommonUtils.checkNull(request.getParamValue("ORG")); //上级单位
            
            String PART_BASE_PART_TYPES = CommonUtils.checkNull(request.getParamValue("PART_BASE_PART_TYPES"));
            String PART_PRODUCE_STATE = CommonUtils.checkNull(request.getParamValue("PART_PRODUCE_STATE"));
            
            TtPartDefinePO updatePo=   new TtPartDefinePO();
            TtPartDefinePO inPo=   new TtPartDefinePO();
           // TtPartDefineHistoryPO Hispo=new TtPartDefineHistoryPO();
            updatePo.setPartId(Long.parseLong(partId));
//            List<TtPartDefinePO> list = dao.select(updatePo);
           // Hispo.setPartId(Long.parseLong(partId));
           // Hispo.setPartOldcode(list.get(0).getPartOldcode());
           // Hispo.setPartCname(list.get(0).getPartCname());
           // Hispo.setPartCode(list.get(0).getPartCode());
            inPo.setProduceFac(Integer.parseInt(ProduceWay));
          //  Hispo.setProduceFac(Integer.parseInt(ProduceWay));
            inPo.setSuperiorPurchasing(Integer.parseInt(SUPERIORPURCHASING));
          //  Hispo.setSuperiorPurchasing(Integer.parseInt(SUPERIORPURCHASING));
            inPo.setUpdateDate(new Date());
          //  Hispo.setUpdateDate(new Date());
            inPo.setUpdateBy(logonUser.getUserId());
         //   Hispo.setUpdateBy(logonUser.getUserId());
            if(StringUtil.notNull(PART_BASE_PART_TYPES)){
                inPo.setPartType(Integer.parseInt(PART_BASE_PART_TYPES));
                //Hispo.setPartType(Integer.parseInt(PART_BASE_PART_TYPES));
            }
            if(StringUtil.notNull(PART_PRODUCE_STATE)){
                inPo.setProduceState(Integer.parseInt(PART_PRODUCE_STATE));
                //Hispo.setProduceState(Integer.parseInt(PART_PRODUCE_STATE));
            }
            
            dao.update(updatePo,inPo);
         //   Hispo.setDept("采购");
         //   dao.insert(Hispo);//2017-4-14屏蔽  改为将备件主数据所有属性写入历史记录表
//            PartBaseQueryDao dao1=PartBaseQueryDao.getInstance();
//            dao1.saveHistory(partId);
            act.setOutData("success", "true");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "保存备件采购属性");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
	
	/**
	 * 
	 * @Title      : 失效配件采购属性 （未启用）
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
    public void celPartPlanner(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			Integer state = Constant.STATUS_DISABLE;
			Long userId = logonUser.getUserId();//操作用户ID
			TtPartDefinePO selPo = new TtPartDefinePO();
			TtPartDefinePO updatePo = new TtPartDefinePO();
			
			selPo.setPartId(Long.parseLong(partId));
			
			updatePo.setState(state);
			updatePo.setDisableBy(userId);
			updatePo.setDisableDate(new Date());
			
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			dao.update(selPo, updatePo);
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"失效配件采购属性 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 有效配件采购属性 （未启用）
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void enablePartPlanner(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			Integer state = Constant.STATUS_ENABLE;
			Long userId = logonUser.getUserId();//操作用户ID
			TtPartDefinePO selPo = new TtPartDefinePO();
			TtPartDefinePO updatePo = new TtPartDefinePO();
			
			selPo.setPartId(Long.parseLong(partId));
			
			updatePo.setState(state);
			updatePo.setUpdateBy(userId);
			updatePo.setUpdateDate(new Date());
			
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			dao.update(selPo, updatePo);
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"有效配件采购属性");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 保存配件采购属性
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
    public void savePartPlanner(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			String planerId = CommonUtils.checkNull(request.getParamValue("planerId"));//计划员ID
			String buyerId = request.getParamValue("buyerId");//采购员ID
			String priceId = request.getParamValue("priceId");// 采购价id
			String venderId = request.getParamValue("venderId");// 供应商id
			String whId = request.getParamValue("whId");//仓库ID
//			String isPlan = request.getParamValue("isPlan");//是否计划
			String partType = request.getParamValue("partType");//配件类别
			String produceState = request.getParamValue("produceState");// 自制件/外购件
			String ownedBase = request.getParamValue("ownedBase");//所属基地
			String deliveryCycle = request.getParamValue("deliveryCycle");//交付周期
			String buyMinPkg = request.getParamValue("buyMinPkg");//最小包装量
			String minPurchase = request.getParamValue("minPurchase");//最小采购量
			
			
//			String isPlan = CommonUtils.checkNull(request.getParamValue("isPlan")); //是否计划
//			String isDirect = CommonUtils.checkNull(request.getParamValue("isDirect"));//是否直发
//			String isLack = CommonUtils.checkNull(request.getParamValue("isLack"));//是否紧缺
//			String isRecv = CommonUtils.checkNull(request.getParamValue("isRecv"));//是否领用
//			String isRecv1 = CommonUtils.checkNull(request.getParamValue("isRecv1"));//是否特殊配件  //add by zhumingwei 2013-09-16
//			String oemPlan = CommonUtils.checkNull(request.getParamValue("oemPlan"));//车厂是否计划
			
			Long userId = logonUser.getUserId();
			TtPartDefinePO selPo = new TtPartDefinePO();
			TtPartDefinePO updatePo = new TtPartDefinePO();
			
			selPo.setPartId(Long.parseLong(partId));
			// 计划员
			updatePo.setPlanerId(Long.parseLong(planerId));
			// 采购员 
			if(!CommonUtils.isEmpty(buyerId)){
			    updatePo.setBuyerId(Long.parseLong(buyerId));
			}
			// 默认收货仓库
			if(!CommonUtils.isEmpty(whId)){
			    updatePo.setWhId(Long.parseLong(whId));
			}
			// 是否计划
//			updatePo.setIsPlan(Constant.IF_TYPE_YES);
			// 配件类别
			if(!CommonUtils.isEmpty(partType)){
			    updatePo.setPartType(Integer.parseInt(partType));
			}
			// 自制件/外购件
			if(!CommonUtils.isEmpty(produceState)){
			    updatePo.setProduceState(Integer.parseInt(produceState));
			}
			// 所属基地
			if(!CommonUtils.isEmpty(ownedBase)){
			    updatePo.setOwnedBase(Integer.parseInt(ownedBase));
			}
			// 交付周期
			if(!CommonUtils.isEmpty(deliveryCycle)){
//			    updatePo.setDeliveryCycle(Integer.parseInt(deliveryCycle));
			    updatePo.setDeliverPeriod(Integer.parseInt(deliveryCycle));
			}
			// 最小包装量
			if(!CommonUtils.isEmpty(buyMinPkg)){
			    updatePo.setBuyMinPkg(Long.parseLong(buyMinPkg));
			}
			// 最小采购量
			if(!CommonUtils.isEmpty(minPurchase)){
			    updatePo.setMinPurchase(Integer.parseInt(minPurchase));
			}
			// 供应商
			if(!CommonUtils.isEmpty(venderId)){
			    // 如果采购价id值无效时
			    TtPartBuyPricePO pricePO = new TtPartBuyPricePO();
			    if(CommonUtils.isEmpty(priceId)){
			        pricePO.setPartId(selPo.getPartId());
			        pricePO.setVenderId(Long.parseLong(venderId));
			        List<TtPartBuyPricePO> priceList = dao.select(pricePO);
			        // 判断配件采购价是否已存在，如果存在就修改，不存在就新增
			        if(priceList == null || priceList.size() <= 0 ){
			            pricePO.setPriceId(Long.parseLong(SequenceManager.getSequence("")));
			            pricePO.setBuyPrice(0d);
			            pricePO.setClaimPrice(0d);
			            pricePO.setMinPackage(1l);
			            pricePO.setCreateBy(logonUser.getUserId());
			            pricePO.setCreateDate(new Date());
			            dao.insert(pricePO);
			        }
			    }else{
			        // 更新供应商
			        pricePO.setPriceId(Long.parseLong(priceId));
			        TtPartBuyPricePO uPartBuyPricePO = new TtPartBuyPricePO();
			        uPartBuyPricePO.setVenderId(Long.parseLong(venderId));
			        dao.update(pricePO, uPartBuyPricePO);
			    }
			    
			    TtPartVenderPO vePO = new TtPartVenderPO();
			    vePO.setVenderId(Long.parseLong(venderId));
			    vePO.setPartId(Long.parseLong(partId));
			    List<TtPartVenderPO> partVenderList = dao.select(vePO);
			    if(partVenderList == null || partVenderList.size() <= 0){
			        vePO.setSvId(Long.parseLong(SequenceManager.getSequence("")));
			        vePO.setState(Constant.IF_TYPE_YES);
			        vePO.setStatus(1l);
			        vePO.setCreateUser(logonUser.getUserId());
			        vePO.setCreateDate(new Date());
			        dao.insert(vePO);
			    }
			    
			}
			// 
            //mod by yuan 20130920
			/*if(null != buyerId && !"".equals(buyerId))
			{
				updatePo.setBuyerId(Long.parseLong(buyerId));
			}*/
			
			
//			updatePo.setIsDirect(Integer.parseInt(isDirect));
//			updatePo.setIsLack(Integer.parseInt(isLack));
//			updatePo.setIsReceive(Integer.parseInt(isRecv));
//			updatePo.setIsSpecial(Integer.parseInt(isRecv1));//add by zhumingwei 2013-09-16
			//updatePo.setOemPlan(Integer.parseInt(oemPlan)); mod by yuan 20130920
			updatePo.setUpdateBy(userId);
			updatePo.setUpdateDate(new Date());
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			dao.update(selPo, updatePo);
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"保存配件采购属性");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询配件供应商
	 * @param      :       
	 * @return     :    json
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void venderSearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryVender(partId, Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);		
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询配件供应商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 设置默认供应商及最小包装量
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void saveDefaultVender(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应商id
			String minPackage = CommonUtils.checkNull(request.getParamValue("minPackage"));//最小包装量
			
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId")); //新默认价格表ID
			String newMakerId = CommonUtils.checkNull(request.getParamValue("makerId")); //新默认制造商ID
			String prvPriceId = CommonUtils.checkNull(request.getParamValue("prvPriceId"));//前默认价格表ID
			String prvMakerId = CommonUtils.checkNull(request.getParamValue("prvMakerId"));//前默认制造商ID
			
			Long userId = logonUser.getUserId();
			
			TtPartBuyPricePO selPo = null;
			TtPartBuyPricePO updatePo = null;
			TtPartMakerRelationPO selMPo = null;
			TtPartMakerRelationPO updMPo = null;
			Date date = new Date();
			
			if(null != prvPriceId && !"".equals(prvPriceId))
			{
				selPo = new TtPartBuyPricePO();
				updatePo = new TtPartBuyPricePO();
				
				selPo.setPartId(Long.parseLong(partId));
				selPo.setPriceId(Long.parseLong(prvPriceId));
				
				updatePo.setIsDefault(Constant.IF_TYPE_NO);
				updatePo.setUpdateBy(userId);
				updatePo.setUpdateDate(date);
				
				dao.update(selPo, updatePo);
				
				if(null != prvMakerId && !"".equals(prvMakerId) && !"null".equals(prvMakerId))
				{
					selMPo = new TtPartMakerRelationPO();
					updMPo = new TtPartMakerRelationPO();
					
					selMPo.setPartId(Long.parseLong(partId));
					selMPo.setMakerId(Long.parseLong(prvMakerId));
					
					updMPo.setIsDefault(Constant.IF_TYPE_NO);
					updMPo.setUpdateBy(userId);
					updMPo.setUpdateDate(date);
					
					dao.update(selMPo, updMPo);
				}
				
			}
			
			selPo = new TtPartBuyPricePO();
			updatePo = new TtPartBuyPricePO();
			
			selPo.setPartId(Long.parseLong(partId));
			selPo.setPriceId(Long.parseLong(priceId));
			
			updatePo.setVenderId(Long.parseLong(venderId));
			updatePo.setMinPackage(Long.parseLong(minPackage));
			updatePo.setIsDefault(Constant.IF_TYPE_YES);
			updatePo.setUpdateBy(userId);
			updatePo.setUpdateDate(new Date());
			
			dao.update(selPo, updatePo);
			
			selMPo = new TtPartMakerRelationPO();
			updMPo = new TtPartMakerRelationPO();
			
			selMPo.setPartId(Long.parseLong(partId));
			selMPo.setMakerId(Long.parseLong(newMakerId));
			
			updMPo.setIsDefault(Constant.IF_TYPE_YES);
			updMPo.setUpdateBy(userId);
			updMPo.setUpdateDate(date);
			
			dao.update(selMPo, updMPo);
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"设置默认供应商及最小包装量失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 取消默认供应商
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void clearDefaultVender(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));//配件价格id
			String venderId = CommonUtils.checkNull(request.getParamValue("venderId"));//供应商id
			String prvMakerId = CommonUtils.checkNull(request.getParamValue("prvMakerId"));//前默认制造商ID
			
			Long userId = logonUser.getUserId();
			
			TtPartBuyPricePO selPo = new TtPartBuyPricePO();
			TtPartBuyPricePO updatePo = new TtPartBuyPricePO();
			TtPartMakerRelationPO selMPo = null;
			TtPartMakerRelationPO updMPo = null;
			Date date = new Date();
			
			selPo.setPartId(Long.parseLong(partId));
			selPo.setPriceId(Long.parseLong(priceId));
			selPo.setVenderId(Long.parseLong(venderId));
			
			updatePo.setIsDefault(Constant.IF_TYPE_NO);
			updatePo.setUpdateBy(userId);
			updatePo.setUpdateDate(date);
			
			dao.update(selPo, updatePo);
			
			if(null != prvMakerId && !"".equals(prvMakerId) && !"null".equals(prvMakerId))
			{
				selMPo = new TtPartMakerRelationPO();
				updMPo = new TtPartMakerRelationPO();
				
				selMPo.setPartId(Long.parseLong(partId));
				selMPo.setMakerId(Long.parseLong(prvMakerId));
				
				updMPo.setIsDefault(Constant.IF_TYPE_NO);
				updMPo.setUpdateBy(userId);
				updMPo.setUpdateDate(date);
				
				dao.update(selMPo, updMPo);
			}
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
			if("".equals(curPage)){
				curPage = "1";
			}
			
			act.setOutData("success", "true");
			act.setOutData("curPage", curPage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"取消默认供应商");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 批量更新配件采购属性
	 * @param      : @param relList      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-21
	 */
	public void savePartPlanner(List<Map<String,String>> relList){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			if(null != relList && relList.size() > 0)
			{
//				Long userId = logonUser.getUserId();
//				Date date = new Date();
//				TtPartDefinePO selPo = null;
//				TtPartDefinePO updatePo = null;
//				TtPartBuyPricePO selPPo = null;
//				TtPartBuyPricePO updatePPo = null;
//				TtPartMakerRelationPO selMPo = null;
//				TtPartMakerRelationPO updMPo = null;
				int listSize = relList.size();
				for(int i = 0; i < listSize; i ++ )
				{
				    Map<String, String> paramMap = relList.get(i);
				    String partId = CommonUtils.checkNull(paramMap.get("partId")); //配件Id
		            String planerId = CommonUtils.checkNull(paramMap.get("planerId"));//计划员ID
		            String buyerId =  CommonUtils.checkNull(paramMap.get("buyerId"));//采购员ID
//		            String priceId = paramMap.get("priceId");//采购ID
		            String venderId =  CommonUtils.checkNull(paramMap.get("venderId"));// 供应商id
		            String whId =  CommonUtils.checkNull(paramMap.get("whId"));//仓库ID
//		            String isPlan =  CommonUtils.checkNull(paramMap.get("isPlan"));//是否计划
		            String partType =  CommonUtils.checkNull(paramMap.get("partType"));//配件类别
		            String ownedBase =  CommonUtils.checkNull(paramMap.get("ownedBase"));//所属基地
		            String deliveryCycle =  CommonUtils.checkNull(paramMap.get("deliveryCycle"));//交付周期
		            String minPack1 =  CommonUtils.checkNull(paramMap.get("minPack1"));//最小包装量
		            String minPurchase =  CommonUtils.checkNull(paramMap.get("minPurchase"));//最小采购量
		            
		            
//		          String isPlan = CommonUtils.checkNull(request.getParamValue("isPlan")); //是否计划
//		          String isDirect = CommonUtils.checkNull(request.getParamValue("isDirect"));//是否直发
//		          String isLack = CommonUtils.checkNull(request.getParamValue("isLack"));//是否紧缺
//		          String isRecv = CommonUtils.checkNull(request.getParamValue("isRecv"));//是否领用
//		          String isRecv1 = CommonUtils.checkNull(request.getParamValue("isRecv1"));//是否特殊配件  //add by zhumingwei 2013-09-16
//		          String oemPlan = CommonUtils.checkNull(request.getParamValue("oemPlan"));//车厂是否计划
		            
		            Long userId = logonUser.getUserId();
		            TtPartDefinePO selPo = new TtPartDefinePO();
		            TtPartDefinePO updatePo = new TtPartDefinePO();
		            
		            selPo.setPartId(Long.parseLong(partId));
		            // 计划员
		            updatePo.setPlanerId(Long.parseLong(planerId));
		            // 采购员 
		            if(!CommonUtils.isEmpty(buyerId)){
		                updatePo.setBuyerId(Long.parseLong(buyerId));
		            }
		            // 默认收货仓库
		            if(!CommonUtils.isEmpty(whId)){
		                updatePo.setWhId(Long.parseLong(whId));
		            }
		            // 是否计划
//		            updatePo.setIsPlan(Integer.parseInt(isPlan));
		            // 配件类别
		            if(!CommonUtils.isEmpty(partType)){
		                updatePo.setPartType(Integer.parseInt(partType));
		                updatePo.setProduceState(Integer.parseInt(partType));
		            }
		            // 采购方式
		            if(!CommonUtils.isEmpty(ownedBase)){
		                updatePo.setOwnedBase(Integer.parseInt(ownedBase));
		                Integer sp = null;
		                int obj = Integer.parseInt(ownedBase);
		                if(obj==92811001){
		                    // 92811001 客服采购     
		                    // 97141006  客服公司
		                    sp=97141006;
		                }else if(obj==92811002 || obj==92811003 || obj==92811004){
		                    // 92811002 三工厂采购 
		                    // 92811003 四工厂采购 
		                    // 92811004 五工厂采购 
		                    // 97141003 股份公司
		                    sp=97141003;
		                }else if(obj==92811005){
		                    // 92811005 北京采购 
		                    // 97141001 北京工厂
		                    sp=97141001;
		                }else if(obj==92811006){
		                    // 92811006 合肥采购 
		                    // 97141004 合肥工厂
		                    sp=97141004;
		                }else if(obj==92811007){
		                    // 92811007 铃木采购 
		                    // 97141007 铃木工厂
		                    sp=97141007;
		                }else if(obj==92811008){
		                    // 92811008 河北采购 
		                    // 97141005 河北工厂
		                    sp=97141005;
		                }else if(obj==92811009){
		                    // 92811009 福特采购 
		                    // 97141002 福特工厂
		                    sp=97141002;
		                }else if(obj==92811010){
		                    // 92811010 东安动力 
		                    // 97141009 东安动力
		                    sp=97141009;
		                }else if(obj==92811011){
		                    // 92811011 东安三菱 
		                    // 97141010 东安三菱
		                    sp=97141010;
		                }else if(obj==92811012){
		                    // 92811012 南京采购 
		                    // 97141008 南京工厂
		                    sp=97141008;
		                }else if(obj==92811013){
		                    // 92811013 新能源采购
		                    // 97141011 新能源公司
		                    sp=97141011;
		                }else if(obj==92811014){
		                    // 92811014 江铃采购
		                    // 97141012 江铃工厂
		                    sp=97141012;
		                }
		                updatePo.setProduceFac(obj);
		                updatePo.setSuperiorPurchasing(sp);
		            }
		            // 交付周期
		            if(!CommonUtils.isEmpty(deliveryCycle)){
		                updatePo.setDeliveryCycle(Integer.parseInt(deliveryCycle));
		            }
		            // 最小包装量
		            if(!CommonUtils.isEmpty(minPack1)){
		                updatePo.setMinPack1(Long.parseLong(minPack1));
		            }
		            // 最小采购量
		            if(!CommonUtils.isEmpty(minPurchase)){
		                updatePo.setMinPurchase(Integer.parseInt(minPurchase));
		            }
		            // 供应商
		            if(!CommonUtils.isEmpty(venderId)){
		                // 如果采购价id值无效时
		                TtPartBuyPricePO pricePO = new TtPartBuyPricePO();
	                    pricePO.setPartId(selPo.getPartId());
	                    pricePO.setVenderId(Long.parseLong(venderId));
	                    List<TtPartBuyPricePO> priceList = dao.select(pricePO);
	                    // 判断配件采购价是否已存在，如果存在就修改，不存在就新增
	                    if(priceList == null || priceList.size() <= 0 ){
	                        pricePO.setPriceId(Long.parseLong(SequenceManager.getSequence("")));
	                        pricePO.setBuyPrice(0d);
	                        pricePO.setClaimPrice(0d);
	                        pricePO.setMinPackage(1l);
	                        pricePO.setCreateBy(logonUser.getUserId());
	                        pricePO.setCreateDate(new Date());
	                        dao.insert(pricePO);
		                }
		                
		            }
		            updatePo.setUpdateBy(userId);
		            updatePo.setUpdateDate(new Date());
		            dao.update(selPo, updatePo);
		            act.setOutData("success", "true");
				    
				    
//					String partId = relList.get(i).get("partId"); //配件Id
//					String planerId = relList.get(i).get("planerId");//计划员ID
//					String buyerId = relList.get(i).get("buyerId");//采购员ID
//					String isPlan = relList.get(i).get("isPlan");//是否计划
//					String isDirect = relList.get(i).get("isDirect");//是否直发
//					String isLack = relList.get(i).get("isLack");//是否紧缺
//					String oemPlan = relList.get(i).get("oemPlan");//车厂是否计划
////					String venderCode = relList.get(i).get("venderCode");//供应商Code
////					String makerCode = relList.get(i).get("makerCode");//制造商Code
////					String minPackage = relList.get(i).get("minPackage");//最小包装量
//					String isRecv = relList.get(i).get("isRecv");//是否领用
//					
//					/*String vSql = " AND V.VENDER_CODE = '"+ venderCode +"' ";
//					List<Map<String,Object>> vList = dao.checkVenderAccount(vSql);
//					String venderId = "0";
//					if(null != vList && vList.size() == 1)
//					{
//						venderId = vList.get(0).get("VENDER_ID").toString();
//					}
//					
//					String mSql = " AND M.MAKER_CODE = '"+ makerCode +"' ";
//					List<Map<String,Object>> mList = dao.checkMakerAccount(mSql);
//					String makerId = "0";
//					if(null != mList && mList.size() == 1)
//					{
//						makerId = mList.get(0).get("MAKER_ID").toString();
//					}*/
//					
//					selPo = new TtPartDefinePO();
//					updatePo = new TtPartDefinePO();
//					
//					selPo.setPartId(Long.parseLong(partId));
//					
//					if(!"".equals(planerId))
//					{
//						updatePo.setPlanerId(Long.parseLong(planerId));
//					}
//					if(!"".equals(buyerId))
//					{
//						updatePo.setBuyerId(Long.parseLong(buyerId));
//					}
//					if(!"".equals(isPlan))
//					{
//						updatePo.setIsPlan(Integer.parseInt(isPlan));
//					}
//					if(!"".equals(isDirect))
//					{
//						updatePo.setIsDirect(Integer.parseInt(isDirect));
//					}
//					if(!"".equals(isLack))
//					{
//						updatePo.setIsLack(Integer.parseInt(isLack));
//					}
//					if(!"".equals(isRecv))
//					{
//						updatePo.setIsReceive(Integer.parseInt(isRecv));
//					}
//					if(!"".equals(oemPlan))
//					{
//						updatePo.setOemPlan(Integer.parseInt(oemPlan));
//					}
//					
//					updatePo.setUpdateBy(userId);
//					updatePo.setUpdateDate(date);
//					
//					dao.update(selPo, updatePo);
					
					/*selPPo = new TtPartBuyPricePO();
					updatePPo = new TtPartBuyPricePO();
					
					selPPo.setPartId(Long.parseLong(partId));
					
					updatePPo.setIsDefault(Constant.IF_TYPE_NO);
					updatePPo.setUpdateBy(userId);
					updatePPo.setUpdateDate(date);
					
					dao.update(selPPo, updatePPo);
					
					selPPo = new TtPartBuyPricePO();
					updatePPo = new TtPartBuyPricePO();
					
					selPPo.setPartId(Long.parseLong(partId));
					selPPo.setVenderId(Long.parseLong(venderId));
					
					updatePPo.setIsDefault(Constant.IF_TYPE_YES);
					updatePPo.setMinPackage(Long.parseLong(minPackage));
					updatePPo.setUpdateBy(userId);
					updatePPo.setUpdateDate(date);
					
					dao.update(selPPo, updatePPo);
					
					selMPo = new TtPartMakerRelationPO();
					updMPo = new TtPartMakerRelationPO();
					
					selMPo.setPartId(Long.parseLong(partId));
					
					updMPo.setIsDefault(Constant.IF_TYPE_NO);
					updMPo.setUpdateBy(userId);
					updMPo.setUpdateDate(date);
					
					dao.update(selMPo, updMPo);
					
					selMPo = new TtPartMakerRelationPO();
					updMPo = new TtPartMakerRelationPO();
					
					selMPo.setPartId(Long.parseLong(partId));
					selMPo.setMakerId(Long.parseLong(makerId));
					
					updMPo.setIsDefault(Constant.IF_TYPE_YES);
					updMPo.setUpdateBy(userId);
					updMPo.setUpdateDate(date);
					
					dao.update(selMPo, updMPo);*/
					
				}
			}
			
			partPlannerQueryInit();
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"批量更新配件采购属性失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 配件采购属性维护-> 导入文件
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-21
	 */
	public void purProUpload(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
//			String parentOrgId = "";//父机构（销售单位）ID
//			//判断主机厂与服务商
//			String comp = logonUser.getOemCompanyId();
//			if (null == comp ){
//				
//				parentOrgId = Constant.OEM_ACTIVITIES;
//			}else {
//				parentOrgId = logonUser.getDealerId();
//			}
			List<Map<String,String>> errorInfo = null;
			String err="";
			
			errorInfo =  new ArrayList<Map<String,String>>();
			long maxSize=1024*1024*5;
			int errNum = insertIntoTmp(request, "uploadFile", 12,3,maxSize);
			
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于三行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于!";
					break;
				default:
					break;
				}
			}
			
			if(!"".equals(err)){
				act.setOutData("error", err);
				act.setForword(INPUT_ERROR_URL);
			}else{
				List<Map> list= getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo);
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					act.setForword(INPUT_ERROR_URL);
				}else{
					//保存
					savePartPlanner(voList);
				}
				
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 读取CELL
	 * @param      : @param voList
	 * @param      : @param list
	 * @param      : @param errorInfo      
	 * @return     :    
	 * LastDate    : 2013-4-12
	 */
    @SuppressWarnings("rawtypes")
    private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo){
		if(null==list){
			list=new ArrayList();
		}
		String plannerStr = "计划员";
		String purchaserStr = "采购员";
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while (it.hasNext()) {
			    int cellIndex = 0;
			    int cellLength = 0;
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				String partIdTmp = "";
				Map<String, String> tempmap = new HashMap<String, String>();
				// 配件编码
				if ("".equals(cells[cellIndex].getContents().trim())) {
					Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "配件编码");
					errormap.put("3", "为空!");
					errorInfo.add(errormap);
                    return;
				} else {
					List<Map<String, Object>> partCheck = dao.checkOldCode(cells[cellIndex].getContents().trim().toUpperCase());
					if (partCheck.size() == 1) {
						partIdTmp = partCheck.get(0).get("PART_ID").toString();
						tempmap.put("partId", partCheck.get(0).get("PART_ID").toString());
						tempmap.put("partoldCode", partCheck.get(0).get("PART_OLDCODE").toString());
					} else {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件编码【" + cells[cellIndex].getContents().trim()+"】");
						errormap.put("3", "不存在!");
						errorInfo.add(errormap);
                        return;
					}
				}
				// 计划员
				cellIndex++; cellLength++; 
				if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
					/*Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "计划员账号");
					errormap.put("3", "为空!");
					errorInfo.add(errormap);*/
					tempmap.put("planerId", "");
				} else {
					String plaAcct = cells[cellIndex].getContents().trim();
					/*StringBuffer sb = new StringBuffer();
					sb.append(" AND U.ACNT = '" + plaAcct.toUpperCase() + "' ");
					List<Map<String, Object>> actCheck = dao.checkUserAccount(sb.toString());*/
					List<Map<String, Object>> actCheck = dao.getUsers(plannerStr, plaAcct.toUpperCase());
					if(actCheck.size() == 1)
					{
						tempmap.put("planerId", actCheck.get(0).get("USER_ID").toString());
					}
					else
					{
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "计划员账号【"+ plaAcct +"】");
						errormap.put("3", "无效!");
						errorInfo.add(errormap);
                        return;
					}
				}
				// 采购员
				cellIndex++; cellLength++; 
				if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
				    /*Map<String, String> errormap = new HashMap<String, String>();
					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
					errormap.put("2", "计划员账号");
					errormap.put("3", "为空!");
					errorInfo.add(errormap);*/
				    tempmap.put("planerId", "");
				} else {
				    String plaAcct = cells[cellIndex].getContents().trim();
				    /*StringBuffer sb = new StringBuffer();
					sb.append(" AND U.ACNT = '" + plaAcct.toUpperCase() + "' ");
					List<Map<String, Object>> actCheck = dao.checkUserAccount(sb.toString());*/
				    List<Map<String, Object>> actCheck = dao.getUsers(purchaserStr, plaAcct.toUpperCase());
				    if(actCheck.size() == 1)
				    {
				        tempmap.put("buyerId", actCheck.get(0).get("USER_ID").toString());
				    } else {
				        Map<String, String> errormap = new HashMap<String, String>();
				        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
				        errormap.put("2", "计划员账号【"+ plaAcct +"】");
				        errormap.put("3", "无效!");
				        errorInfo.add(errormap);
                        return;
				    }
				}
				// 供应商
				cellLength+=2; 
                if (cells.length < 5) {
                    tempmap.put("venderId", "");
                }else{
                    cellIndex++;
                    String venderCode = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    cellIndex++;
                    String venderName = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if(CommonUtils.isEmpty(venderCode) || CommonUtils.isEmpty(venderName)){
                        tempmap.put("venderId", "");
                    }else{
                        TtPartVenderDefinePO venderDefinePO = new TtPartVenderDefinePO();
                        venderDefinePO.setVenderCode(venderCode);
                        venderDefinePO.setVenderName(venderName);
                        List<TtPartVenderDefinePO> venderList = dao.select(venderDefinePO);
                        if(venderList == null || venderList.size() < 1){
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "供应商不存在");
                            errormap.put("3", "无效!");
                            errorInfo.add(errormap);
                            return;
                        }else{
                            tempmap.put("venderId", venderList.get(0).getVenderId().toString());
                            
                        }
                        
                    }
                }
				
                // 默认收货库房
                cellLength+=2; 
                if (cells.length < 7) {
                    tempmap.put("whId", "");
                }else{
                    cellIndex++;
                    String whCode = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    cellIndex++;
                    String whName = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if(CommonUtils.isEmpty(whCode) || CommonUtils.isEmpty(whName)){
                        tempmap.put("venderId", "");
                    }else{
                        TtPartWarehouseDefinePO whPO = new TtPartWarehouseDefinePO();
                        whPO.setWhCode(whCode);
                        whPO.setWhName(whName);
                        List<TtPartWarehouseDefinePO> whList = dao.select(whPO);
                        if(whList == null || whList.size() < 1){
                            Map<String, String> errormap = new HashMap<String, String>();
                            errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                            errormap.put("2", "收货仓库不存在");
                            errormap.put("3", "无效!");
                            errorInfo.add(errormap);
                            return;
                        }else{
                            tempmap.put("whId", whList.get(0).getWhId().toString());
                        }
                        
                    }
                }
                
                // 是否计划,1:是，0：否
//                cellIndex++;
//                if (cells.length < 8 || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
//                    tempmap.put("isPlan", "");
//                }else{
//                    String isPlan = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
//                    if("1".equals(isPlan)){
//                        tempmap.put("isPlan", Constant.IF_TYPE_YES.toString());
//                    }else if("0".equals(isPlan)){
//                        tempmap.put("isPlan", Constant.IF_TYPE_NO.toString());
//                    }else{
//                        tempmap.put("isPlan", "");
//                    }
//                }
                
                // 配件种类(1:自制；2:配套)
                cellIndex++; cellLength++; 
                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
                    tempmap.put("partType", "");
                }else{
                    String partType = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if("1".equals(partType)){
                        tempmap.put("partType", Constant.PART_PRODUCE_STATE_01.toString());
                    }else if("2".equals(partType)){
                        tempmap.put("partType", Constant.PART_PRODUCE_STATE_02.toString());
                    }else{
                        tempmap.put("partType", "");
                    }
                }
                
                // 上级采购单位
//                cellIndex++; cellLength++; 
//                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
////                    tempmap.put("", "");
//                }else{
//                    
//                }
                
                // 所属基地
                cellIndex++; cellLength++; 
                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
                    tempmap.put("ownedBase", "");
                }else{
                    String ownedBase = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    TcCodePO tcCodePO = new TcCodePO();
                    tcCodePO.setCodeDesc(ownedBase);
                    List<TcCodePO> tcCodePOs = dao.select(tcCodePO);
                    if(tcCodePO != null && tcCodePOs.size() == 1){
                        tempmap.put("ownedBase", tcCodePOs.get(0).getCodeId());
                    }else{
                        tempmap.put("ownedBase", "");
                    }
                }
                
                // 交付周期
                cellIndex++; cellLength++; 
                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
                    tempmap.put("deliveryCycle", "");
                }else{
                    String deliveryCycle = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if(Pattern.matches("^-?\\d+$", deliveryCycle)){
                        tempmap.put("deliveryCycle", deliveryCycle);
                    }else{
                        tempmap.put("deliveryCycle", "");
                    }
                }
                
                // 最小包装量
                cellIndex++; cellLength++; 
                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
                    tempmap.put("minPack1", "1");
                }else{
                    String minPack1 = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if(Pattern.matches("^-?\\d+$", minPack1)){
                        tempmap.put("minPack1", minPack1);
                    }else{
                        tempmap.put("minPack1", "1");
                    }
                }
                
                // 最小采购量
                cellIndex++; cellLength++; 
                if (cells.length < cellLength || CommonUtils.isEmpty(cells[cellIndex].getContents())) {
                    tempmap.put("minPurchase", "1");
                }else{
                    String minPurchase = CommonUtils.checkNull(cells[cellIndex].getContents()).trim();
                    if(Pattern.matches("^-?\\d+$", minPurchase)){
                        tempmap.put("deliveryCycle", minPurchase);
                    }else{
                        tempmap.put("minPurchase", "1");
                    }
                }
                voList.add(tempmap);
                
                
//				/*if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
//					Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "采购员账号");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);
//					tempmap.put("buyerId", "");
//				} else {
//					String buyAcct = cells[2].getContents().trim();
//					StringBuffer sb = new StringBuffer();
//					sb.append(" AND U.ACNT = '" + buyAcct.toUpperCase() + "' ");
//					
//					List<Map<String, Object>> actCheck = dao.checkUserAccount(sb.toString());
//					
//					
//					List<Map<String, Object>> actCheck = dao.getUsers(purchaserStr, buyAcct.toUpperCase());
//					
//					if(actCheck.size() == 1)
//					{
//						tempmap.put("buyerId", actCheck.get(0).get("USER_ID").toString());
//					}
//					else
//					{
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "采购员账号【" + buyAcct + "】");
//						errormap.put("3", "无效!");
//						errorInfo.add(errormap);
//					}
//				}*/
//				
//				String yesStr = "1";
//				
//				tempmap.put("oemPlan", "");
//				
//				/*if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
//					Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "是否车厂计划");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);
//					tempmap.put("oemPlan", "");
//					
//				} else {
//					String oemPlanStr = cells[3].getContents().trim();
//					if(yesStr.equals(oemPlanStr))
//					{
//						tempmap.put("oemPlan", Constant.IF_TYPE_YES + "");
//					}
//					else
//					{
//						tempmap.put("oemPlan", Constant.IF_TYPE_NO + "");
//					}
//					
//				}*/
//				
//				String isPlan = "";
//				if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
//					/*Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "是否计划");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);*/
//					tempmap.put("isPlan", "");
//				} else {
//					String isPlanStr = cells[2].getContents().trim();
//					isPlan = isPlanStr;
//					if(yesStr.equals(isPlanStr))
//					{
//						tempmap.put("isPlan", Constant.IF_TYPE_YES + "");
//						tempmap.put("isDirect", Constant.IF_TYPE_NO + "");
//					}
//					else
//					{
//						tempmap.put("isPlan", Constant.IF_TYPE_NO + "");
//						tempmap.put("isDirect", Constant.IF_TYPE_YES + "");
//					}
//					
//				}
//				
//				if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
//					/*Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "是否直发");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);*/
//					tempmap.put("isDirect", "");
//				} else {
//					String isDirStr = cells[3].getContents().trim();
//					if(!isPlan.equals(isDirStr))
//					{
//						if(yesStr.equals(isDirStr))
//						{
//							tempmap.put("isDirect", Constant.IF_TYPE_YES + "");
//							tempmap.put("isPlan", Constant.IF_TYPE_NO + "");
//						}
//						else
//						{
//							tempmap.put("isDirect", Constant.IF_TYPE_NO + "");
//							tempmap.put("isPlan", Constant.IF_TYPE_YES + "");
//						}
//					}
//					else
//					{
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "是否直发与是否计划");
//						errormap.put("3", "值不能相同!");
//						errorInfo.add(errormap);
//					}
//					
//				}
//				
//				if (cells.length < 5 || CommonUtils.isEmpty(cells[4].getContents())) {
//					/*Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "是否紧缺件");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);*/
//					tempmap.put("isLack", "");
//				} else {
//					String isLack = cells[4].getContents().trim();
//					if(yesStr.equals(isLack))
//					{
//						tempmap.put("isLack", Constant.IF_TYPE_YES + "");
//					}
//					else
//					{
//						tempmap.put("isLack", Constant.IF_TYPE_NO + "");
//					}
//				}
//				
//				if (cells.length < 6 || CommonUtils.isEmpty(cells[5].getContents())) {
//					/*Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "是否领用");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);*/
//					tempmap.put("isRecv", "");
//				} else {
//					String isRecv = cells[5].getContents().trim();
//					if(yesStr.equals(isRecv))
//					{
//						tempmap.put("isRecv", Constant.IF_TYPE_YES + "");
//					}
//					else
//					{
//						tempmap.put("isRecv", Constant.IF_TYPE_NO + "");
//					}
//				}
//				
//				/*if (cells.length < 9 || CommonUtils.isEmpty(cells[8].getContents())) {
//					Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "默认供应商编码");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);
//				} else {
//					String venderCode = cells[8].getContents().trim();
//					String sqlStr = " AND V.VENDER_CODE = '" + venderCode + "' ";
//					List<Map<String,Object>> vList = dao.checkVenderAccount(sqlStr);
//					if(null != vList && vList.size() == 1)
//					{
//						String partOldCode = cells[0].getContents().trim();
//						if(!"".equals(partOldCode))
//						{
//							StringBuffer sbSql = new StringBuffer();
//							sbSql.append(" AND T.PART_OLDCODE = '"+ partOldCode +"' ");
//							sbSql.append(" AND V.VENDER_CODE = '"+ venderCode +"' ");
//							List<Map<String,Object>> pvList = dao.checkPartVender(sbSql.toString());
//							if(null != pvList && pvList.size() == 1)
//							{
//								tempmap.put("venderCode", venderCode);
//							}
//							else
//							{
//								Map<String, String> errormap = new HashMap<String, String>();
//								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//								errormap.put("2", "配件编码【"+partOldCode+"】、供应商编码【"+venderCode+"】");
//								errormap.put("3", "无效关系!");
//								errorInfo.add(errormap);
//							}
//						}
//					}
//					else
//					{
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "默认供应商编码【"+venderCode+"】");
//						errormap.put("3", "无效或不存在!");
//						errorInfo.add(errormap);
//					}
//				}
//				
//				if (cells.length < 10 || CommonUtils.isEmpty(cells[9].getContents())) {
//					Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "默认制造商编码");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);
//					tempmap.put("makerCode", "-1");
//				} else {
//					String makerCode = cells[9].getContents().trim();
//					String sqlStr = " AND M.MAKER_CODE = '" + makerCode + "' ";
//					List<Map<String,Object>> mList = dao.checkMakerAccount(sqlStr);
//					if(null != mList && mList.size() == 1)
//					{
//						String partOldCode = cells[0].getContents().trim();
//						String venderCode = cells[8].getContents().trim();
//						if(!"".equals(partOldCode) && !"".equals(venderCode))
//						{
//							List<Map<String,Object>> pvmList = dao.checkPartVenderMaker(partOldCode, venderCode, makerCode);
//							if(null != pvmList && pvmList.size() == 1)
//							{
//								tempmap.put("makerCode", makerCode);
//							}
//							else
//							{
//								Map<String, String> errormap = new HashMap<String, String>();
//								errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//								errormap.put("2", "配件编码【"+partOldCode+"】、供应商编码【"+venderCode+"】和制造商编码【"+makerCode+"】");
//								errormap.put("3", "无效关系!");
//								errorInfo.add(errormap);
//							}
//						}
//					}
//					else
//					{
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "默认制造商编码【"+makerCode+"】");
//						errormap.put("3", "无效或不存在!");
//						errorInfo.add(errormap);
//					}
//				}
//				
//				if (cells.length < 11 || CommonUtils.isEmpty(cells[10].getContents())) {
//					Map<String, String> errormap = new HashMap<String, String>();
//					errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//					errormap.put("2", "最小包装量");
//					errormap.put("3", "为空!");
//					errorInfo.add(errormap);
//				} else {
//					String minPkStr = cells[10].getContents().trim();
//					String regex = "(^[1-9]+(\\d)*$)";
//					Pattern pattern = Pattern.compile(regex);
//					Matcher matcher = pattern.matcher(minPkStr);
//					
//					if(matcher.find())
//					{
//						tempmap.put("minPackage", minPkStr);
//					}
//					else
//					{
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "最小包装量");
//						errormap.put("3", "不合法!");
//						errorInfo.add(errormap);
//					}
//				}*/
//				
//				voList.add(tempmap);
			}
		}
	}
	
	/**
	 * 
	 * @Title      : 导出配件采购属批量修改模板
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-6
	 */
	public void exportExcelTemplate(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("配件编码(必填)");
			listHead.add("计划员账号");
			listHead.add("采购员账号");
			listHead.add("供应商编码");
			listHead.add("供应商名称");
			listHead.add("默认收货库房编码");
			listHead.add("默认收货库房名称");
//			listHead.add("是否计划(1:是；0：否)");
			listHead.add("配件种类(1:自制；2:配套)");
//			listHead.add("上级采购单位");
			listHead.add("采购方式");
			listHead.add("交付周期");
			listHead.add("最小包装量(默认1)");
			listHead.add("最小采购量(默认1)");
			
//			listHead.add("车厂是否计划(1:是；0：否)");
//			listHead.add("是否直发(1:是；0：否)");
//			listHead.add("是否紧缺件(1:是；0：否) ");
//			listHead.add("是否领用(1:是；0：否) ");
//			listHead.add("默认供应商编码 ");
//			listHead.add("默认制造商编码 ");
//			listHead.add("最小包装量 ");
			list.add(listHead);
			// 导出的文件名
			String fileName = "配件采购属批量更新模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"导出配件采购属批量修改模板错误");
			logger.error(logonUser,e1);
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
	 * 
	 * @Title      : 导出配件采购属批量修改模板 (含配件编码)
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-24
	 */
	public void expPartPurProTempExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
		    Map<String, String> paramMap = new HashMap<String, String>();
			
			String[] head = new String[11];
			head[0] = "配件编码";
			head[1] = "计划员账号";
//			head[2] = "采购员账号";
//			head[3] = "车厂是否计划(1:是；0：否)";
			head[2] = "是否计划(1:是；0：否)";
			head[3] = "是否直发(1:是；0：否)";
			head[4] = "是否紧缺件(1:是；0：否)";
			head[5] = "是否领用(1:是；0：否)";
			
			List<Map<String, Object>> list = dao.partPurchaseList(paramMap);
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[11];
						detail[0] = CommonUtils.checkNull(map
								.get("PART_OLDCODE"));
						detail[1] = "";
						detail[2] = "";
						detail[3] = "";
						detail[4] = "";
						detail[5] = "";
						list1.add(detail);
					}
				}
			}
			
			String fileName = "配件采购属批量更新模板";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件采购属性Excel");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 导出配件采购属性Excel
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-21
	 */
	public void expPartPurProExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
		try {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("partCode", CommonUtils.checkNull(request.getParamValue("PART_CODE")));//件号
            paramMap.put("partOldcode", CommonUtils.checkNull(request.getParamValue("PART_OLDCODE")));//配件编码
            paramMap.put("partCname", CommonUtils.checkNull(request.getParamValue("PART_CNAME")));//配件名称
            paramMap.put("planerId", CommonUtils.checkNull(request.getParamValue("PLAN_ID")));//计划员ID
//            paramMap.put("isPlan", CommonUtils.checkNull(request.getParamValue("IS_PLAN")));//是否计划
            paramMap.put("whId", CommonUtils.checkNull(request.getParamValue("WH_ID")));//默认收货库房
            paramMap.put("buyerId", CommonUtils.checkNull(request.getParamValue("BUYER_ID")));//采购员
            paramMap.put("partType", CommonUtils.checkNull(request.getParamValue("PART_TYPE")));//配件种类
            paramMap.put("ownedBase", CommonUtils.checkNull(request.getParamValue("OWNED_BASE")));//所属基地
            paramMap.put("venderId", CommonUtils.checkNull(request.getParamValue("VENDER_ID")));//供应商
            paramMap.put("decodeSql", "true");
            // 序号   操作  配件编码    配件名称    件号  供应商 计划员 采购员 默认收货库房  是否计划    配件种类    上级采购单位  所属基地    交付周期    最小包装量   最小采购量
			List<Object> head = new LinkedList<Object>();
			head.add("序号");
			head.add("配件编码");
			head.add("配件名称");
			head.add("件号");
			head.add("供应商");
			head.add("计划员");
			head.add("采购员");
			head.add("默认收货库房");
//			head.add("是否计划");
			head.add("配件种类");
			head.add("上级采购单位");
			head.add("采购方式");
			head.add("交付周期");
			head.add("最小包装量");
			head.add("最小采购量");
//			String[] head = new String[15];
//			head[0] = "序号";
//			head[1] = "配件编码";
//			head[2] = "配件名称";
//			head[3] = "件号";
//			head[4] = "计划员";
//			head[5] = "采购员";
//			head[5] = "是否计划";
//			head[6] = "是否直发";
//			head[7] = "是否紧缺件";
//			head[8] = "是否领用";
//			head[9] = "是否有效";
			
			List<Map<String, Object>> dataList = dao.partPurchaseList(paramMap);
			List<List<Object>> expExcelList = new LinkedList<List<Object>>();
			expExcelList.add(head);
			if (dataList != null && dataList.size() != 0) {
				for (int i = 0; i < dataList.size(); i++) {
					Map map = (Map) dataList.get(i);
					if (map != null && map.size() != 0) {
					    List<Object> rowList = new LinkedList<Object>();
					    rowList.add(i+1);
					    rowList.add(CommonUtils.checkNull(map.get("PART_OLDCODE")));
					    rowList.add(CommonUtils.checkNull(map.get("PART_CNAME")));
					    rowList.add(CommonUtils.checkNull(map.get("PART_CODE")));
					    rowList.add(CommonUtils.checkNull(map.get("VENDER_NAME")));
					    rowList.add(CommonUtils.checkNull(map.get("PLANER_NAME")));
					    rowList.add(CommonUtils.checkNull(map.get("BUYER_NAME")));
					    rowList.add(CommonUtils.checkNull(map.get("WH_NAME")));
//					    rowList.add(CommonUtils.checkNull(map.get("IS_PLAN_DESC")));
					    rowList.add(CommonUtils.checkNull(map.get("PRODUCE_STATE_DESC")));
					    rowList.add(CommonUtils.checkNull(map.get("SP_DESC")));
					    rowList.add(CommonUtils.checkNull(map.get("PRODUCE_FAC_NAME")));
					    rowList.add(CommonUtils.checkNull(map.get("DELIVERY_CYCLE")));
					    rowList.add(CommonUtils.checkNull(map.get("MIN_PACK1")));
					    rowList.add(CommonUtils.checkNull(map.get("MIN_PURCHASE")));
                        expExcelList.add(rowList);
//						String[] detail = new String[15];
//						detail[0] = CommonUtils.checkNull(i+1);
//						detail[3] = CommonUtils.checkNull(map
//								.get("PART_CODE"));
//						detail[1] = CommonUtils.checkNull(map
//								.get("PART_OLDCODE"));
//						detail[2] = CommonUtils.checkNull(map
//								.get("PART_CNAME"));
//						detail[4] = CommonUtils.checkNull(map
//								.get("P_NAME"));
//						/*detail[5] = CommonUtils.checkNull(map
//								.get("B_NAME"));*/
//						int isPlanValue = 0;
//						int isDirValue = 0;
//						int isLackValue = 0;
//						int isRecValue = 0;
//						int stateValue = 0;
//						/*int oemPlanValue = 0;
//						if(null != map.get("OEM_PLAN") && !"".equals(map.get("OEM_PLAN")))
//						{
//							oemPlanValue = Integer.parseInt(map.get("OEM_PLAN").toString());
//						}*/
//						if(null != map.get("IS_PLAN") && !"".equals(map.get("IS_PLAN")))
//						{
//							isPlanValue = Integer.parseInt(map.get("IS_PLAN").toString());
//						}
//						if(null != map.get("IS_DIRECT") && !"".equals(map.get("IS_DIRECT")))
//						{
//							isDirValue = Integer.parseInt(map.get("IS_DIRECT").toString());
//						}
//						if(null != map.get("IS_LACK") && !"".equals(map.get("IS_LACK")))
//						{
//							isLackValue = Integer.parseInt(map.get("IS_LACK").toString());
//						}
//						if(null != map.get("IS_RECEIVE") && !"".equals(map.get("IS_RECEIVE")))
//						{
//							isRecValue = Integer.parseInt(map.get("IS_RECEIVE").toString());
//						}
//						if(null != map.get("STATE") && !"".equals(map.get("STATE")))
//						{
//							stateValue = Integer.parseInt(map.get("STATE").toString());
//						}
//						
//						/*if(Constant.IF_TYPE_YES == oemPlanValue)
//						{
//							detail[6] = "是";
//						}
//						else
//						{
//							detail[6] = "否";
//						}*/
//						if(Constant.IF_TYPE_YES == isPlanValue)
//						{
//							detail[5] = "是";
//						}
//						else
//						{
//							detail[5] = "否";
//						}
//						if(Constant.IF_TYPE_YES == isDirValue)
//						{
//							detail[6] = "是";
//						}
//						else
//						{
//							detail[6] = "否";
//						}
//						if(Constant.IF_TYPE_YES == isLackValue)
//						{
//							detail[7] = "是";
//						}
//						else
//						{
//							detail[7] = "否";
//						}
//						if(Constant.IF_TYPE_YES == isRecValue)
//						{
//							detail[8] = "是";
//						}
//						else
//						{
//							detail[8] = "否";
//						}
//						if(Constant.STATUS_ENABLE == stateValue)
//						{
//							detail[9] = "有效";
//						}
//						else
//						{
//							detail[9] = "无效";
//						}
					}
				}
			}
			
//			this.exportEx(fileName, ActionContext.getContext().getResponse(),
//					request, head, list1);
            // 导出的文件名
			String fileName = "配件采购属性维护信息.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
//          CsvWriterUtil.writeCsv(list, os);
            CsvWriterUtil.createXlsFile(expExcelList, os);
            os.flush();

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出配件采购属性Excel");
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
	 * 
	 * @Title      : 文件导出为xls文件
	 * @param      : @param response
	 * @param      : @param request
	 * @param      : @param head
	 * @param      : @param list
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
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
	
}
