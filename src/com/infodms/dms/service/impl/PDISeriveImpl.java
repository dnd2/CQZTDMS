package com.infodms.dms.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.service.PDISerive;
import com.infodms.dms.util.CommonUtils;
/**
 * 接口实现
 * @author yuewei
 *
 */
public class PDISeriveImpl extends BaseAction implements PDISerive{

	private static final String defaultPDI = "30.0";//默认PDI费用 30
	private final UtilDao dao = UtilDao.getInstance();
	private Logger logger = Logger.getLogger(PDISeriveImpl.class);
	/**
	 * 完成验证转PDI
	 */
	@SuppressWarnings("unchecked")
	public String checkAcceptChangePDI(String vin,Map<String,String> info) {
		try {
			Double pdiprice = BaseUtils.ConvertDouble(info.get("pdiprice").equals("")==true?defaultPDI:info.get("pdiprice")) ;
			Long dearid = BaseUtils.ConvertLong(info.get("dealerid"));
			
			
			TmDealerPO tmTmep =new TmDealerPO();
			tmTmep.setDealerId(dearid);
			tmTmep.setDealerType(Constant.DEALER_TYPE_DWR);
			List<TmDealerPO> tmList = dao.select(tmTmep);
			if(tmList!=null && tmList.size()>0){//判断是否是售后的经销商
				TtAsRepairOrderPO order=new TtAsRepairOrderPO();
				order.setVin(vin);
				order.setRepairTypeCode(Constant.REPAIR_TYPE_08);
				order.setRoStatus(Constant.RO_PRO_STATUS_01);
				List<TtAsRepairOrderPO> orderList = dao.select(order);
				if(orderList!=null&& orderList.size()==0){//判断是否转化过PDI
					logger.info("传值过来的最重要的值=============="+dearid);
					TtAsRepairOrderPO ro=new TtAsRepairOrderPO();
					TmDealerPO d=new TmDealerPO();
					d.setDealerId(dearid);
					List<TmDealerPO> select = dao.select(d);
					if( select!=null&&select.size()>0){
						TmDealerPO tmDealerPO = select.get(0);
						String dealerName = tmDealerPO.getDealerName();
						ro.setDealerName(dealerName);
						String dealerShortname = tmDealerPO.getDealerShortname();
						ro.setDealerShortname(dealerShortname);
					
						Long userId = loginUser.getUserId();
						String dealerCode = tmDealerPO.getDealerCode();
						Map<String, Object> map = dao.getInfoByVin(vin);
						
						Long seqId = getSeq();
						ro.setId(seqId); //
						ro.setDealerCode(dealerCode);//
						String getBillNo = Utility.GetBillNo("",dealerCode,"PR");
						ro.setRoNo(getBillNo);//RO_NO 
						ro.setRepairTypeCode(Constant.REPAIR_TYPE_08);//PDI
						ro.setPrimaryRoNo("1");//原工单号
						ro.setIsCustomerInAsc(1);
						ro.setIsWash(1);
						ro.setServiceAdvisor(dealerCode);//服务专员
						ro.setRoStatus(Constant.RO_STATUS_02);//结算
						ro.setCreateDate(new Date());//创建时间
						ro.setVin(vin);//vin
						
						ro.setCreateBy(userId);//创建人
						ro.setRoCreateDate(new Date());//工单开始时间
						ro.setOwnerName(CommonUtils.checkNull(map.get("CUSTOMER_NAME")));//车主姓名
						ro.setLicense(CommonUtils.checkNull(map.get("LICENSE_NO")));// 牌照号 一般无
						String engine_no = CommonUtils.checkNull(map.get("ENGINE_NO"));
						ro.setEngineNo(engine_no);//发动机号
						String brand_code = CommonUtils.checkNull(map.get("BRAND_CODE"));
						ro.setBrand(brand_code);//BQHS
						String series_code = CommonUtils.checkNull(map.get("SERIES_CODE"));
						ro.setSeries(series_code);//SUV
						String model_code = CommonUtils.checkNull(map.get("MODEL_CODE"));
						ro.setModel(model_code);//B3
						Double mileage = 0.0;//BaseUtils.ConvertDouble(map.get("MILEAGE")+"");
						ro.setInMileage(mileage);//进厂里程数
						ro.setOutMileage(mileage);//出厂里程数
						String deliverer = CommonUtils.checkNull(info.get("deliverer"));
						ro.setDeliverer(deliverer);//送修人姓名
						ro.setDelivererPhone(CommonUtils.checkNull(info.get("delivererPhone")));//送修人电话
						ro.setDelivererMobile(CommonUtils.checkNull(info.get("delivererMobile")));//送修人手机
						Object object2 = map.get("PRODUCT_DATE");
						if(object2!=null){
							ro.setDeliveryDate(Utility.parseString2Date(object2.toString(), null));//生产日期
						}
						ro.setAddItemAmount(pdiprice);
						ro.setRepairAmount(pdiprice);
						ro.setBalanceAmount(pdiprice);
						Object object = map.get("COM_INVO_DATE");
						if(object!=null){
							ro.setGuaranteeDate(Utility.parseString2Date(object.toString(), null));//保养开始时间
						}
						ro.setFreeTimes(0);//0
						ro.setApprovalYn(0);//0
						ro.setForBalanceTime(new Date());
						ro.setOrderValuableType(Constant.RO_PRO_STATUS_01);//正常工单
						ro.setDealerId(dearid);
						ro.setLineNo(BaseUtils.ConvertLong("1"));//车牌
						ro.setPartFlag(1);//1
						ro.setVinIntype(Constant.IF_TYPE_YES);
						ro.setIsClaimFore(1);//1 需要索赔但没预授权
						ro.setCarUseType(0);//
						ro.setQuelityGrate(Constant.QUELITY_GRATE_01);//一级
						ro.setVer(2);
						
						dao.insert(ro);
						
						TtAsWrOtherfeePO fee=new TtAsWrOtherfeePO();
						fee.setFeeCode("QT011");
						List<TtAsWrOtherfeePO> fees = dao.select(fee);
						if( fees!=null&&fees.size()>0){
							//其他费用设置值并添加
							TtAsRoAddItemPO ne=new TtAsRoAddItemPO();
							ne.setId(seqId);
							ne.setAddItemCode(fee.getFeeCode());
							ne.setAddItemName(fee.getFeeName());
							ne.setAddItemAmount(pdiprice);
							ne.setRemark(info.get("remark"));
							ne.setCreateBy(userId);
							ne.setCreateDate(new Date());
							ne.setRoId(ro.getId());
							ne.setIsSel(0);
							ne.setPayType(Constant.PAY_TYPE_02);
							ne.setMainPartCode("-1");
							ne.setChargePartitionCode("S");
							dao.insert(ne);
						}
						
						//转化索赔单并自动上报
						TtAsWrApplicationPO app=new TtAsWrApplicationPO();
						Long seqId1 = getSeq();
						app.setId(seqId1);
						String claimNo="";
						claimNo = Utility.GetClaimBillNo("",dealerCode,"S");
						claimNo = claimNo.substring(1,claimNo.length() );
						if(!Utility.testString(claimNo)){
							throw new Exception("索赔单号生成失败!");
						}
						claimNo = claimNo.toUpperCase();
						app.setClaimNo(claimNo);
						app.setDealerId(dearid);
						app.setRoNo(getBillNo);
						app.setLineNo(BaseUtils.ConvertLong("1"));
						app.setVin(vin);
						app.setRoStartdate(new Date());
						app.setRoEnddate(new Date());
						app.setStartMileage(0.0);
						app.setServeAdvisor(dealerCode);
						app.setReporter(dealerShortname);
						app.setClaimType(Constant.CLA_TYPE_11);
						app.setSubmitTimes(0);
						app.setNetitemAmount(pdiprice);
						app.setRepairTotal(pdiprice);
						app.setGrossCredit(pdiprice);
						app.setInMileage(0.0);
						app.setIsRecommitedSend(0);
						app.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_03);
						app.setCreateBy(userId);
						app.setCreateDate(new Date());
						app.setBalanceAmount(pdiprice);
						app.setEngineNo(engine_no);
						app.setBrandCode(brand_code);
						app.setSeriesCode(series_code);
						app.setModelCode(model_code);
						app.setYieldly("2010010100000001");
						app.setOemCompanyId(tmDealerPO.getOemCompanyId());
						String brand_Name = CommonUtils.checkNull(map.get("BRAND_NAME"));
						String series_name = CommonUtils.checkNull(map.get("SERIES_NAME"));
						String model_name = CommonUtils.checkNull(map.get("MODEL_NAME"));
						String modelId = CommonUtils.checkNull(map.get("MODEL_ID"));
						String seriesId = CommonUtils.checkNull(map.get("SERIES_ID"));
						app.setBrandName(brand_Name);
						app.setSeriesName(series_name);
						app.setModelName(model_name);
						app.setBalanceNetitemAmount(pdiprice);
						app.setIsAudit(Constant.IF_TYPE_NO);
						app.setApplicationDel(String.valueOf(Constant.RO_APP_STATUS_01));
						app.setApplyNetitemAmount(pdiprice);
						app.setApplyRepairTotal(pdiprice);
						app.setModelId(BaseUtils.ConvertLong(modelId));
						app.setSeriesId(BaseUtils.ConvertLong(seriesId));
						app.setIsAuto(0);
						app.setIsInvoice(0);
						app.setIsPrint(Constant.PART_BASE_FLAG_NO.longValue());
						app.setBalanceYieldly(Constant.PART_IS_CHANGHE_01);
						app.setIsLetter(0);
						String fkNo = Utility.GetClaimBillNo("",dealerCode,"FK");
						app.setFkNo(fkNo);
						app.setVerseon(0);
						app.setIsOldAudit(1);
						app.setQuelityGrate(Constant.QUELITY_GRATE_01);//一级
						app.setIsImport(Constant.IF_TYPE_NO);
						app.setSubDate(new Date());
						app.setDealerName(dealerName);
						app.setDealerShortname(dealerShortname);
						dao.insert(app);
						
						TtAsWrNetitemPO ne=new TtAsWrNetitemPO();
						ne.setId(seqId1);
						ne.setNetitemId(getSeq());
						ne.setItemCode(fee.getFeeCode());
						ne.setItemDesc(fee.getFeeName());
						ne.setAmount(pdiprice);
						ne.setRemark(info.get("remark"));
						ne.setCreateBy(userId);
						ne.setCreateDate(new Date());
						ne.setBalanceAmount(pdiprice);
						ne.setPayType(Constant.PAY_TYPE_02);
						ne.setApplyAmount(pdiprice);
						ne.setMainPartCode("-1");
						dao.insert(ne);
						
						TmVehiclePO tm=new TmVehiclePO();
						tm.setVin(vin);
						TmVehiclePO tm1=new TmVehiclePO();
						tm1.setIsPdi(0);
						dao.update(tm, tm1);
					}
				}
			}else{
				return "-2";
			}
		} catch (Exception e) {
			logger.info("系统数据出错，请联系管理员！");
			e.printStackTrace();
			return "-1";
		}
		return "0";
	}
	

}
