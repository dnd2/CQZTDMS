package com.infodms.dms.actions.partsmanage.common;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartClaimItemBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartClaimTypeDao;
import com.infodms.dms.dao.partinfo.PartShippingItemDao;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PartClaimItemMemory {
	public static final Logger logger = Logger.getLogger(PartClaimItemMemory.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private PartShippingItemDao partShippingItemDao = PartShippingItemDao.getInstance();
	private PartClaimTypeDao partClaimTypeDao = PartClaimTypeDao.getInstance();
	public static PartClaimItemMemory getInstance() {
		return new PartClaimItemMemory();
	}
	private static final int DEL_FLAG = 0;//删除标志
	private static final int CLAIM_TYPE_FLAG = 1;//修改索赔类型
	private static final int CLAIM_COUNT_FLAG = 2;//修改索赔数量
	private static final int REMARK_FLAG = 3;//修改备注
	public void addPartClaimItem() {
		Set<PartClaimItemBean> partClaimItems = PartClaimItemSet.get(logonUser.getUserId());
		//取选中的配件Id
		String[] partIds = request.getParamValues("partIds");
		String signNo = request.getParamValue("signNo");
		String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
		String dealerId = logonUser.getDealerId();
		for (String partId : partIds) {
			Map<String, Object> shippingItem = partShippingItemDao.getSignDetail(signNo, Long.parseLong(partId), claimId);
			if (null != shippingItem) {
				partClaimItems.add(assembleBean(shippingItem, true));
			}
		}
		PartClaimItemSet.set(logonUser.getUserId(), partClaimItems);
		act.setOutData("ps", partClaimItems);
		
		List<Map<String, Object>> partClaimType = partClaimTypeDao.getClaimType();
		act.setOutData("pcts", partClaimType);
	} 
	
	public Set<PartClaimItemBean> setPartClaimItem(List<Map<String, Object>> shippingItems) {
		//清空缓存
		PartClaimItemSet.remove(logonUser.getUserId());
		Set<PartClaimItemBean> partClaimItems = PartClaimItemSet.get(logonUser.getUserId());
		for (Map<String, Object> shippingItem : shippingItems) {
			partClaimItems.add(assembleBean(shippingItem, false));
		}
		PartClaimItemSet.set(logonUser.getUserId(), partClaimItems);
		return partClaimItems;
	}
	
	/**
	 * 
	* @Title: assembleBean 
	* @Description: TODO(组装Bean) 
	* @param @param shippingItem
	* @param @param isAdd (true 新增方法,索赔类型,索赔数量,和备注都为空, false从数据库里查询出值)
	* @throws
	 */
	private PartClaimItemBean assembleBean(Map<String, Object> shippingItem, boolean isAdd) {
		PartClaimItemBean pcib = new PartClaimItemBean();
		pcib.setUuid(UUID.randomUUID().toString());
		pcib.setPartId(CommonUtils.checkNull(shippingItem.get("PART_ID")));
		pcib.setItemId(CommonUtils.checkNull(shippingItem.get("ITEM_ID")));
		pcib.setPartCode(CommonUtils.checkNull(shippingItem.get("PART_CODE")));
		pcib.setPartName(CommonUtils.checkNull(shippingItem.get("PART_NAME")));
		pcib.setUnit(CommonUtils.checkNull(shippingItem.get("UNIT")));
		pcib.setOrderCount(CommonUtils.checkNull(shippingItem.get("ORDER_COUNT")));
		pcib.setTransCount(CommonUtils.checkNull(shippingItem.get("COUNT")));
		pcib.setSignCount(CommonUtils.checkNull(shippingItem.get("SIGN_QUANTITY")));
		pcib.setClaimId(CommonUtils.checkNull(shippingItem.get("CLAIM_ID")));
		if (isAdd) {
			pcib.setClaimCount("");
			pcib.setClaimType("");
			pcib.setRemark("");
		} else {
			pcib.setClaimCount(CommonUtils.checkNull(shippingItem.get("CLAIM_COUNT")));
			pcib.setClaimType(CommonUtils.checkNull(shippingItem.get("CLAIM_TYPE_ID")));
			pcib.setRemark(CommonUtils.checkNull(shippingItem.get("REMARK")));
		}
		return pcib;
	}
	
	public void modifyItem() {
		String uuid = request.getParamValue("uuid");
		String value = CommonUtils.checkNull(request.getParamValue("value"));
		int modifyType = Integer.parseInt(request.getParamValue("modifyType"));
		Set<PartClaimItemBean> partClaimItems = PartClaimItemSet.get(logonUser.getUserId());
		Iterator<PartClaimItemBean> it = partClaimItems.iterator();
		while (it.hasNext()) {
			PartClaimItemBean bean = it.next();
			if (uuid.equals(bean.getUuid())) {
				switch (modifyType) {
				case DEL_FLAG : 
					it.remove();
					break;
				case CLAIM_TYPE_FLAG : 
					bean.setClaimType(value);
					break;
				case CLAIM_COUNT_FLAG : 
					bean.setClaimCount(value);
					break;
				case REMARK_FLAG : 
					bean.setRemark(value);
					break;
				}
			}
		}
		PartClaimItemSet.set(logonUser.getUserId(), partClaimItems);
		act.setOutData("ps", partClaimItems);
		
		List<Map<String, Object>> partClaimType = partClaimTypeDao.getClaimType();
		act.setOutData("pcts", partClaimType);
		act.setForword(PTConstants.PART_CLAIM);
	}
	
}
