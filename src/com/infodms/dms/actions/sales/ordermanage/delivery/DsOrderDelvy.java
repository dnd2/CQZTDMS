/**********************************************************************
* <pre>
* FILE : DeliveryApply.java
* CLASS : DeliveryApply
* AUTHOR : 
* FUNCTION : 订单发运
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.delivery;


import static com.infodms.dms.actions.sales.financemanage.AccountOpera.dmsFreezePrice_Report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.MonthGeneralOrderCall;
import com.infodms.dms.actions.sales.planmanage.QuotaAssign.ResourceReserveQuery;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.ordermanage.delivery.DsOrderDeliveryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsDsDlvryReqDtlPO;
import com.infodms.dms.po.TtVsDsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCode;
import com.infodms.dms.util.businessUtil.GetOrderNOUtil;
import com.infodms.dms.util.businessUtil.ReqLogUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 常规订单发运申请Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-21
 * @author 
 * @mail  	
 * @version 1.0
 * @remark 
 */
public class DsOrderDelvy {
	
	public Logger logger = Logger.getLogger(DeliveryApply.class);   
	DsOrderDeliveryDao dao  = DsOrderDeliveryDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/delivery/dsdeliveryDetail.jsp";
	/**
	 * 常规订单发运申请页面初始化
	 */
	public void doInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			String areaId = request.getParamValue("areaId");		//业务范围
			String dealerId =logonUser.getDealerId();	//经销商ID
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNO")) ;
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> list = dao.getDeliveryApplyList("", "", areaId, dealerId,oemCompanyId, orderNo);
			List<Map<String,Object>> accountlist = dao.getDealerAccount(dealerId);
			List<Map<String,Object>> addresslist = dao.getDealerAddress(dealerId);
			//获取价格类型
			TmDealerPO tdPo=new TmDealerPO();
			tdPo.setDealerId(new Long(dealerId));
			tdPo=(TmDealerPO) dao.select(tdPo).get(0);
			act.setOutData("priceId", tdPo.getPersonCharge());
			act.setOutData("list", list);
			act.setOutData("accountlist", accountlist);
			act.setOutData("addresslist",addresslist);
			act.setOutData("dealerId", dealerId);
			act.setOutData("curDealerId", logonUser.getDealerId());
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 电商订单发运申请
	 */
	public void applySubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try{
			String applyAmounts = request.getParamValue("applysAmounts");			//申请发运数量
			String callAmounts = request.getParamValue("cAmounts");		    //已申请数量
			String materialIds = request.getParamValue("materialIds");		//物料ID
			String singlePrices = request.getParamValue("singlePrices");	//物料单价
			String typeId = request.getParamValue("typeId");				//账户类型ID
			String accountId = request.getParamValue("accountId");			//经销商账户ID
			String availableAmount = request.getParamValue("availableAmount");//经销商账户可用余额
			String freezeAmount = request.getParamValue("freezeAmount");	//经销商账户预扣
			String transportType = request.getParamValue("transportType");	//发运方式
			String addressId = request.getParamValue("addressId");			//发运地址ID
			String areasIds = request.getParamValue("areaIds");				//业务范围
			String isCheck = request.getParamValue("isCheck");				//资金开关
			String fleetId = request.getParamValue("fleetId");			    //集团客户ID
			String address = request.getParamValue("address");			    //集团客户地址
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 价格类型
			String otherPriceReason = CommonUtils.checkNull(request.getParamValue("otherPriceReason"));// 使用其他价格原因
			String receiver = CommonUtils.checkNull(request.getParamValue("receiver"));// 收货方ID
			String receiverName = CommonUtils.checkNull(request.getParamValue("receiverName"));// 收货方名称
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan"));// 联系人
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));// 联系电话
			String discountAccountId = CommonUtils.checkNull(request.getParamValue("discountAccountId"));// 折扣账户
			String reqRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")).trim() ;
			String tmp_license_amount = CommonUtils.checkNull(request.getParamValue("tmp_license_amount")) ; //临牌数量
			String totailAccount = CommonUtils.checkNull(request.getParamValue("totailAccount"));//订单总价
			String isCover = CommonUtils.checkNull(request.getParamValue("isCover"));            //是否代交车
			//String orderPriceSums = request.getParamValue("orderPriceSums");   //订单价格合计
			String reqTotalAmounts=request.getParamValue("reqTotalAmount");//总数量
			String[] applyAmount = applyAmounts.split(",");					//取得所有数量放在数组中
			String[] singlePrice = singlePrices.split(",");
			String[] materialId = materialIds.split(",");
			//String[] callAmount = callAmounts.split(",");
			String[] priceListIds=request.getParamValue("priceListIds").split(",");
			//String[] orderPriceSum = orderPriceSums.split(",");
			TtVsDsDlvryReqPO tvdrpo = new TtVsDsDlvryReqPO();					//发运申请表
			TtVsDsDlvryReqDtlPO tvdrdpo = new TtVsDsDlvryReqDtlPO();			//发运申请明细表
			String orderIdflg = "";
			Integer reqTotalAmount = 0;
			Double reqTotalPrice = new Double(request.getParamValue("totailAccount"));
			Double reqDiscountPrice = new Double(0);
			Long req_id = Long.parseLong(SequenceManager.getSequence(""));
			tvdrpo.setReqId(req_id);//设置申请主键
			if(!"".equals(typeId)&&typeId!=null){
				tvdrpo.setFundType(Long.parseLong(typeId));
			}
			tvdrpo.setAddressId(new Long(addressId));
			tvdrpo.setReqDate(new Date(System.currentTimeMillis()));
			tvdrpo.setPriceId(priceId);
			tvdrpo.setOtherPriceReason(otherPriceReason);
			tvdrpo.setReqRemark(reqRemark + " 收货方:" + receiverName) ;
			tvdrpo.setTmpLicenseAmount(new Integer(tmp_license_amount));
			tvdrpo.setReqExecStatus(Constant.REQ_EXEC_STATUS_01);
			tvdrpo.setCreateBy(userId);
			tvdrpo.setCreateDate(new Date(System.currentTimeMillis()));
			tvdrpo.setReceiver(new Long(receiver));
			tvdrpo.setLinkMan(linkMan);
			tvdrpo.setDeliveryType(new Integer(transportType));
			tvdrpo.setTel(tel);
			tvdrpo.setVer(0);
			tvdrpo.setReqTotalAmount(new Integer(reqTotalAmounts));
			tvdrpo.setReqTotalPrice(reqTotalPrice);
			tvdrpo.setDealerId(new Long(logonUser.getDealerId()));
			String dlvryReqNO = OrderCode.getDLVRY_ZG(tvdrpo.getFundType());
			tvdrpo.setDlvryReqNo(dlvryReqNO);
			dao.insert(tvdrpo);					//插入真实发运申请表
			int linenumber = 1;
			for(int i=0;i<applyAmount.length;i++){		
				if(!"0".equals(applyAmount[i])){
					reqTotalAmount = 0;
					reqTotalPrice = new Double(0);
					reqDiscountPrice = new Double(0);
					tvdrdpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//设置发运申请明细主键
					tvdrdpo.setMaterialId(Long.parseLong(materialId[i]));
					tvdrdpo.setPriceId(priceListIds[i]==null||"".equals(priceListIds[i])?null:new Long(priceListIds[i]));
					tvdrdpo.setReqId(tvdrpo.getReqId());
					tvdrdpo.setReqAmount(Integer.parseInt(applyAmount[i]));
					//真实发运申请时，已发运数量默认为0或者null
					//tvdrdpo.setDeliveryAmount(Integer.parseInt(applyAmount[i]));
					tvdrdpo.setCreateBy(userId);
					tvdrdpo.setCreateDate(new Date(System.currentTimeMillis()));
					tvdrdpo.setSinglePrice(Double.parseDouble(singlePrice[i]));
					/*
					if (null != orderPriceSum && orderPriceSum.length > 0) {
						tvdrdpo.setTotalPrice(Double.parseDouble(orderPriceSum[i]));
					}
					*/
					tvdrdpo.setVer(0);
					dao.insert(tvdrdpo);//插入真实发运发运申请明细表
					//向接口表中写入记录
					sendDataToErp(tvdrpo,tvdrdpo,logonUser,linenumber);
					linenumber++;
				}
			}			
			act.setOutData("returnValue", 1);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"常规订单发运申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	//向接口表中写入记录
	private void sendDataToErp(TtVsDsDlvryReqPO tvdrpo,TtVsDsDlvryReqDtlPO tvdrdpo,AclUserBean logonUser,int lineNumber)throws Exception {
		dao.sendDataToErp(tvdrpo,tvdrdpo, logonUser,lineNumber);
	}
	
}
