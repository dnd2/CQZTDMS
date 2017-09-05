package com.infodms.yxdms.action;

import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.claim.preAuthorization.AuthorizationDao;
import com.infodms.yxdms.service.AuthorizationService;
import com.infodms.yxdms.service.OrderService;
import com.infodms.yxdms.service.impl.AuthorizationServiceImpl;
import com.infodms.yxdms.service.impl.OrderServiceImpl;
import com.infodms.yxdms.utils.BaseAction;

public class AuthorizationAction extends BaseAction{

	private AuthorizationService authorizationservice = new AuthorizationServiceImpl();
	private OrderService orderservice=new OrderServiceImpl();
	private final AuthorizationDao dao = AuthorizationDao.getInstance();
	
	public void authorizationPrint(){
		try {
			Map< String, Object> foInfoMap = dao.authorizationModifyQuery(request,loginUser);
			Map<String, Object> vinInfo =  orderservice.showInfoByVin(request);
			Map<String, Object> userInfo = orderservice.findLoginUserInfo(Long.valueOf(foInfoMap.get("CREATE_BY").toString()));
			
			Map<String, Object> partInfo = null;
			if(null != foInfoMap.get("MAIN_PART_ID")){		
				partInfo = dao.getPartInfo(request,loginUser,foInfoMap);
			}
			//获取三包等级
			String vrLevel = "";
			List<Map<String, Object>> vrLevelList = dao.getVrLevel(request);
			if (null != vrLevelList && vrLevelList.size() > 0) {
				vrLevel = vrLevelList.get(0).get("VR_LEVEL").toString();
			}
			act.setOutData("userInfo", userInfo);
			act.setOutData("foInfoMap", foInfoMap);
			act.setOutData("vinInfo", vinInfo);
			act.setOutData("partInfo", partInfo);
			act.setOutData("vrLevel", vrLevel);
			act.setForword("/jsp/claim/preAuthorization/authorizationDetailPrint.jsp");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
