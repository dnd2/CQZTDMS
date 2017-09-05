/**
 * @Title: MaterialGroupManage.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sysproduct.productmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmGroupColorPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtAsWrModelItemPO;
import com.infodms.dms.po.TtAsWrProductPackagePO;
import com.infodms.dms.po.TtProductMaterialPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSA11;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * @param <List>
 * 
 */
public class MaterialGroupManage {
	private Logger logger = Logger.getLogger(MaterialGroupManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final ProductManageDao dao = ProductManageDao.getInstance();

	private final String MATERIAL_GROUP_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialGroupManageQuery.jsp";// 物料组维护查询页面
	private final String MATERIAL_GROUP_MANAGE_ADD_URL = "/jsp/sysproduct/productmanage/materialGroupManageAdd.jsp";// 物料组维护新增页面
	private final String MATERIAL_GROUP_MANAGE_MOD_URL = "/jsp/sysproduct/productmanage/materialGroupManageMod.jsp";// 物料组维护修改页面
	
	//zhumingwei 2011-10-20
	private final String DEALER_PRODECT_PACKAGE_URL = "/jsp/sysproduct/productmanage/dealerProductPackageQuery.jsp";//经销商产品套餐维护
	private final String DEALER_PRODECT_PACKAGE_ADD_URL = "/jsp/sysproduct/productmanage/dealerProductPackageAdd.jsp";//经销商产品套餐新增
	private final String DEALER_PRODECT_PACKAGE_DETAIL_URL = "/jsp/sysproduct/productmanage/dealerProductPackageDetail.jsp";//经销商产品套餐查询
	private final String DEALER_PRODECT_DISTRIBUTION_URL = "/jsp/sysproduct/productmanage/dealerProductDistribution.jsp";//经销商产品套餐分配
	private final String DEALER_PRODECT_DISTRIBUTION_ADD_URL = "/jsp/sysproduct/productmanage/dealerProductDistributionAdd.jsp";//经销商产品套餐分配维护
	private final String DEALER_PRODECT_PACKAGE_UPDATE_URL = "/jsp/sysproduct/productmanage/dealerProductUpdate.jsp";//经销商产品套餐维护修改
	
	private final String DEALER_PRODECT_PACKAGE_INIT_URL = "/jsp/sysproduct/productmanage/dealerProductPackageInit.jsp";//经销商产品套餐查询
	
	private final String NEW_DEALER_PRODECT_DISTRIBUTION_URL = "/jsp/sysproduct/productmanage/newDealerProductDistribution.jsp";//经销商产品套餐分配
	private final String NEW_DEALER_PRODECT_DISTRIBUTION_ADD_URL = "/jsp/sysproduct/productmanage/newDealerProductDistributionAdd.jsp";//经销商产品
	
	// 物料组维护pre
	public void materialGroupManageQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			//查询参数传 递
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCodeArg"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupNameArg")),"utf-8");
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCodeArg"));
			String status = CommonUtils.checkNull(request.getParamValue("statusArg"));
			String forcast_flag = CommonUtils.checkNull(request.getParamValue("forcast_flag"));
			
			String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));
			//查询参数据传递
			act.setOutData("groupCode", groupCode);
			act.setOutData("groupName", groupName);
			act.setOutData("status", status);
			act.setOutData("parentGroupCode", parentGroupCode);
			act.setOutData("curPage", curPage);
			act.setOutData("forcast_flag", forcast_flag);
			act.setForword(MATERIAL_GROUP_MANAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-20 add 经销商产品套餐维护
	@SuppressWarnings("unchecked")
	public void dealerProductPackage() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List provinces = this.getProvinceByUserId(logonUser.getUserId());
			act.setOutData("provinces",provinces);
			act.setForword(DEALER_PRODECT_PACKAGE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-27
	public void dealerProductPackageInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List provinces = this.getProvinceByUserId(logonUser.getUserId());
			act.setOutData("provinces",provinces);
			act.setForword(DEALER_PRODECT_PACKAGE_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-20
	@SuppressWarnings("unchecked")
	public List getProvinceByUserId(Long uid){
		StringBuffer sql= new StringBuffer();
		sql.append("select ur.user_id,ur.region_code,r.region_name,r.region_id\n" );
		sql.append(" from tc_user_region_relation ur ,tm_region r\n" );
		sql.append(" where ur.region_code = r.region_code and ur.user_id = '"+uid+"' \n");
		List list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	//zhumingwei 2011-10-20
	public void dealerProductPackageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("packageName")),"utf-8");
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			//String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("packageCode", packageCode);
			map.put("packageName", packageName);
			map.put("regionId", regionId);
			map.put("status", status);
			//map.put("companyId", companyId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.getDealerProductPackageQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-27
	public void getDealerProductPackage() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("packageName")),"utf-8");
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("packageCode", packageCode);
			map.put("packageName", packageName);
			map.put("regionId", regionId);
			map.put("status", status);
			map.put("companyId", companyId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.getDealerPackageQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21
	public void dealerProductDistributionQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("packageName")),"utf-8");
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			//String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("packageCode", packageCode);
			map.put("packageName", packageName);
			map.put("regionId", regionId);
			map.put("status", status);
			map.put("dealerId", dealerId);
			//map.put("companyId", companyId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.getDealerProductPackageQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-20
	public void dealerProductPackageAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List provinces = this.getProvinceByUserId(logonUser.getUserId());
			act.setOutData("provinces",provinces);
			act.setForword(DEALER_PRODECT_PACKAGE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void updateDealerProductPackage(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			List<Map<String, Object>> detail = dao.getProdyctRegion(id);
			Map map = detail.get(0);
			act.setOutData("map", map);
			List provinces = this.getProvinceByUserId(logonUser.getUserId());
			act.setOutData("provinces",provinces);
			act.setForword(DEALER_PRODECT_PACKAGE_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void getProdyctMaterialInfo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String product_id = CommonUtils.checkNull(request.getParamValue("product_id"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getProdyctMaterialInfoList(product_id, Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "业务范围详细信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21
	public void addProductAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName = CommonUtils.checkNull(request.getParamValue("packageName"));
			String province = CommonUtils.checkNull(request.getParamValue("province"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String[] groupIds = groupId.split(",");
			
			TtAsWrProductPackagePO productPO = new TtAsWrProductPackagePO();
			Long productId = Long.parseLong(SequenceManager.getSequence(""));
			productPO.setProductId(productId);
			productPO.setPackageCode(packageCode.trim());
			productPO.setPackageName(packageName.trim());
			productPO.setProvinceId(Long.parseLong(province));
			productPO.setStatus(Integer.parseInt(status));
			productPO.setCreateBy(logonUser.getUserId());
			productPO.setCreateDate(new Date());
			dao.insert(productPO);
			
			for (int i = 0; i < groupIds.length; i++) {
				TtProductMaterialPO proMatPO = new TtProductMaterialPO();
				proMatPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				proMatPO.setProductId(productId);
				proMatPO.setMaterialId((Long.parseLong(groupIds[i])));
				proMatPO.setCreateBy(logonUser.getUserId());
				proMatPO.setCreateDate(new Date());
				dao.insert(proMatPO);
			}
			act.setForword(DEALER_PRODECT_PACKAGE_URL);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21
	public void dealerProductPackagedetail() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			List<Map<String, Object>> detail = dao.getProdyctRegion(id);
			List<Map<String, Object>> detailMat = dao.getProdyctMaterial(id);
			Map map = detail.get(0);
			act.setOutData("map", map);
			act.setOutData("detailMat", detailMat);
			act.setForword(DEALER_PRODECT_PACKAGE_DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21
	public void dealerProductDisterbutionAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			List<Map<String, Object>> detail = dao.getProdyctRegion(id);
			Map map = detail.get(0);
			act.setOutData("map", map);
			List<Map<String, Object>> dtlList = dao.getProductDistribution(id) ;
			
			if(!CommonUtils.isNullList(dtlList)) {
				StringBuffer theId = new StringBuffer("") ;
				StringBuffer theName = new StringBuffer("") ;
				StringBuffer theCode = new StringBuffer("") ;
				
				int len = dtlList.size() ;
				
				for(int i=0; i<len; i++) {
					int strLen = theId.length() ;
					
					if(strLen == 0) {
						theId.append(dtlList.get(i).get("COMPANY_ID")) ;
						theName.append(dtlList.get(i).get("COMPANY_NAME")) ;
						theCode.append(dtlList.get(i).get("COMPANY_CODE")) ;
					} else {
						theId.append(",").append(dtlList.get(i).get("COMPANY_ID")) ;
						theName.append(",").append(dtlList.get(i).get("COMPANY_NAME")) ;
						theCode.append(",").append(dtlList.get(i).get("COMPANY_CODE")) ;
					}
				}
				
				act.setOutData("theId", theId) ;
				act.setOutData("theCode", theCode) ;
				act.setOutData("theName", theName) ;
			}
			
			act.setOutData("ID", id);
			act.setForword(DEALER_PRODECT_DISTRIBUTION_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-21 
	public void dealerProductDistribution() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//List provinces = this.getProvinceByUserId(logonUser.getUserId());
			//act.setOutData("provinces",provinces);
			act.setForword(DEALER_PRODECT_DISTRIBUTION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-25
	public void dealerPackageReportExcel(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String packageCode = request.getParamValue("packageCode");	
			String packageName = request.getParamValue("packageName");	
			String regionId = request.getParamValue("regionId");
			String status = request.getParamValue("status");
			String[] head=new String[6];
			head[0]="套餐代码";
			head[1]="套餐名称";
			head[2]="省份";
			head[3]="状态";
			head[4]="物料组代码";
			head[5]="物料组名称";
			
			Map<String, String> paraMap = new HashMap<String, String>() ;
			paraMap.put("packageCode", packageCode) ;
			paraMap.put("packageName", packageName) ;
			paraMap.put("regionId", regionId) ;
			paraMap.put("status", status) ;
			
			List<Map<String, Object>> list= dao.getProductComboDtl(paraMap);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[12];
						detail[0] = String.valueOf(map.get("PACKAGE_CODE"));
						detail[1] = String.valueOf(map.get("PACKAGE_NAME"));
						detail[2] = String.valueOf(map.get("REGION_NAME"));
						detail[3] = String.valueOf(map.get("STATUS"));
						detail[4] = String.valueOf(map.get("GROUP_CODE"));
						detail[5] = String.valueOf(map.get("GROUP_NAME"));
						list1.add(detail);
			    	}
			    }
		    }
		    com.infodms.dms.actions.claim.basicData.ToExcel.toExcel222(ActionContext.getContext().getResponse(), request, head, list1);
		  // act.setForword(DEALER_PRODECT_PACKAGE_URL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	public void dealerPackageDlr(){
    	ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String packageCode = request.getParamValue("packageCode");	
			String packageName = request.getParamValue("packageName");	
			String regionId = request.getParamValue("regionId");
			String status = request.getParamValue("status");
			String companyId = request.getParamValue("dealerId") ;
			String[] head=new String[6];
			head[0]="套餐代码";
			head[1]="套餐名称";
			head[2]="省份";
			head[3]="状态";
			head[4]="经销商公司代码";
			head[5]="经销商公司名称";
			
			Map<String, String> paraMap = new HashMap<String, String>() ;
			paraMap.put("packageCode", packageCode) ;
			paraMap.put("packageName", packageName) ;
			paraMap.put("regionId", regionId) ;
			paraMap.put("status", status) ;
			paraMap.put("companyId", companyId) ;
			
			List<Map<String, Object>> list= dao.getProductComboDlr(paraMap);
		    List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[12];
						detail[0] = String.valueOf(map.get("PACKAGE_CODE"));
						detail[1] = String.valueOf(map.get("PACKAGE_NAME"));
						detail[2] = String.valueOf(map.get("REGION_NAME"));
						detail[3] = String.valueOf(map.get("STATUS"));
						detail[4] = String.valueOf(map.get("COMPANY_CODE"));
						detail[5] = String.valueOf(map.get("COMPANY_NAME"));
						list1.add(detail);
			    	}
			    }
		    }
		    com.infodms.dms.actions.claim.basicData.ToExcel.toExcel222(ActionContext.getContext().getResponse(), request, head, list1);
		   //act.setForword(DEALER_PRODECT_PACKAGE_URL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}

	// 物料组维护查询
	public void materialGroupManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupName")),"utf-8");
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUP_LEVEL"));
			//新增过滤条件是否在产 2012-05-08 hxy
			String forcast_flag = CommonUtils.checkNull(request.getParamValue("forcast_flag"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("groupName", groupName);
			map.put("parentGroupCode", parentGroupCode);
			map.put("status", status);
			map.put("companyId", companyId);
			map.put("forcast_flag", forcast_flag);
			map.put("groupLevel", groupLevel);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getMaterialGroupManageQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料组维护新增pre
	public void materialGroupManageAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			//String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupName")),"utf-8");
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String forcast_flag = CommonUtils.checkNull(request.getParamValue("forcast_flag"));
			String page = CommonUtils.checkNull(request.getParamValue("page"));
			//Long companyId = logonUser.getCompanyId();
			//List<Map<String, Object>> groups1 = dao.getModelGroupList(Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
			//List<Map<String, Object>> groups2 = dao.getModelGroupList(Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组

			//act.setOutData("groups1", groups1);
			//act.setOutData("groups2", groups2);
			
			//查询参数据传递
			act.setOutData("groupCode", groupCode);
			act.setOutData("groupName", groupName);
			act.setOutData("status", status);
			act.setOutData("parentGroupCode", parentGroupCode);
			act.setOutData("forcast_flag", forcast_flag);
			act.setOutData("page", page);
			act.setForword(MATERIAL_GROUP_MANAGE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料组维护新增
	public void materialGroupManageAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String ifType=request.getParamValue("ifType");
			
			//String colorCode[] = request.getParamValues("colorCode") == null ? new String[] {} : request.getParamValues("colorCode");
			//String colorName[] = request.getParamValues("colorName") == null ? new String[] {} : request.getParamValues("colorName");

			// 获得上级物料组
			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupCode(parentGroupCode);
			parent = dao.getTmVhclMaterialGroupPO(parent);

			// 物料组保存
			long groupId = new Long(SequenceManager.getSequence(""));
			TmVhclMaterialGroupPO po = new TmVhclMaterialGroupPO();
			po.setGroupId(groupId);
			po.setGroupCode(groupCode);
			po.setGroupName(groupName);
			if (!parentGroupCode.equals("")) {
				po.setGroupLevel(new Integer(parent.getGroupLevel().intValue() + 1));
				String treeCode = dao.getMaterialGroupTreeCode(parent.getGroupId().toString());
				po.setTreeCode(treeCode);
				po.setParentGroupId(parent.getGroupId());
			} else {
				String treeCode = dao.getMaterialGroupTreeCode("");
				po.setTreeCode(treeCode);
				po.setParentGroupId(-1L);
				po.setGroupLevel(new Integer(1));
			}
			if(ifType.equals(Constant.IF_TYPE_YES.toString())){
				po.setForcastFlag(1);
			}else{
				po.setForcastFlag(0);
			}
			po.setStatus(new Integer(status));
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			po.setCompanyId(logonUser.getCompanyId());
			po.setIfStatus(new Integer(0));// 接口状态

			dao.insert(po);
			
//			if(colorCode.length>0){
//				dao.deleteColorByGroupId(String.valueOf(groupId));
//				for (int i = 0; i < colorCode.length; i++) {
//						TmGroupColorPO colorPOValue = new TmGroupColorPO();
//						colorPOValue.setColorId(new Long(SequenceManager.getSequence("")));
//						colorPOValue.setGroupId(groupId);
//						colorPOValue.setColorCode(colorCode[i]);
//						colorPOValue.setColorName(colorName[i]);
//						colorPOValue.setCreateBy(logonUser.getUserId());
//						colorPOValue.setCreateDate(new Date(System.currentTimeMillis()));
//						colorPOValue.setUpdateBy(logonUser.getUserId());
//						colorPOValue.setUpdateDate(new Date(System.currentTimeMillis()));
//						dao.insert(colorPOValue); 
//				}
//			}
			
//			// 判断上级物料组是否是车系
//			if (parent != null && parent.getGroupLevel().intValue() == 2) {
//				String modelGroup1 = CommonUtils.checkNull(request.getParamValue("modelGroup1"));
//				String modelGroup2 = CommonUtils.checkNull(request.getParamValue("modelGroup2"));
//
//				// 索赔工时车型组车型关系保存
//				TtAsWrModelItemPO itemPo = new TtAsWrModelItemPO();
//				itemPo.setModelId(po.getGroupId());
//				itemPo.setWrgroupId(new Long(modelGroup1));
//				itemPo.setCreateDate(new Date());
//				itemPo.setCreateBy(logonUser.getUserId());
//				dao.insert(itemPo);
//
//				// 配件车型组车型关系保存
//				if(modelGroup2 != null && !"".equals(modelGroup2)) {
//					itemPo = new TtAsWrModelItemPO();
//					itemPo.setModelId(po.getGroupId());
//					itemPo.setWrgroupId(new Long(modelGroup2));
//					itemPo.setCreateDate(new Date());
//					itemPo.setCreateBy(logonUser.getUserId());
//					dao.insert(itemPo);
//				}
//			}

			/* 此处需调用接口 */
			//OSA11 os = new OSA11();
			//os.execute(po);//接口
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料组维护修改pre
	public void materialGroupManageModPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long companyId = logonUser.getCompanyId();
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("groupName")),"utf-8");
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String page = CommonUtils.checkNull(request.getParamValue("page"));
			String forcast_flag = CommonUtils.checkNull(request.getParamValue("forcast_flag"));
			TmVhclMaterialGroupPO po = new TmVhclMaterialGroupPO();
			po.setGroupId(new Long(groupId));
			po = dao.getTmVhclMaterialGroupPO(po);

			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupId(po.getParentGroupId());
			parent = dao.getTmVhclMaterialGroupPO(parent);
			
			TmGroupColorPO colorPOValue = new TmGroupColorPO();
			if(po!=null){
				TmGroupColorPO colorPO = new TmGroupColorPO();
				colorPO.setGroupId(Long.valueOf(groupId));
				List<PO> list = dao.select(colorPO);
				if(list!=null && list.size()>0){
					colorPOValue = (TmGroupColorPO)list.get(0);
				}
			}
			if(colorPOValue!=null){
				act.setOutData("colorId", colorPOValue.getColorId());
				act.setOutData("colorCode", colorPOValue.getColorCode());
				act.setOutData("colorName", colorPOValue.getColorName());
			}

			// 判断物料级别是否是车型
			if (po.getGroupLevel().intValue() == 3) {
				Map<String, Object> group1 = dao.getModelGroup(po.getGroupId(),
						Constant.WR_MODEL_GROUP_TYPE_01);// 索赔工时车型组
				Map<String, Object> group2 = dao.getModelGroup(po.getGroupId(),
						Constant.WR_MODEL_GROUP_TYPE_02);// 配件车型组
				act.setOutData("group1", group1);
				act.setOutData("group2", group2);
			}

			//List<Map<String, Object>> groups1 = dao.getModelGroupList(Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
			//List<Map<String, Object>> groups2 = dao.getModelGroupList(Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组
			
			//List<Map<String, Object>> colorList = dao.getMaterialGroupColor(groupId);
			
			act.setOutData("po", po);
			act.setOutData("parent", parent);
			//act.setOutData("groups1", groups1);
			//act.setOutData("groups2", groups2);
			//act.setOutData("colorList", colorList);
			//查询参数据传递
			act.setOutData("groupCode", groupCode);
			act.setOutData("groupName", groupName);
			act.setOutData("status", status);
			act.setOutData("parentGroupCode", parentGroupCode);
			act.setOutData("forcast_flag", forcast_flag);
			act.setOutData("page", page);
			
			act.setForword(MATERIAL_GROUP_MANAGE_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料组维护修改
	public void materialGroupManageMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));
			String parentGroupCode = CommonUtils.checkNull(request.getParamValue("parentGroupCode"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String ifType=request.getParamValue("ifType");
			
			//String colorCode[] = request.getParamValues("colorCode") == null ? new String[] {} : request.getParamValues("colorCode");
			//String colorName[] = request.getParamValues("colorName") == null ? new String[] {} : request.getParamValues("colorName");

			TmVhclMaterialGroupPO parent = new TmVhclMaterialGroupPO();
			parent.setGroupCode(parentGroupCode);
			parent = dao.getTmVhclMaterialGroupPO(parent);

			List<Map<String,Object>> groupList = dao.getAllMaterialGroup(groupId);
			if (null != groupList && groupList.size()>0) {
				for (int i = 0; i < groupList.size(); i++) {
					String group_Id = String.valueOf(groupList.get(i).get("GROUP_ID"));
					TmVhclMaterialGroupPO tempPO = new TmVhclMaterialGroupPO();
					tempPO.setGroupId(Long.parseLong(group_Id));
					
					TmVhclMaterialGroupPO valuePO = new TmVhclMaterialGroupPO();
					valuePO.setStatus(Integer.parseInt(status));
					if(ifType.equals(Constant.IF_TYPE_YES.toString())){
						valuePO.setForcastFlag(1);
					}else{
						valuePO.setForcastFlag(0);
					}
					dao.update(tempPO, valuePO);
				}
			}
			
			TmVhclMaterialGroupPO condition = new TmVhclMaterialGroupPO();
			condition.setGroupId(new Long(groupId));

			
			TmVhclMaterialGroupPO value = new TmVhclMaterialGroupPO();
			value.setGroupCode(groupCode);
			value.setGroupName(groupName);
			
			
			if (!parentGroupCode.equals("")) {
				value.setGroupLevel(new Integer(parent.getGroupLevel().intValue() + 1));
				String treeCode = dao.getMaterialGroupTreeCode(parent.getGroupId().toString());
				value.setTreeCode(treeCode);
				value.setParentGroupId(parent.getGroupId());
			} else {
				String treeCode = dao.getMaterialGroupTreeCode("");
				value.setTreeCode(treeCode);
				value.setGroupLevel(new Integer(1));
				value.setParentGroupId(null);
			}
			value.setStatus(new Integer(status));
			value.setIfStatus(new Integer(0));// 接口状态
			value.setUpdateDate(new Date());
			value.setUpdateBy(logonUser.getUserId());
			if(ifType.equals(Constant.IF_TYPE_YES.toString())){
				value.setForcastFlag(1);
			}else{
				value.setForcastFlag(0);
			}
			dao.update(condition, value);
			
//			if(colorCode.length>0){
//				dao.deleteColorByGroupId(groupId);
//				for (int i = 0; i < colorCode.length; i++) {
//						TmGroupColorPO colorPOValue = new TmGroupColorPO();
//						colorPOValue.setColorId(new Long(SequenceManager.getSequence("")));
//						colorPOValue.setGroupId(Long.parseLong(groupId));
//						colorPOValue.setColorCode(colorCode[i]);
//						colorPOValue.setColorName(colorName[i]);
//						colorPOValue.setCreateBy(logonUser.getUserId());
//						colorPOValue.setCreateDate(new Date(System.currentTimeMillis()));
//						colorPOValue.setUpdateBy(logonUser.getUserId());
//						colorPOValue.setUpdateDate(new Date(System.currentTimeMillis()));
//						dao.insert(colorPOValue); 
//				}
//			}
			

//			// 删除车型组车型关系
//			TtAsWrModelItemPO itemPo = new TtAsWrModelItemPO();
//			itemPo.setModelId(new Long(groupId));
//			dao.delete(itemPo);

//			// 判断上级物料组是否是车系
//			if (parent != null && parent.getGroupLevel().intValue() == 2) {
//				String modelGroup1 = CommonUtils.checkNull(request.getParamValue("modelGroup1"));
//				String modelGroup2 = CommonUtils.checkNull(request.getParamValue("modelGroup2"));
//
//				// 索赔工时车型组车型关系保存
//				itemPo = new TtAsWrModelItemPO();
//				itemPo.setModelId(new Long(groupId));
//				itemPo.setWrgroupId(new Long(modelGroup1));
//				itemPo.setCreateDate(new Date());
//				itemPo.setCreateBy(logonUser.getUserId());
//				dao.insert(itemPo);
//
//				// 配件车型组车型关系保存
//				itemPo = new TtAsWrModelItemPO();
//				itemPo.setModelId(new Long(groupId));
//				if(modelGroup2 != null && "".equals(modelGroup2)) {
//					itemPo.setWrgroupId(-1L);
//				} else if(modelGroup2 != null && !"".equals(modelGroup2)) {
//					itemPo.setWrgroupId(new Long(modelGroup2));
//				}
//				itemPo.setCreateDate(new Date());
//				itemPo.setCreateBy(logonUser.getUserId());
//				dao.insert(itemPo);
//			}

//			/* 此处需调用接口 */
//			OSA11 os = new OSA11();
//			os.execute(value);//接口
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.UPDATE_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
	// 物料组维护查询
	public void getModelGroup() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			Long companyId = logonUser.getCompanyId();
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));

			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			List<PO> list = dao.select(group);
			group = list.size() == 0 ? null : (TmVhclMaterialGroupPO) list
					.get(0);

			// 判断上级物料组是否是车系
			if (group != null && group.getGroupLevel().intValue() == 2) {
				List<Map<String, Object>> groups1 = dao.getModelGroupList(
						Constant.WR_MODEL_GROUP_TYPE_01, companyId);// 索赔工时车型组
				List<Map<String, Object>> groups2 = dao.getModelGroupList(
						Constant.WR_MODEL_GROUP_TYPE_02, companyId);// 配件车型组
				act.setOutData("returnValue", 1);
				act.setOutData("groups1", groups1);
				act.setOutData("groups2", groups2);
			} else {
				act.setOutData("returnValue", 2);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商产品套餐分配(维护物料组和经销商关系)-页面初始化
	 * @author HXY
	 * @update 2013-01-29
	 * */
	public void newDealerProductDistribution() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(NEW_DEALER_PRODECT_DISTRIBUTION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商产品套餐分配-查询
	 * @author HXY
	 * @update 2013-01-29
	 * */
	public void newDealerProductDistributionQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName = java.net.URLDecoder.decode(CommonUtils.checkNull(request.getParamValue("packageName")),"utf-8");
			String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("packageCode", packageCode);
			map.put("packageName", packageName);
			map.put("regionId", regionId);
			map.put("status", status);
			map.put("dealerId", dealerId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dao.getDealerProductPackageQuery(map, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
		
	/**
	 * 经销商产品套餐分配-维护
	 * @author HXY
	 * @update 2013-01-29
	 * */
	public void newDealerProductDisterbutionAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id = CommonUtils.checkNull(request.getParamValue("ID"));
			List<Map<String, Object>> detail = dao.getProdyctRegion(id);
			Map<String, Object> map = detail.get(0);
			act.setOutData("map", map);
			List<Map<String, Object>> dtlList = dao.getProductDistribution(id) ;
			
			if(!CommonUtils.isNullList(dtlList)) {
				StringBuffer theId = new StringBuffer("") ;
				StringBuffer theName = new StringBuffer("") ;
				StringBuffer theCode = new StringBuffer("") ;
				
				int len = dtlList.size() ;
				
				for(int i=0; i<len; i++) {
					int strLen = theId.length() ;
					
					if(strLen == 0) {
						theId.append(dtlList.get(i).get("COMPANY_ID")) ;
						theName.append(dtlList.get(i).get("COMPANY_NAME")) ;
						theCode.append(dtlList.get(i).get("COMPANY_CODE")) ;
					} else {
						theId.append(",").append(dtlList.get(i).get("COMPANY_ID")) ;
						theName.append(",").append(dtlList.get(i).get("COMPANY_NAME")) ;
						theCode.append(",").append(dtlList.get(i).get("COMPANY_CODE")) ;
					}
				}
				
				act.setOutData("theId", theId) ;
				act.setOutData("theCode", theCode) ;
				act.setOutData("theName", theName) ;
			}
			
			act.setOutData("ID", id);
			act.setForword(NEW_DEALER_PRODECT_DISTRIBUTION_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商产品套餐维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
