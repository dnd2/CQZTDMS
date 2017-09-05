package com.infodms.yxdms.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.yxdms.dao.InvoiceDAO;
import com.infodms.yxdms.service.InvoiceService;
import com.infoservice.mvc.context.RequestWrapper;

public class InvoiceServiceImpl extends InvoiceDAO implements InvoiceService{

	public Map<String, Double> invoiceInfoMoneyByNO(String no) {
		Double pdiandkeepfitmoney=super.findPdiAndKeepFit(no);
		Double partandaccmoney=super.findPartAndAcc(no);
		Double allmoney=super.findAll(no);
		Map<String, Double> map=new HashMap<String, Double>();
		//保养和PDI
		map.put("pdiAndKeepFitMoney", pdiandkeepfitmoney);
		//配件材料费+辅料费
		map.put("partAndAccMoney", partandaccmoney);
		//二次入库费用
		List<Map<String, Object>> SecondTimeByNo = this.invoiceInfoSecondTimeByNo(no);
		Double secondTimeMoney=0.0d;
		for (Map<String, Object> mapSecond : SecondTimeByNo) {
			BigDecimal b= (BigDecimal) mapSecond.get("AMOUNT");
			secondTimeMoney=Arith.add(secondTimeMoney,b.doubleValue());
		}
		map.put("secondTimeMoney", secondTimeMoney);
		//其他费用合计
		Double ohersMoney=0.0d;
		ohersMoney=Arith.sub(allmoney, pdiandkeepfitmoney);
		ohersMoney=Arith.sub(ohersMoney, partandaccmoney);
		map.put("ohersMoney",ohersMoney);
		Double fjcount=super.findFJcount(no);
		map.put("fjcount", fjcount);
		return map;
	}

	public List<Map<String, Object>> invoiceInfoSecondTimeByNo(String no) {
		List<Map<String, Object>> list=super.invoiceInfoSecondTimeByNo(no);
		return list;
	}

	public List<Map<String, Object>> returnShow(String no) {
		List<Map<String, Object>> list=super.returnShow(no);
		return list;
	}

	public List<Map<String, Object>> findSpecialAmount(String dealerId,String startTime, String endTime, Long areaId) {
		return super.findSpecialAmount(dealerId,startTime,endTime,areaId);
	}

	public List<FsFileuploadPO> AppprintFJ(String balanecNo) {
		return super.AppprintFJ(balanecNo);
	}

	
     public List<TtAsPaymentPO> queryPaymentByid(RequestWrapper request,AclUserBean loginUser) {
    	 return super.queryPaymentByid(request,loginUser);
	}

	public int addsureRemarkPayment(RequestWrapper request,AclUserBean loginUser) {
		return super.addsureRemarkPayment( request, loginUser);
	}

	public String checkticeksByBalanceNo(RequestWrapper request) {
		return super.checkticeksByBalanceNo(request);
	}
}
