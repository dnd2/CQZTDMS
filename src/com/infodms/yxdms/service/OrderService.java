package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface OrderService {

	Map<String, String> checkActivityByVin(RequestWrapper request);

	PageResult<Map<String, Object>> orderList(AclUserBean loginUser, RequestWrapper request,Integer pageSize, Integer currPage);

	List<Map<String, Object>> chooseRoType(RequestWrapper request);

	PageResult<Map<String, Object>> addLabour(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> addPart(RequestWrapper request,Integer pageSize, Integer currPage);

	Map<String, Object> showInfoByVin(RequestWrapper request);

	Map<String, Object> findLoginUserInfo(Long userId);

	int roInsert(RequestWrapper request, AclUserBean loginUser);

	Map<String, Object> findRepairById(RequestWrapper request);

	List<Map<String, Object>> findLaboursById(RequestWrapper request,String type);

	List<Map<String, Object>> findPartsById(RequestWrapper request,String type);

	String orderDelete(RequestWrapper request);

	boolean checkIsRoEndByVin(RequestWrapper request);

	List<Map<String, Object>> findAccById(RequestWrapper request);

	PageResult<Map<String, Object>> accList(RequestWrapper request,Integer pageSize, Integer currPage);

	String checkMileage(RequestWrapper request);

	List<Map<String, Object>> showInfoBylicenseNo(RequestWrapper request, AclUserBean loginUser);

	List<Map<String, Object>> findVinListByVin(RequestWrapper request);

	Map<String, Object> getGuaFlag(String partCode,String vin,String inMileage,String purchasedDate);

	PageResult<Map<String, Object>> doActivity(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	int deleteTrPart(RequestWrapper request);

	List<Map<String, Object>> findAccData(RequestWrapper request);

	List<Map<String, Object>> findOutById(RequestWrapper request);

	String findFreeRoByPartId(RequestWrapper request);

	Map<String, Object> showInfoByroNo(RequestWrapper request);

	List<Map<String, Object>> checkaddPart(RequestWrapper request,AclUserBean loginUser);


	Map<String, Object> showInfoWinterByVin(RequestWrapper request,String str,AclUserBean loginUser);

}
