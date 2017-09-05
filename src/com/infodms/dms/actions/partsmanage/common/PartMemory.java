package com.infodms.dms.actions.partsmanage.common;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
public class PartMemory implements PTConstants {
	public static final Logger logger = Logger.getLogger(PartMemory.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	
	public void addPartInfoSet() {
		Set<PartinfoBean> partInfo = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
		//取选中的配件Id
		String[] partIds = request.getParamValues("partIds");
		String dealerId = logonUser.getDealerId();
		PartinfoDao partinfoDao = new PartinfoDao();
		for (String partId : partIds) {
			Map<String, Object> part = partinfoDao.getPartDetail(partId, dealerId);
			if (null != part) {
				PartinfoBean bean = new PartinfoBean();
				bean.setPartId(CommonUtils.checkNull(part.get("PART_ID")));//配件ID
				bean.setPartCode(CommonUtils.checkNull(part.get("PART_CODE")));//配件代码
				bean.setPartName(CommonUtils.checkNull(part.get("PART_NAME")));//配件名称
				bean.setUnit(CommonUtils.checkNull(part.get("UNIT")));//单位
				bean.setMiniPack(CommonUtils.checkNull(part.get("MINI_PACK")));//最小包装数
				bean.setPaperQuantity(CommonUtils.checkNull(part.get("PAPER_QUANTITY")).equals("")?"0":CommonUtils.checkNull(part.get("PAPER_QUANTITY")));//账面库存
				bean.setSafeQuantity(CommonUtils.checkNull(part.get("SAFE_QUANTITY")).equals("")?"0":CommonUtils.checkNull(part.get("SAFE_QUANTITY")));//安全库存
				bean.setSalePrice(CommonUtils.checkNull(part.get("SALE_PRICE")));//单价
				bean.setDiscountRate(CommonUtils.checkNull(part.get("DISCOUNT_RATE")).equals("")?"1.00":CommonUtils.checkNull(part.get("DISCOUNT_RATE")));//折扣
				NumberFormat nbf = NumberFormat.getInstance(); 
				nbf.setMinimumFractionDigits(2);//设置小数点后2位 
				double d = Double.parseDouble(bean.getSalePrice()) * Double.parseDouble(bean.getDiscountRate());
				String dd = nbf.format(d);
				bean.setDisPrice(dd);//折扣后价格
				bean.setCount("0");//订购数量
				bean.setRemark("");//备注
				bean.setOrderPrice("0");
				partInfo.add(bean);
			}
		}
		PartInfoSet.setPartInfoSet(String.valueOf(logonUser.getUserId()), partInfo);
		act.setOutData("partInfoSet", partInfo);
		act.setForword(addPartUrl);
	}
	
	public void delPartInfoSet() {
		
		String partId = request.getParamValue("partId");
		Set<PartinfoBean> partInfo = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
		Iterator<PartinfoBean> it = partInfo.iterator();
		while (it.hasNext()) {
			PartinfoBean bean = it.next();
			if (partId.equals(bean.getPartId())) {
				it.remove();
				break;
			}
		}
		PartInfoSet.setPartInfoSet(String.valueOf(logonUser.getUserId()), partInfo);
		act.setOutData("partInfoSet", partInfo);
		act.setForword(addPartUrl);
	}
	
	public void modifyCount() {
		String partId = request.getParamValue("partId");
		String count = CommonUtils.checkNull(request.getParamValue("count"));
		String orderPrice = CommonUtils.checkNull(request.getParamValue("orderPrice"));
		Set<PartinfoBean> partInfo = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
		Iterator<PartinfoBean> it = partInfo.iterator();
		while (it.hasNext()) {
			PartinfoBean bean = it.next();
			if (partId.equals(bean.getPartId())) {
				bean.setCount("".equals(count) ? "0" : count);
				bean.setOrderPrice("".equals(orderPrice) ? "0.00" : orderPrice);
				break;
			}
		}
		PartInfoSet.setPartInfoSet(String.valueOf(logonUser.getUserId()), partInfo);
		act.setOutData("partInfoSet", partInfo);
		act.setForword(addPartUrl);
	}
	
	public void modifyRemark() {
		String partId = request.getParamValue("partId");
		String remark = request.getParamValue("remark");
		Set<PartinfoBean> partInfo = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
		Iterator<PartinfoBean> it = partInfo.iterator();
		while (it.hasNext()) {
			PartinfoBean bean = it.next();
			if (partId.equals(bean.getPartId())) {
				bean.setRemark(remark);
				break;
			}
		}
		PartInfoSet.setPartInfoSet(String.valueOf(logonUser.getUserId()), partInfo);
		act.setOutData("partInfoSet", partInfo);
		act.setForword(addPartUrl);
	}
}
