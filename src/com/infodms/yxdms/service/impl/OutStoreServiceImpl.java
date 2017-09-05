package com.infodms.yxdms.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.yxdms.dao.OutStoreDAO;
import com.infodms.yxdms.entity.oldpart.LogUpatePartProductCodePO;
import com.infodms.yxdms.service.OutStoreService;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public class OutStoreServiceImpl extends OutStoreDAO implements OutStoreService{

	public Map<String, Double> showOutStoreNumByCondition(RequestWrapper request) {
		Map<String, Double> map=new HashMap<String, Double>();
		List<Map<String, Object>> listMapTemp =super.showOutStoreNum(request);
		Double retrun_amount=0.0d;
		Double no_return_amount=0.0d;
		BigDecimal ra = BigDecimal.ZERO;
		BigDecimal nra = BigDecimal.ZERO;
		if(DaoFactory.checkListNull(listMapTemp)){
			Map<String, Object> mapTemp = listMapTemp.get(0);
			BigDecimal a = (BigDecimal) mapTemp.get("RETRUN_AMOUNT");
			BigDecimal b = (BigDecimal) mapTemp.get("NO_RETURN_AMOUNT");
			if(null==a){
				a = BigDecimal.ZERO;
			}
			if(null==b){
				b = BigDecimal.ZERO;
			}
			ra = ra.add(a);
			nra = nra.add(b);
		}
		retrun_amount=ra.doubleValue();
		no_return_amount=nra.doubleValue();
		map.put("retrun_amount", retrun_amount);
		map.put("no_return_amount",no_return_amount );
		return map;
	}
	/**
	 * 查询并导出
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void expotDataOldAudit(ActionContext act, RequestWrapper request,Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list=super.findDataOldAudit(request,pageSizeMax,currPage);
		String[] head={"索赔申请单","签收数","装箱单号","扣除原因","配件代码","配件名称","供应商代码","备注","供应商名称","回运数","其它原因","责任性质","编号","VIN","维修日期","索赔单类型"};
		List<Map<String, Object>> records = list.getRecords();
		List params=new ArrayList();
		if(records!=null &&records.size()>0){
			for (Map<String, Object> map : records) {
				String CLAIM_NO =BaseUtils.checkNull(map.get("CLAIM_NO"));
				String SIGN_AMOUNT =BaseUtils.checkNull(map.get("SIGN_AMOUNT"));
				String BOX_NO =BaseUtils.checkNull(map.get("BOX_NO"));
				String DEDUCT_REMARK =this.getTypeDesc(BaseUtils.checkNull(map.get("DEDUCT_REMARK")));
				String PART_CODE = BaseUtils.checkNull(map.get("PART_CODE"));
				String PART_NAME = BaseUtils.checkNull(map.get("PART_NAME"));
				String PRODUCER_CODE = BaseUtils.checkNull(map.get("PRODUCER_CODE"));
				String SUPPLIER_REMARK = BaseUtils.checkNull(map.get("SUPPLIER_REMARK"));
				String PRODUCER_NAME = BaseUtils.checkNull(map.get("PRODUCER_NAME"));
				String RETURN_AMOUNT = BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
				String OTHER_REMARK = BaseUtils.checkNull(map.get("OTHER_REMARK"));
				String IS_MAIN_CODE = this.getTypeDesc(BaseUtils.checkNull(map.get("IS_MAIN_CODE")));
				String BARCODE_NO = BaseUtils.checkNull(map.get("BARCODE_NO"));
				String VIN = BaseUtils.checkNull(map.get("VIN"));
				String RO_STARTDATE = BaseUtils.checkNull(map.get("RO_STARTDATE"));
				String CLAIM_TYPE = this.getTypeDesc(BaseUtils.checkNull(map.get("CLAIM_TYPE")));
				
				String[] detail={CLAIM_NO,SIGN_AMOUNT,BOX_NO,DEDUCT_REMARK,PART_CODE,PART_NAME,PRODUCER_CODE,SUPPLIER_REMARK,PRODUCER_NAME,RETURN_AMOUNT,OTHER_REMARK,IS_MAIN_CODE,BARCODE_NO,VIN,RO_STARTDATE,CLAIM_TYPE};
				params.add(detail);
			}
		}
		this.toExcel("索赔旧件审核入库", act, head, params);
	}
	@SuppressWarnings("unchecked")
	public int auditAllRebut(RequestWrapper request) {
		int res = 1;
		try {
			String barcode_nos = DaoFactory.getParam(request, "barcode_nos");
			StringBuffer sb= new StringBuffer();
			sb.append("update tt_as_wr_old_returned_detail d\n" );
			sb.append("   set d.is_in_house   = 10041001,\n" );
			sb.append("       d.sign_amount   = 0,\n" );
			sb.append("       d.deduct_remark = 10501006\n" );
			sb.append(" where 1=1 \n" );
			sb.append("   and (d.sign_amount = 1 or d.sign_amount = 0)\n" );
			sb.append("   and d.is_in_house = 10041002");
			DaoFactory.getsql(sb, "d.barcode_no",barcode_nos , 6);
			if(!"".equals(barcode_nos)){
				this.update(sb.toString(), null);
			}else{
				res=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			res=-1;
			throw new RuntimeException(e);
		}
		return res;
	}
	public int diyOutPartSure(RequestWrapper request, AclUserBean loginUser) {
		return super.diyOutPartSure(request,loginUser);
	}

	public List<Map<String, Object>> linkByPartCode(String partCode) {
		return super.linkByPartCode(partCode);
	}
	@SuppressWarnings("unchecked")
	public int logUpatePartProductCode(AclUserBean loginUser,String claimId, String partCode, String mod_code, String mod_name) {
		int res=1;
		try {
			Long userId = loginUser.getUserId();
			String userName = loginUser.getName();
			TtAsWrOldReturnedDetailPO dp = new TtAsWrOldReturnedDetailPO();
			dp.setClaimId(Long.valueOf(claimId));
			List<TtAsWrOldReturnedDetailPO> list = this.select(dp);
			if(DaoFactory.checkListNull(list)){
				for (TtAsWrOldReturnedDetailPO tt : list) {
					String producerCode = tt.getProducerCode();
					String producerName = tt.getProducerName();
					LogUpatePartProductCodePO log=new LogUpatePartProductCodePO();
					log.setId(DaoFactory.getPkId());
					log.setUserId(userId);
					log.setUserName(userName);
					log.setClaimId(Long.valueOf(claimId));
					log.setOldProducerCode(producerCode);
					log.setOldProducerName(producerName);
					log.setNewProducerCode(mod_code);
					log.setNewProducerName(mod_name);
					log.setCreateDate(new Date());
					log.setPartCode(tt.getPartCode());
					log.setPartId(tt.getPartId());
					this.insert(log);
				}
			}
		} catch (NumberFormatException e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
}
