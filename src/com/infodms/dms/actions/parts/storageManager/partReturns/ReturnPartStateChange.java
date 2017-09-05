package com.infodms.dms.actions.parts.storageManager.partReturns;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partReturns.ReturnsPartStateChangeDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartDlrChangeDtlPO;
import com.infodms.dms.po.TtPartDlrReturnDtlPO;
import com.infodms.dms.po.TtPartReturnRelationPO;
import com.infodms.dms.po.TtPartReturnUnlockDtlPO;
import com.infodms.dms.po.TtPartReturnUnlockMainPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


/**
 * 配件退换货状态变更
 * @author fanzhineng
 * <br>TT_PART_DLR_RETURN_MAIN 退货表
 * <br>TT_PART_DLR_CHANGE_MAIN 换货表
 */
public class ReturnPartStateChange {
	public static final Logger logger = Logger.getLogger(ReturnPartStateChange.class);
	private static final ReturnsPartStateChangeDao dao = ReturnsPartStateChangeDao.getInstance();
	
	/*=================================URL START=======================================*/
	/**
	 * 配件退换货解封申请页面
	 */
	private static final String TO_RETURN_PART_APPL = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartAppl.jsp";
    /**
     * 配件退换货解封申请-新增-页面
     */
    private static final String TO_ADD_RETURN_APPLY = "/jsp/parts/storageManager/partReturnsStateChange/toAddReturnApply.jsp";
	/**
	 * 配件退换货解封审核配置页面
	 */
	private static final String TO_RETURN_PART_CONFIG = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartConfig.jsp";
	/**
	 * 配件退换货解封审核配置页面
	 */
	private static final String TO_RETURN_PART_CFGUPDATE = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartCfgUpdate.jsp";
	/**
	 * 配件退换货解封审核查询页面
	 */
	private static final String TO_RETURN_PART_AUDIT = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartAudit.jsp";
	/**
	 * 配件退货解封明细查询页面
	 */
	private static final String TO_RETURN_PART_SELECT = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartSelect.jsp";
	/**
	 * 配件退换货解封单审核页面
	 */
	private static final String TO_RETURN_PART_APPAUDIT = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartAppAudit.jsp";
	/**
	 * 配件退换货解封查询页面
	 */
	private static final String TO_RETURN_PART_UNLOCKED = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartUnlocked.jsp";
	/**
	 * 配件退换货解封页面
	 */
	private static final String TO_RETURN_PART_UNLOCK_END = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartUnlockEnd.jsp";
	/**
	 * 配件退换货解封单查询页面
	 */
	private static final String TO_RETURN_PART_SELECTBYDEALER = "/jsp/parts/storageManager/partReturnsStateChange/toReturnPartSelectByDealer.jsp";
	/*=================================URL END=========================================*/
	/**
	 * 跳转到配件退换货解封申请页面
	 */
	public void toReturnPartAppl(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setOutData("dealerId", loginUser.getDealerId());
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			act.setOutData("oldDate", simpleDateFormat.format(CommonUtils.getFirstDateOfMonth(new Date())));
			act.setOutData("newDate", simpleDateFormat.format(CommonUtils.getLastDateOfMonth(new Date())));
			act.setForword(TO_RETURN_PART_APPL);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封申请页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取配件退换货解封申请信息
	 */
	public void getReturnPartAppl(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnPartAppl(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取配件退换货解封申请信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到配件退换货解封申请新增页面
	 */
	@SuppressWarnings("unchecked")
	public void toAddReturnApply(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			if(StringUtil.notNull(loginUser.getDealerId())){
				String dealerId = loginUser.getDealerId();
				TmDealerPO tmd = new TmDealerPO();
				tmd.setDealerId(Long.valueOf(dealerId));
				TmDealerPO tmdx = (TmDealerPO) dao.select(tmd).get(0);
				act.setOutData("dealerId", tmdx.getDealerId());
				act.setOutData("dealerCode", tmdx.getDealerCode());
				act.setOutData("dealerName", tmdx.getDealerName());
				TtPartWarehouseDefinePO whPo = new TtPartWarehouseDefinePO();
				whPo.setOrgId(tmdx.getDealerId());
				List<TtPartWarehouseDefinePO> whList = dao.select(whPo);
				act.setOutData("whList", whList);
			}else{
				if(Constant.ORG_TYPE_OEM.equals(loginUser.getOrgType())){
					//主机厂
					TmOrgPO tmo = new TmOrgPO();
					tmo.setOrgId(Long.valueOf(Constant.OEM_ACTIVITIES));
					TmOrgPO tmox = (TmOrgPO) dao.select(tmo).get(0);
					act.setOutData("dealerId", tmox.getOrgId());
					act.setOutData("dealerCode", tmox.getOrgCode());
					act.setOutData("dealerName", tmox.getOrgName());
					TtPartWarehouseDefinePO whPo = new TtPartWarehouseDefinePO();
					whPo.setOrgId(tmox.getOrgId());
					List<TtPartWarehouseDefinePO> whList = dao.select(whPo);
					act.setOutData("whList", whList);
				}
			}
			
			//查询退货解封总额度，已使用额度，可用额度
			//总额度
			request.setAttribute("GET_ED_FLAG_BY_DEALER_ID", "YES");
			List<Map<String,Object>> list = dao.getReturnPartConfig(request, loginUser, 1, 50).getRecords();
			Map<String,Object> map = new HashMap<String, Object>();
			if(list!=null){
				if(StringUtil.notNull(loginUser.getDealerId())){
					map = list.get(0);//配送中心有自己的额度
				}else{
					//总部没有额度，没有调拨价
					map.put("AMOUNT", "0");
				}
			}else{
				map.put("AMOUNT", "0");
			}
			Double edAmount = 0D;
			if(StringUtil.notNull(CommonUtils.checkNull(map.get("AMOUNT")))){
				edAmount = Double.valueOf(CommonUtils.checkNull(map.get("AMOUNT")));
			}
			
			String first = DateUtil.format(CommonUtils.getFirstDateOfMonth(new Date()), "yyyy-MM-dd") ;
			String last = DateUtil.format(CommonUtils.getLastDateOfMonth(new Date()), "yyyy-MM-dd");
			//当前月已用
			Map<String,Object> mp = dao.getArradyAmountNowMonth(request,loginUser,first,last);
			Double arradyAmount = 0D;
			if(StringUtil.notNull(CommonUtils.checkNull(mp.get("AMOUNT")))){
				arradyAmount =  Double.valueOf(CommonUtils.checkNull(mp.get("AMOUNT")));
			}
			
			Double kyAmount = edAmount - arradyAmount;
			act.setOutData("edAmount", edAmount);
			act.setOutData("arradyAmount", arradyAmount);
			act.setOutData("kyAmount", kyAmount);
			
			act.setOutData("OEM_ACTIVITIES", Constant.OEM_ACTIVITIES);
			act.setOutData("userName", loginUser.getName());
			act.setForword(TO_ADD_RETURN_APPLY);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封申请新增页面异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
			toReturnPartAppl();
		}
	}
	
	/**
	 * 获取配件退换货已入库配件查询
	 */
	public void getReturnParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnParts(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取配件退换货已入库配件查询异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存退换货解封申请单
	 */
	@SuppressWarnings("unchecked")
	public void saveReturnApply(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String error = "";
		try{
			String dealerId = request.getParamValue("dealerId");
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
//			String amount = request.getParamValue("amount");//总金额
			String remark = request.getParamValue("remark");//解封原因
//			String unlocType = request.getParamValue("unlocType");//解封类型(退换货解封)
			String[] cks = request.getParamValues("ck");
			
			String unlocCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_91);//获取解封单号
			if(cks==null){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "未获取到保存数据，请重试！");
			}else{
				Date nowDate = new Date();//获取当前时间
//				Double saleAmount_sum = 0D;//累计调拨总金额
				
				//创建解封单主单
				TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
				um.setUnlocId(Long.valueOf(SequenceManager.getSequence("")));
				um.setUnlocCode(unlocCode);
				um.setDealerId(Long.valueOf(dealerId));
				um.setDealerCode(dealerCode);
				um.setDealerName(dealerName);
//				um.setUnlocType(Integer.valueOf(unlocType));//解封类型(退换货解封)
				um.setUnlocType(Constant.RC_JF_TYPE_01); // 解封类型（退货解封）
				um.setState(Constant.RC_JF_STATE_01);//已保存
				if(StringUtil.notNull(remark)){
					um.setRemark(remark);//申请解封原因
				}
				um.setCreateDate(nowDate);
				um.setCreateBy(loginUser.getUserId());
				
				for (int i = 0; i < cks.length; i++) {
					String dtlId = cks[i];//退货明细id
					String returnCode = request.getParamValue("returnCode"+dtlId);//退货单号
					String soCode = request.getParamValue("soCode"+dtlId);//销售单编码
					String inCode = request.getParamValue("inCode"+dtlId);//销售单编码
					String partId = request.getParamValue("partId"+dtlId);
					String partOldcode = request.getParamValue("partOldcode"+dtlId);
					String partCname = request.getParamValue("partCname"+dtlId);
//					String partCode = request.getParamValue("partCode"+dtlId);
//					String salePrice = request.getParamValue("salePrice"+dtlId);//调拨价
					String inQty = request.getParamValue("inQty"+dtlId);//冻结总量
					String kyQty = request.getParamValue("kyQty"+dtlId);//可用总量
					String applyQty = request.getParamValue("applyQty"+dtlId);//申请总量
					//String unlocQty = request.getParamValue("unlocQty"+dtlId);//已解封数量
//					String saleAmount = request.getParamValue("saleAmount"+dtlId);//调拨金额
					
					//验证可用数量是否正确
					List<Map<String,Object>> validateList = dao.getReturnMainAndDtlByDtlIds(request,loginUser,dtlId);
					if(validateList==null){
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "查询明细异常，请重试！");
					}else{
						Map<String,Object> map = validateList.get(0);
						//String returnType = CommonUtils.checkNull(map.get("RETURN_TYPE"));//直发，非直发
						
						String inQty_select = CommonUtils.checkNull(map.get("IN_QTY"));
						String kyQty_select = CommonUtils.checkNull(map.get("KY_QTY"));
						String unlocQty_select = CommonUtils.checkNull(map.get("UNLOC_QTY"));
						String whId = CommonUtils.checkNull(map.get("WH_ID"));
						String inlocId =  CommonUtils.checkNull(map.get("INLOC_ID"));
						
						if(!kyQty.equals(kyQty_select) && Integer.parseInt(applyQty) > Integer.parseInt(kyQty_select)){
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】可用数小于申请数！");
						}
						if(Integer.valueOf(inQty_select) < (Integer.valueOf(kyQty_select)+ Integer.valueOf(unlocQty_select))){
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】冻结<(解封+可用)！");
						}
						if(Integer.valueOf(inQty_select).intValue()!=Integer.valueOf(inQty).intValue()){
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】总冻结数量不符！");
						}
						
						
						//创建解封单明细单
						TtPartReturnUnlockDtlPO ud = new TtPartReturnUnlockDtlPO();
						ud.setDtlId(Long.valueOf(SequenceManager.getSequence("")));
						ud.setUnlocId(um.getUnlocId());
						ud.setRdtlId(Long.valueOf(dtlId));
						ud.setPartId(Long.valueOf(partId));
						ud.setPartOldcode(partOldcode);
						ud.setPartCode(partOldcode);
//						ud.setPartCode(partCode);
						ud.setPartCname(partCname);
						ud.setSoCode(soCode);
						ud.setInCode(inCode);
						Long qty = Long.valueOf(applyQty);//申请数量
						
//						Double dbPrice = Double.valueOf(CommonUtils.checkNull(map.get("SALE_PRICE")));//调拨单价
//						if(Double.valueOf(salePrice).intValue()!=dbPrice.intValue()){
//							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】调拨单价异常！");
//						}
//						Double dbAmount = Arith.round(dbPrice*qty,2);
//						if(Double.valueOf(saleAmount).intValue()!=dbAmount.intValue()){
//							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】调总金额异常！");
//						}
//						saleAmount_sum = saleAmount_sum + dbAmount;//累计总金额
						
						ud.setApplyQty(qty);
//						ud.setBuyPrice(dbPrice);
						ud.setBuyPrice(0d);
//						ud.setAmount(dbAmount);
						ud.setAmount(0d);
						ud.setCreateDate(nowDate);
						ud.setCreateBy(loginUser.getUserId());
						String sellerId = CommonUtils.checkNull(map.get("SELLER_ID"));
						String sellerCode = CommonUtils.checkNull(map.get("SELLER_CODE"));
						String sellerName = CommonUtils.checkNull(map.get("SELLER_NAME"));
						ud.setSellerId(Long.valueOf(sellerId));
						ud.setSellerCode(sellerCode);
						ud.setSellerName(sellerName);
						ud.setWhId(Long.valueOf(whId));
						ud.setInlocId(Long.valueOf(inlocId));
						dao.insert(ud);//插入明细
						
						//更新可用数量（总数量-申请数量-已解封），更新是否完全解封
						//退货
						TtPartDlrReturnDtlPO rd = new TtPartDlrReturnDtlPO();
						rd.setDtlId(Long.valueOf(dtlId));
						
						TtPartDlrReturnDtlPO rdx = new TtPartDlrReturnDtlPO();
							rdx.setKyQty(Long.valueOf(kyQty_select) - qty);//可用-申请
							if(rdx.getKyQty().intValue() == 0 && Long.valueOf(unlocQty_select).intValue() == Long.valueOf(inQty_select).intValue()){
								rdx.setIsUnloc(Constant.IF_TYPE_YES);
							}else if(rdx.getKyQty().intValue() < 0){
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "单号【"+returnCode+"】,编码【"+partOldcode+"】可用数小于申请数！");
							}else{
								
							}
						rdx.setUpdateDate(nowDate);
						rdx.setUpdateBy(loginUser.getUserId());
						dao.update(rd, rdx);
						
					}
				}
				um.setAmount(0d);//设置总调拨金额
//				if(saleAmount_sum.intValue()!=Double.valueOf(amount).intValue()){
//					throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "页面总金额："+amount+"不等于 计算总金额："+saleAmount_sum+"！");
//				}
				dao.insert(um);
			}
			
			if(StringUtil.notNull(error)){
				act.setOutData("error", error);
			}else{
				act.setOutData("success", "解封单["+unlocCode+"]保存成功！");
			}
			
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存退换货解封申请单异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到配件退换货解封审核配置页面
	 */
	public void toReturnPartConfig(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_RETURN_PART_CONFIG);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封审核配置页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询配件退换货解封审核配置信息
	 */
	public void getReturnPartConfig(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnPartConfig(request,loginUser,curPage,Constant.PAGE_SIZE_MIDDLE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询配件退换货解封审核配置信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到配件退换货解封审核配置修改页面
	 */
	public void toReturnPartcfgUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			List<Map<String,Object>> list= dao.getReturnPartConfig(request,loginUser,1,Constant.PAGE_SIZE_MIDDLE).getRecords();
			Map<String,Object> map = list.get(0);
			act.setOutData("map", map);
			act.setForword(TO_RETURN_PART_CFGUPDATE);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封审核配置修改页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存配件退换货解封审核配置
	 */
	@SuppressWarnings("unchecked")
	public void updateReturnPartCfg(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//获取前台数据
			String DEALER_ID = request.getParamValue("DEALER_ID");
			String PARENT_ID = request.getParamValue("PARENT_ID");
			String AMOUNT = request.getParamValue("AMOUNT");
			String REMARK = request.getParamValue("REMARK");
			//验证配置表是否存在数据
			TtPartReturnRelationPO tprr = new TtPartReturnRelationPO();
			tprr.setOrgId(Long.valueOf(DEALER_ID));
			List<TtPartReturnRelationPO> tprrList = dao.select(tprr);
			
			if(tprrList!=null && !tprrList.isEmpty() && tprrList.size()>0){
				//存在update
				TtPartReturnRelationPO tprrx = tprrList.get(0);
				
				TtPartReturnRelationPO tprrx1 = new TtPartReturnRelationPO();
				tprrx1.setReId(tprrx.getReId());
				TtPartReturnRelationPO tprrx2 = new TtPartReturnRelationPO();
				tprrx2.setAmount(Double.valueOf(AMOUNT));
				if(StringUtil.notNull(REMARK)){
					tprrx2.setRemark(REMARK);
				}else{
					tprrx2.setRemark("");
				}
				tprrx2.setUpdateDate(new Date());
				tprrx2.setUpdateBy(loginUser.getUserId());
				dao.update(tprrx1, tprrx2);
			}else{
				//不存在insert
				TtPartReturnRelationPO tprrx = new TtPartReturnRelationPO();
				tprrx.setReId(Long.valueOf(SequenceManager.getSequence("")));
				tprrx.setOrgId(Long.valueOf(DEALER_ID));
				tprrx.setParentId(Long.valueOf(PARENT_ID));
				tprrx.setAmount(Double.valueOf(AMOUNT));
				if(StringUtil.notNull(REMARK)){
					tprrx.setRemark(REMARK);
				}
				tprrx.setCreateDate(new Date());
				tprrx.setCreateBy(loginUser.getUserId());
				tprrx.setUpdateDate(new Date());
				tprrx.setUpdateBy(loginUser.getUserId());
				dao.insert(tprrx);
			}
			//操作结果
			act.setOutData("success", "修改成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SAVE_FAILURE_CODE, "保存配件退换货解封审核配置异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 跳转到配件退换货解封审核页面
	 */
	public void toReturnPartAudit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_RETURN_PART_AUDIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封审核页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到配件退换货解封页面
	 */
	public void toReturnPartUnlocked(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setForword(TO_RETURN_PART_UNLOCKED);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到配件退换货解封页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 作废解封单
	 */
	@SuppressWarnings("unchecked")
	public void deleteUnloc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("value");
			if(StringUtil.notNull(unlocId)){
				Date nowDate = new Date();//作废时间，回滚时间
				//作废主订单
				TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
				um.setUnlocId(Long.valueOf(unlocId));
				TtPartReturnUnlockMainPO umx = new TtPartReturnUnlockMainPO();
				umx.setState(Constant.RC_JF_STATE_00);//作废
				umx.setDisableDate(nowDate);
				umx.setDisableBy(loginUser.getUserId());
				umx.setUpdateDate(nowDate);
				umx.setUpdateBy(loginUser.getUserId());
				dao.update(um, umx);
				//查询解封类型
				TtPartReturnUnlockMainPO ums = (TtPartReturnUnlockMainPO) dao.select(um).get(0);
				Integer unlocType = ums.getUnlocType();
				
				//回写退换货可用数量数据
				TtPartReturnUnlockDtlPO ud = new TtPartReturnUnlockDtlPO();
				ud.setUnlocId(um.getUnlocId());
				List<TtPartReturnUnlockDtlPO> listDtl = dao.select(ud);
				for (TtPartReturnUnlockDtlPO dtl : listDtl) {
					Long rDtl = dtl.getRdtlId();//退换货明细单id
					Long appQty = dtl.getApplyQty();
					if(unlocType.equals(Constant.RC_JF_TYPE_01)){
						//退货
						TtPartDlrReturnDtlPO rd = new TtPartDlrReturnDtlPO();
						rd.setDtlId(rDtl);
						TtPartDlrReturnDtlPO rds = (TtPartDlrReturnDtlPO) dao.select(rd).get(0);
						Long kyQty = rds.getKyQty();
						
						TtPartDlrReturnDtlPO rdx = new TtPartDlrReturnDtlPO();
						rdx.setKyQty(kyQty+appQty);	
						if(rdx.getKyQty().intValue()>0){
							rdx.setIsUnloc(Constant.IF_TYPE_NO);
						}
						rdx.setUpdateBy(loginUser.getUserId());
						rdx.setUpdateDate(nowDate);
						dao.update(rd, rdx);
					}else if(unlocType.equals(Constant.RC_JF_TYPE_02)){
						//换货
						TtPartDlrChangeDtlPO cd = new TtPartDlrChangeDtlPO();
						cd.setDtlId(rDtl);
						
						TtPartDlrChangeDtlPO cds = (TtPartDlrChangeDtlPO) dao.select(cd).get(0);
						Long kyQty = cds.getKyQty();
						
						TtPartDlrChangeDtlPO cdx = new TtPartDlrChangeDtlPO();
						cdx.setKyQty(kyQty+appQty);	
						if(cdx.getKyQty().intValue()>0){
							cdx.setIsUnloc(Constant.IF_TYPE_NO);
						}
						cdx.setUpdateBy(loginUser.getUserId());
						cdx.setUpdateDate(nowDate);
						dao.update(cd,cdx);
						
					}
					
				}
				act.setOutData("success", "作废成功！");
			}else{
				act.setOutData("error", "作废失败，没有获取到解封订单，请重试！");
			}
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "作废解封单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到退货配件解封明细查询页面
	 */
	public void toReturnPartSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("value");
            String state = CommonUtils.checkNull(request.getParamValue("state"));
//			TtPartReturnUnlockMainPO main = new TtPartReturnUnlockMainPO();
//			main.setUnlocId(Long.valueOf(unlocId));
//			TtPartReturnUnlockMainPO mains = (TtPartReturnUnlockMainPO) dao.select(main).get(0);
//			act.setOutData("mains", mains);
//			TcUserPO user = new TcUserPO();
//			user.setUserId(mains.getCreateBy());
//			TcUserPO users = (TcUserPO) dao.select(user).get(0);
//			act.setOutData("userName", users.getName());
//			TcCodePO tc = new TcCodePO();
//			tc.setCodeId(mains.getState().toString());
//			TcCodePO tcs = (TcCodePO) dao.select(tc).get(0);
//			act.setOutData("codeDesc", tcs.getCodeDesc());
//			act.setOutData("app_date", DateUtil.format(mains.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
//			String unlocType = TcCodeDao.getInstance().getCodeDescByCodeId(mains.getUnlocType().toString());
//			act.setOutData("unlocType", unlocType);
            Map<String,Object> mainMap = dao.getUnlocMainByUnlocId(unlocId, Integer.parseInt(state));
            act.setOutData("mains", mainMap);
			
			act.setForword(TO_RETURN_PART_SELECT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到退货配件解封明细查询页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取退货配件解封明细
	 */
	public void getReturnPartSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnPartSelect(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取退货配件解封明细异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 提交解封申请单
	 */
	@SuppressWarnings("unchecked")
	public void submitUnloc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("value");
			String unlocCode = request.getParamValue("unlocCode");
			TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
			um.setUnlocId(Long.valueOf(unlocId));
			
			// 校验解封数量是否正确
			List<Map<String, Object>> unlocPartList = dao.getReturnPartInQtyList(unlocId);
			if(unlocPartList.size() <= 0){
			    act.setOutData("success", "退货单【"+unlocCode+"】下没有可解封的退货配件！");
			    return;
			}
			for(int i = 0 ; i < unlocPartList.size(); i++){
			    Map<String, Object> unlocPart = unlocPartList.get(i);
			    long difference = ((BigDecimal) unlocPart.get("DIFFERENCE")).longValue(); // 退货配件入库数量减申请解封数量的差
			    if(difference < 0){
			        act.setOutData("success", "配件【"+unlocPart.get("PART_OLDCODE").toString()+"】的可解封数量大于申请的解封数量！");
			        return;
			    }
			}
			
//			TtPartReturnUnlockMainPO ums = (TtPartReturnUnlockMainPO) dao.select(um).get(0);
//			Double dbAmount = ums.getAmount();
			//1.判断是否在当前月直接通过审核的解封额度范围内(额度>（已用+本次）)
//			List<Map<String,Object>> list = dao.getReturnPartConfig(request, loginUser, 1, 50).getRecords();
//			Map<String,Object> map = new HashMap<String, Object>();
//			if(list!=null){
//				if(StringUtil.notNull(loginUser.getDealerId())){
//					map = list.get(0);//配送中心有自己的额度
//				}else{
//					//总部没有额度，没有调拨价
//					map.put("AMOUNT", "0");
//				}
//			}else{
//				map.put("AMOUNT", "0");
//			}
//			Double edAmount = 0D;
//			if(StringUtil.notNull(CommonUtils.checkNull(map.get("AMOUNT")))){
//				edAmount = Double.valueOf(CommonUtils.checkNull(map.get("AMOUNT")));
//			}
			
//			String first = DateUtil.format(CommonUtils.getFirstDateOfMonth(new Date()), "yyyy-MM-dd") ;
//			String last = DateUtil.format(CommonUtils.getLastDateOfMonth(new Date()), "yyyy-MM-dd");
			
//			Map<String,Object> mp = dao.getArradyAmountNowMonth(request,loginUser,first,last);
//			Double arradyAmount = 0D;
//			if(StringUtil.notNull(CommonUtils.checkNull(mp.get("AMOUNT")))){
//				arradyAmount =  Double.valueOf(CommonUtils.checkNull(mp.get("AMOUNT")));
//			}
//			//-----换货解封不用额度控制（秦凯需求）
//			Integer unlocType = ums.getUnlocType();
//			if(unlocType.equals(Constant.RC_JF_TYPE_02)){
//				edAmount = dbAmount+arradyAmount;//换货解封，额度默认=本单+已用
//			}
			//-----换货解封不用额度控制（秦凯需求）
			
			TtPartReturnUnlockMainPO umx = new TtPartReturnUnlockMainPO();
//			if(edAmount>=(dbAmount+arradyAmount)){
//				在当前月直接通过审核的解封额度范围内，直接审核通过！备注:当月额度范围内系统自动审核通过
//				umx.setState(Constant.RC_JF_STATE_03);
//				if(unlocType.equals(Constant.RC_JF_TYPE_01)){
//					umx.setCheckRemark("当月额度范围内系统自动审核通过!");
//				}
//				umx.setCheckBy(-1L);//表示系统自动审核通过
//				umx.setCheckDate(new Date());
//			}else{
//				//--------------------2016-07-05根据秦凯要求，额度不够不提交
//				//不在当前月直接通过审核的解封额度范围内，需要人工进行审核
//				/*
				umx.setState(Constant.RC_JF_STATE_02); //退货解封处理状态 - 已提交
				umx.setSubmitBy(loginUser.getUserId()); // 提交人
				umx.setSubmitDate(new Date()); // 提交时间
				umx.setUpdateBy(umx.getSubmitBy()); // 更新人
				umx.setUpdateDate(umx.getSubmitDate()); // 更新时间
//				*/
//				//--------------------2016-07-05根据秦凯要求，额度不够不提交
//				String er = "解封授权额度（月度）："+edAmount+",本月使用："+arradyAmount+"<br>";
//				er+="可用额度："+(edAmount-arradyAmount)+" 小于 本单解封额度："+dbAmount+"<br>";
//				er+="额度不足，不能提交解封单，请向总部申请提升额度";
//				throw new BizException(act,  ErrorCodeConstant.SPECIAL_MEG, er);
//			}
			dao.update(um, umx);
			
			act.setOutData("success", "操作成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "提交解封申请单异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到审核页面
	 */
	public void toReturnPartAppAudit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = CommonUtils.checkNull(request.getParamValue("value"));
			String state = CommonUtils.checkNull(request.getParamValue("state"));
//			TtPartReturnUnlockMainPO main = new TtPartReturnUnlockMainPO();
//			main.setUnlocId(Long.valueOf(unlocId));
//			TtPartReturnUnlockMainPO mains = (TtPartReturnUnlockMainPO) dao.select(main).get(0);
//			act.setOutData("mains", mains);
//			TcUserPO user = new TcUserPO();
//			user.setUserId(mains.getCreateBy());
//			TcUserPO users = (TcUserPO) dao.select(user).get(0);
//			act.setOutData("userName", users.getName());
//			TcCodePO tc = new TcCodePO();
//			tc.setCodeId(mains.getState().toString());
//			TcCodePO tcs = (TcCodePO) dao.select(tc).get(0);
//			act.setOutData("codeDesc", tcs.getCodeDesc());
//			act.setOutData("app_date", DateUtil.format(mains.getSubmitDate(), "yyyy-MM-dd HH:mm:ss"));
//			String unlockType = TcCodeDao.getInstance().getCodeDescByCodeId(mains.getUnlocType().toString());
//			act.setOutData("unlockType", unlockType);
			
			// 获取主要信息
//            TtPartReturnUnlockMainPO main = new TtPartReturnUnlockMainPO();
//            main.setUnlocId(Long.valueOf(unlocId));
//            TtPartReturnUnlockMainPO mains = (TtPartReturnUnlockMainPO) dao.select(main).get(0);
//            TcUserPO user = new TcUserPO();
//            user.setUserId(mains.getCreateBy());
//            TcUserPO users = (TcUserPO) dao.select(user).get(0);
            Map<String,Object> mainMap = dao.getUnlocMainByUnlocId(unlocId, Integer.parseInt(state));
			
			//获取明细
//			List<Map<String,Object>> mapList = dao.getUnlocDtlByUnlocId(unlocId,mains.getUnlocType().toString());
			List<Map<String,Object>> mapList = dao.getUnlocDtlByUnlocId(unlocId);
			
			act.setOutData("mains", mainMap);
//			act.setOutData("userName", users.getName());
			act.setOutData("mapList", mapList);
			act.setForword(TO_RETURN_PART_APPAUDIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到退货配件解封明细查询页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 获取待审核的解封申请单
	 */
	public void getReturnPartJFAppInfos(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnPartJFAppInfos(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "获取配件退换货解封申请信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 驳回解封申请单
	 */
	@SuppressWarnings("unchecked")
	public void dontAgreeWithUnloc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("unlocId");
			String checkRemark = request.getParamValue("checkRemark");
			TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
			um.setUnlocId(Long.valueOf(unlocId));
			TtPartReturnUnlockMainPO umx = new TtPartReturnUnlockMainPO();
			umx.setState(Constant.RC_JF_STATE_04);//驳回
			umx.setCheckDate(new Date());
			umx.setCheckBy(loginUser.getUserId());
			umx.setUpdateBy(loginUser.getUserId());
			umx.setUpdateDate(umx.getCheckDate());
			if(StringUtil.notNull(checkRemark)){
				umx.setCheckRemark(checkRemark);
			}else{
				umx.setCheckRemark("&nbsp;");
			}
			dao.update(um, umx);
			act.setOutData("success", "驳回成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "驳回解封申请单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 通过解封申请单
	 */
	@SuppressWarnings("unchecked")
	public void agreeWithUnloc(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("unlocId");
			String checkRemark = request.getParamValue("checkRemark");
			TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
			um.setUnlocId(Long.valueOf(unlocId));
			TtPartReturnUnlockMainPO umx = new TtPartReturnUnlockMainPO();
			umx.setState(Constant.RC_JF_STATE_03);//审核通过
			umx.setCheckDate(new Date());
			umx.setCheckBy(loginUser.getUserId());
			if(StringUtil.notNull(checkRemark)){
				umx.setCheckRemark(checkRemark);
			}else{
				umx.setCheckRemark("&nbsp;");
			}
			dao.update(um, umx);
			act.setOutData("success", "审核成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "通过解封申请单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转到退货配件解封页面
	 */
	public void toReturnPartUnlocakEnd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String unlocId = request.getParamValue("value");
            String state = CommonUtils.checkNull(request.getParamValue("state"));
//			TtPartReturnUnlockMainPO main = new TtPartReturnUnlockMainPO();
//			main.setUnlocId(Long.valueOf(unlocId));
//			TtPartReturnUnlockMainPO mains = (TtPartReturnUnlockMainPO) dao.select(main).get(0);
//			act.setOutData("mains", mains);
//			TcUserPO user = new TcUserPO();
//			user.setUserId(mains.getCreateBy());
//			TcUserPO users = (TcUserPO) dao.select(user).get(0);
//			act.setOutData("userName", users.getName());
//			TcCodePO tc = new TcCodePO();
//			tc.setCodeId(mains.getState().toString());
//			TcCodePO tcs = (TcCodePO) dao.select(tc).get(0);
//			act.setOutData("codeDesc", tcs.getCodeDesc());
//			act.setOutData("app_date", DateUtil.format(mains.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
//			act.setOutData("create_date", DateUtil.format(mains.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
//			String unlockType = TcCodeDao.getInstance().getCodeDescByCodeId(mains.getUnlocType().toString());
//			act.setOutData("unlockType", unlockType);
			
			//获取主要信息
            Map<String,Object> mainMap = dao.getUnlocMainByUnlocId(unlocId, Integer.parseInt(state));
            
            //获取明细
//          List<Map<String,Object>> mapList = dao.getUnlocDtlByUnlocId(unlocId,mains.getUnlocType().toString());
            List<Map<String,Object>> mapList = dao.getUnlocDtlByUnlocId(unlocId);
            
            act.setOutData("mains", mainMap);
//          act.setOutData("userName", users.getName());
            act.setOutData("mapList", mapList);
			act.setForword(TO_RETURN_PART_UNLOCK_END);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转到退货配件解封明细查询页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 退货配件解封
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void unlocPartOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			Date timeF = new Date();
			String unlocId = request.getParamValue("unlocId");
			//验证解封单状态，防止重复解封
			TtPartReturnUnlockMainPO um = new TtPartReturnUnlockMainPO();
			um.setUnlocId(Long.valueOf(unlocId));
			
			TtPartReturnUnlockMainPO ums = (TtPartReturnUnlockMainPO) dao.select(um).get(0);
//			Integer unlocType = ums.getUnlocType();
			if(ums.getState().equals(Constant.RC_JF_STATE_03)){
				//只有等于已审核的单子才可以解封
				
				//回写解封状态
				TtPartReturnUnlockMainPO umx = new TtPartReturnUnlockMainPO();
				umx.setState(Constant.RC_JF_STATE_05);//已解封
				umx.setUnlocDate(timeF);
				umx.setUnlocBy(loginUser.getUserId());
				dao.update(um, umx);
				
				//回写退货表 已解封数量，并判断是否完全解封
				TtPartReturnUnlockDtlPO ud = new TtPartReturnUnlockDtlPO();
				ud.setUnlocId(Long.valueOf(unlocId));
				
				List<TtPartReturnUnlockDtlPO> udList = dao.select(ud);
				for (TtPartReturnUnlockDtlPO uds : udList) {
					//验证占用库存数（解封数量，不需小于退换货占用数量）精确到货位
					//改了封存逻辑后，需要修改对应得验证逻辑
					Map<String,Object> stockMap = dao.getVwPartStock(uds.getPartId().toString(),ums.getDealerId().toString(),uds.getWhId().toString(),uds.getInlocId().toString());
					if(stockMap==null){
						throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件：【"+uds.getPartOldcode()+"】封存数量不足，不能解封！");
					}else{
						String bookedQty = CommonUtils.checkNull(stockMap.get("BOOKED_QTY"));
						if(StringUtil.notNull(bookedQty)){
							Long bookedQtyL = Long.valueOf(bookedQty);
							Long applyQty = uds.getApplyQty();
							if(bookedQtyL<applyQty){
								throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件：【"+uds.getPartOldcode()+"】封存数量不足，不能解封！");
							}
						}else{
							throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "配件：【"+uds.getPartOldcode()+"】封存数量不足，不能解封！");
						}
					}
					
//					if(unlocType.equals(Constant.RC_JF_TYPE_01)){
						TtPartDlrReturnDtlPO rd = new TtPartDlrReturnDtlPO();
						rd.setDtlId(uds.getRdtlId());//退货源PO
						
						TtPartDlrReturnDtlPO rds = (TtPartDlrReturnDtlPO) dao.select(rd).get(0);//旧退货数据
						
						TtPartDlrReturnDtlPO rdx = new TtPartDlrReturnDtlPO();
						Long yUnlocQty = rds.getUnlocQty() + uds.getApplyQty();
						rdx.setUnlocQty(yUnlocQty);//新已解封 = 旧已解封 + 新申请
						rdx.setKyQty(rds.getInQty() - rdx.getUnlocQty()); // 剩余可解封数量
						if(yUnlocQty.equals(rds.getInQty())){
							//新已解封 = 总入库
							rdx.setIsUnloc(Constant.IF_TYPE_YES);//完全解封
						}
						rdx.setUpdateDate(timeF);
						rdx.setUpdateBy(loginUser.getUserId());
						dao.update(rd, rdx);
//					}else if(unlocType.equals(Constant.RC_JF_TYPE_02)){
//						TtPartDlrChangeDtlPO rd = new TtPartDlrChangeDtlPO();
//						rd.setDtlId(uds.getRdtlId());//退货源PO
//						
//						TtPartDlrChangeDtlPO rds = (TtPartDlrChangeDtlPO) dao.select(rd).get(0);//旧退货数据
//						
//						TtPartDlrChangeDtlPO rdx = new TtPartDlrChangeDtlPO();
//						Long yUnlocQty = rds.getUnlocQty() + uds.getApplyQty();
//						rdx.setUnlocQty(yUnlocQty);//新已解封 = 旧已解封 + 新申请
//						if(yUnlocQty.equals(rds.getInQty())){
//							//新已解封 = 总入库
//							rdx.setIsUnloc(Constant.IF_TYPE_YES);//完全解封
//						}
//						rdx.setUpdateDate(timeF);
//						rdx.setUpdateBy(loginUser.getUserId());
//						dao.update(rd, rdx);
//					}
					
				}
				
				// 改了封存逻辑后，需要修改对应解封逻辑
				//调用解封逻辑
				List ins = new LinkedList<Object>();
				ins.add(0, CommonUtils.parseLong(unlocId));
				ins.add(1, Constant.PART_CODE_RELATION_91);
				ins.add(2, 0);// 0：释放占用,1:占用 
				dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				
				//2016-07-06 解封逻辑，以后修改使用start
				/**
				inOrOutFlag = 2; //出库标识
                partRecState = 2; //正常封存

                //1.配件正常封存出库
                insertPRPo = new TtPartRecordPO();

                Long recId = Long.parseLong(SequenceManager.getSequence(""));
                insertPRPo.setRecordId(recId);
                insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
                insertPRPo.setPartId(partId);
                insertPRPo.setPartCode(partCode);
                insertPRPo.setPartOldcode(partOldcode);
                insertPRPo.setPartName(partCname);
                insertPRPo.setPartBatch(partBatch);
                insertPRPo.setVenderId(Long.parseLong(partVenId));
                insertPRPo.setPartNum(returnQty);//出入库数量
                insertPRPo.setConfigId(Long.parseLong(configId));
                insertPRPo.setOrderId(changeId);//变更单ID
                insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                insertPRPo.setOrgCode(parentOrgCode);
                insertPRPo.setOrgName(chgorgCname);
                insertPRPo.setWhId(Long.parseLong(whId));
                insertPRPo.setWhCode(whCode);
                insertPRPo.setWhName(whName);
                insertPRPo.setLocId(locId);
                insertPRPo.setLocCode(locCode);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(date);
                insertPRPo.setCreateDate(date);
                insertPRPo.setPersonId(userId);
                insertPRPo.setPersonName(name);
                insertPRPo.setPartState(partRecState);//配件状态

                dao.insert(insertPRPo);

                //调用出入库逻辑
                ins = new LinkedList<Object>();
                ins.add(0, changeId);
                ins.add(1, configId);

                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);


                inOrOutFlag = 1; //入库标识
                partRecState = 1; //正常

                //2.配件正常入库
                insertPRPo = new TtPartRecordPO();

                Long recId1 = Long.parseLong(SequenceManager.getSequence(""));
                insertPRPo.setRecordId(recId1);
                insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
                insertPRPo.setPartId(partId);
                insertPRPo.setPartCode(partCode);
                insertPRPo.setPartOldcode(partOldcode);
                insertPRPo.setPartName(partCname);
                insertPRPo.setPartBatch(partBatch);
                insertPRPo.setVenderId(Long.parseLong(partVenId));
                insertPRPo.setPartNum(returnQty);//出入库数量
                insertPRPo.setConfigId(Long.parseLong(configId));
                insertPRPo.setOrderId(changeId);//变更单ID
                insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
                insertPRPo.setOrgId(Long.parseLong(parentOrgId));
                insertPRPo.setOrgCode(parentOrgCode);
                insertPRPo.setOrgName(chgorgCname);
                insertPRPo.setWhId(Long.parseLong(whId));
                insertPRPo.setWhCode(whCode);
                insertPRPo.setWhName(whName);
                insertPRPo.setLocId(locId);
                insertPRPo.setLocCode(locCode);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(date);
                insertPRPo.setCreateDate(date);
                insertPRPo.setPersonId(userId);
                insertPRPo.setPersonName(name);
                insertPRPo.setPartState(partRecState);//配件状态

                dao.insert(insertPRPo);

                //调用出入库逻辑
                ins = new LinkedList<Object>();
                ins.add(0, changeId);
                ins.add(1, configId);

                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
                **/
				//2016-07-06 解封逻辑，以后修改使用end
			}else{
				String codeDesc = TcCodeDao.getInstance().getCodeDescByCodeId(ums.getState().toString());
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "解封失败，当前前状态为:【"+codeDesc+"】");
			}
			
			act.setOutData("success", "解封成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "退货配件解封异常");
			if(StringUtil.notNull(e.getMessage())){
				e1.setMessage(e.getMessage());
			}
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 跳转配件退换货解封单查询页面
	 */
	public void toReturnPartSelectByDealer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//RequestWrapper request = act.getRequest();
		try{
			act.setOutData("dealerId", loginUser.getDealerId());
//			act.setOutData("pdealerType", loginUser.getPdealerType());
			act.setOutData("pdealerType", loginUser.getPoseBusType());
			act.setForword(TO_RETURN_PART_SELECTBYDEALER);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "跳转配件退换货解封单查询页面异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询退换货解封单
	 */
	public void getReturnPartSelectByDealer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getReturnPartSelectByDealer(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询退换货解封单异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
}
