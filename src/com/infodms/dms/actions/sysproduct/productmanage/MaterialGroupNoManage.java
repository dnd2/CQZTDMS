package com.infodms.dms.actions.sysproduct.productmanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.productmanage.ProductManageNewDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSA12;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class MaterialGroupNoManage {
	private Logger logger = Logger.getLogger(MaterialGroupNoManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final ProductManageNewDao dao = ProductManageNewDao.getInstance();

	private final String MATERIAL_GROUP_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialGroupNoManageQuery.jsp";// 物料组维护查询页面
	private final String MATERIAL_GROUP_MANAGE_ADD_URL = "/jsp/sysproduct/productmanage/materialGroupNoManageAdd.jsp";// 物料组维护新增页面
	private final String MATERIAL_GROUP_MANAGE_MOD_URL = "/jsp/sysproduct/productmanage/materialGroupNoManageMod.jsp";// 物料组维护修改页面
	
	// 物料组维护prematerialManageQueryPre
	public void materialGroupManageQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MATERIAL_GROUP_MANAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料维护查询
	public void materialManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request
					.getParamValue("materialName"));
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			String companyId = logonUser.getCompanyId().toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("materialCode", materialCode);
			map.put("materialName", materialName);
			map.put("status", status);
			map.put("companyId", companyId);
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getMaterialManageQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料维护新增pre
	public void materialManageAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MATERIAL_GROUP_MANAGE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料维护新增
	public void materialManageAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request
					.getParamValue("materialName"));
			String modelCode = CommonUtils.checkNull(request
					.getParamValue("modelCode"));
			String trimCode = CommonUtils.checkNull(request
					.getParamValue("trimCode"));
			String colorCode = CommonUtils.checkNull(request
					.getParamValue("colorCode"));
			String colorName = CommonUtils.checkNull(request
					.getParamValue("colorName"));
			String modelYear = CommonUtils.checkNull(request
					.getParamValue("modelYear"));
			String issueDate = CommonUtils.checkNull(request
					.getParamValue("issueDate"));
			String enableDate = CommonUtils.checkNull(request
					.getParamValue("enableDate"));
			String disableDate = CommonUtils.checkNull(request
					.getParamValue("disableDate"));
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			String rushOrderFlag = CommonUtils.checkNull(request
					.getParamValue("rushOrderFlag"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));

			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			group = dao.getTmVhclMaterialGroupPO(group);

			// 物料保存
			TmVhclMaterialPO po = new TmVhclMaterialPO();
			po.setMaterialId(new Long(SequenceManager.getSequence("")));
			po.setMaterialCode(materialCode);
			po.setMaterialName(materialName);
			po.setModelCode(modelCode);
			po.setTrimCode(trimCode);
			po.setColorCode(colorCode);
			po.setColorName(colorName);
			po.setModelYear(modelYear);
			po.setIssueDate(DateTimeUtil.stringToDate(issueDate));
			po.setEnableDate(DateTimeUtil.stringToDate(enableDate));
			po.setDisableDate(DateTimeUtil.stringToDate(disableDate));
			po.setStatus(new Integer(status));
			po.setRushOrderFlag(new Integer(rushOrderFlag));
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			po.setCompanyId(logonUser.getCompanyId());

			dao.insert(po);

			// 物料与物料组关系保存
			TmVhclMaterialGroupRPO rpo = new TmVhclMaterialGroupRPO();
			rpo.setId(new Long(SequenceManager.getSequence("")));
			rpo.setMaterialId(po.getMaterialId());
			rpo.setGroupId(group.getGroupId());
			rpo.setCreateDate(new Date());
			rpo.setCreateBy(logonUser.getUserId());

			dao.insert(rpo);
			//调接口 Start
			OSA12 o = new OSA12();
			o.execute(po);
			//调接口 End
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料维护修改pre
	public void materialManageModPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String materialId = CommonUtils.checkNull(request
					.getParamValue("materialId"));
			// 物料po
			TmVhclMaterialPO po = new TmVhclMaterialPO();
			po.setMaterialId(new Long(materialId));
			po = dao.getTmVhclMaterialPO(po);

			String issueDate = DateTimeUtil.parseDateToDate(po.getIssueDate());
			String enableDate = DateTimeUtil
					.parseDateToDate(po.getEnableDate());
			String disableDate = DateTimeUtil.parseDateToDate(po
					.getDisableDate());

			act.setOutData("po", po);
			act.setOutData("issueDate", issueDate);
			act.setOutData("enableDate", enableDate);
			act.setOutData("disableDate", disableDate);
			act.setForword(MATERIAL_GROUP_MANAGE_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料维护修改
	public void materialManageMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String materialId = CommonUtils.checkNull(request
					.getParamValue("materialId"));
			String materialCode = CommonUtils.checkNull(request
					.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request
					.getParamValue("materialName"));
			String modelCode = CommonUtils.checkNull(request
					.getParamValue("modelCode"));
			String trimCode = CommonUtils.checkNull(request
					.getParamValue("trimCode"));
			String colorCode = CommonUtils.checkNull(request
					.getParamValue("colorCode"));
			String colorName = CommonUtils.checkNull(request
					.getParamValue("colorName"));
			String modelYear = CommonUtils.checkNull(request
					.getParamValue("modelYear"));
			String issueDate = CommonUtils.checkNull(request
					.getParamValue("issueDate"));
			String enableDate = CommonUtils.checkNull(request
					.getParamValue("enableDate"));
			String disableDate = CommonUtils.checkNull(request
					.getParamValue("disableDate"));
			String status = CommonUtils.checkNull(request
					.getParamValue("status"));
			String rushOrderFlag = CommonUtils.checkNull(request
					.getParamValue("rushOrderFlag"));
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String orderFlag = CommonUtils.checkNull(request
					.getParamValue("orderFlag"));

			// 获得物料po
			TmVhclMaterialPO material = new TmVhclMaterialPO();
			material.setMaterialId(new Long(materialId));
			material = dao.getTmVhclMaterialPO(material);

			// 获得物料与物料组关系po
			TmVhclMaterialGroupRPO rpo = new TmVhclMaterialGroupRPO();
			rpo.setMaterialId(new Long(materialId));
			rpo = dao.getTmVhclMaterialGroupRPO(rpo);

			// 获得物料组po
			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			group = dao.getTmVhclMaterialGroupPO(group);

			// 物料更新
			TmVhclMaterialPO mCondition = new TmVhclMaterialPO();
			mCondition.setMaterialId(material.getMaterialId());

			TmVhclMaterialPO mValue = new TmVhclMaterialPO();
			mValue.setMaterialCode(materialCode);
			mValue.setMaterialName(materialName);
			mValue.setModelCode(modelCode);
			mValue.setTrimCode(trimCode);
			mValue.setColorCode(colorCode);
			mValue.setColorName(colorName);
			mValue.setModelYear(modelYear);
			mValue.setIssueDate(DateTimeUtil.stringToDate(issueDate));
			mValue.setEnableDate(DateTimeUtil.stringToDate(enableDate));
			mValue.setDisableDate(DateTimeUtil.stringToDate(disableDate));
			mValue.setStatus(new Integer(status));
			mValue.setRushOrderFlag(new Integer(rushOrderFlag));
			mValue.setUpdateDate(new Date());
			mValue.setUpdateBy(logonUser.getUserId());
			mValue.setOrderFlag(new Integer(orderFlag)) ;

			dao.update(mCondition, mValue);

			// 物料与物料组关系更新
			TmVhclMaterialGroupRPO rCondition = new TmVhclMaterialGroupRPO();
			rCondition.setId(rpo.getId());

			TmVhclMaterialGroupRPO rValue = new TmVhclMaterialGroupRPO();
			rValue.setGroupId(group.getGroupId());
			rValue.setUpdateDate(new Date());
			rValue.setUpdateBy(logonUser.getUserId());

			dao.update(rCondition, rValue);
			//调接口 Start
			OSA12 o = new OSA12();
			o.execute(mCondition);
			//调接口 End
			act.setOutData("returnValue", 1);


		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void setLink() {
		String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")) ;
		String seriesCode = CommonUtils.checkNull(request.getParamValue("seriesCode")) ;
		
		String[] groupCode = materialCode.split("\\.") ;
		String modelCode = groupCode[0] ;	// 车型
		
		List<Map<String, Object>> materialList = dao.getErpMaterial(modelCode) ;
		
		if(!CommonUtils.isNullList(materialList)) {
			int len = materialList.size() ;
			
			for(int i=0; i<len; i++) {
				String[] aboutInfo = setLink_N(materialList.get(i).get("MATERIAL_CODE").toString(), seriesCode) ;
				
				act.setOutData("materialCode", aboutInfo[1]) ;
				act.setOutData("flagInt", aboutInfo[0]) ;
				
				if(!"1".equals(aboutInfo[0])) {
					break ;
				}
			}
		} else {
			act.setOutData("modelCode", modelCode) ;
			act.setOutData("flagInt", "-5") ;	// -5：表示对应物料所属车型下不存在无关联物料
		}
	}
	
	public String[] setLink_N(String materialCode, String seriesCode) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String[] aboutInfo = new String[2] ;
		
		String flagInt = "1" ;	// 1：表示关联成功
		
		if(CommonUtils.isNullString(materialCode)) {
			flagInt = "0" ;	// 0：表示物料不存在
		} else {
			String materialId = ProductManageNewDao.getMaterialId(materialCode) ;
			
			if(!CommonUtils.isNullString(materialId)) {
				String[] groupCode = materialCode.split("\\.") ;
				
				int len = groupCode.length ;
				
				if(len == 3) {
					String modelCode = groupCode[0] ;	// 车型
					String packageCode = groupCode[0] + "." + groupCode[1] ;	// 配置
					
					if(!ProductManageNewDao.groupIsExists(modelCode, "3")) {
						if(!CommonUtils.isNullString(seriesCode)) {
							if(ProductManageNewDao.groupIsExists(seriesCode, "2")) {
								ProductManageNewDao.insModel(modelCode, seriesCode, logonUser.getUserId(), logonUser.getCompanyId()) ;
							} else {
								flagInt = "-4" ;	// -4：表示车系不存在，需要确认车系新建车型
							}
						} else {
							flagInt = "-2" ;	// -2：表示车型不存在，需要确认车系新建车型
						}
					} 
					
					if (flagInt != "-4" && flagInt != "-2"){
						if(!ProductManageNewDao.groupIsExists(packageCode, "4")) {
							ProductManageNewDao.insPackage(packageCode, modelCode, logonUser.getUserId(), logonUser.getCompanyId()) ;
						}
						
						if(!ProductManageNewDao.materialIsExistsR(materialId)) {
							ProductManageNewDao.insLink(materialId, packageCode, logonUser.getUserId()) ;
							
							updateVmat(materialId) ;
						} else {
							flagInt = "-3" ;	// -3：表示关联已存在
						}
					}
				} else {
					flagInt = "-1" ;	// -1：表示物料编码错误
				}
			} else {
				flagInt = "0" ;	// 0：表示物料不存在
			}
		}
		
		aboutInfo[0] = flagInt ;
		aboutInfo[1] = materialCode ;
		
		return aboutInfo ;
	}
	
	public void updateVmat(String materialId) {
		ProductManageNewDao.updateVPacakge(materialId) ;
		ProductManageNewDao.updateVModel(materialId) ;
		ProductManageNewDao.updateVSeries(materialId) ;
	}
}