package com.infodms.dms.actions.sales.storage.storagebase;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storage.storagebase.VehicleDistributionDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class VehicleDistribution {
	public Logger logger = Logger.getLogger(VehicleDistribution.class);
	VehicleDistributionDao dao  = VehicleDistributionDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	public  final String distributionInitUrl="/jsp/sales/storage/storagebase/reservoirRegionManage/vehicleDistribution.jsp";

	
	/**
	 *
	 * @FUNCTION   : 库存车分布明细
	 * @author     : andyzhou
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-6-8
	 */
	public void viewDistributionInit(){		
		try {
			//List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			//act.setOutData("list", list_yieldly);
			List<Map<String, Object>> storageList=dao.getStorage(logonUser.getOrgId());
			act.setOutData("storageList", storageList);
			act.setForword(distributionInitUrl);			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"库存车分布明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
}
