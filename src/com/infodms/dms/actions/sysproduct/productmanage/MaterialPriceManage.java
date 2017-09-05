/**
 * @Title: MaterialPriceManage.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-5
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmpVsPriceDtlPO;
import com.infodms.dms.po.TtVsPricePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class MaterialPriceManage extends BaseImport {
	private Logger logger = Logger.getLogger(MaterialPriceManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final ProductManageDao dao = ProductManageDao.getInstance();

	private final String MATERIAL_PRICE_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialPriceManageQuery.jsp";// 物料价格维护查询页面
	private final String MATERIAL_PRICE_MANAGE_TYPE_QUERY_URL = "/jsp/sysproduct/productmanage/materialPriceManageTypeQuery.jsp";// 物料价格类型维护查询页面
	private final String MATERIAL_PRICE_MANAGE_TYPE_ADD_URL = "/jsp/sysproduct/productmanage/materialPriceManageTypeAdd.jsp";// 物料价格类型维护新增页面
	private final String MATERIAL_PRICE_MANAGE_TYPE_MOD_URL = "/jsp/sysproduct/productmanage/materialPriceManageTypeMod.jsp";// 物料价格类型维护修改页面
	private final String MATERIAL_PRICE_MANAGE_IMPORT_URL = "/jsp/sysproduct/productmanage/materialPriceManageImport.jsp";// 物料价格维护导入页面
	private final String MATERIAL_PRICE_MANAGE_IMPORT_SUCCESS_URL = "/jsp/sysproduct/productmanage/materialPriceManageImportSuccess.jsp";
	private final String MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL = "/jsp/sysproduct/productmanage/materialPriceManageImportFailure.jsp";
	private final String MATERIAL_PRICE_MANAGE_IMPORT_COMPLETE_URL = "/jsp/sysproduct/productmanage/materialPriceManageImportComplete.jsp";

	// 物料价格维护pre
	public void materialPriceManageQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List<Map<String, Object>> types = dao.getPriceTypeList(logonUser
					.getCompanyId());

			act.setOutData("types", types);
			act.setForword(MATERIAL_PRICE_MANAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格维护查询
	public void materialPriceManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));
			String priveDesc=CommonUtils.checkNull(request.getParamValue("priceDesc"));
			String companyId = logonUser.getCompanyId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("groupId", groupId);
			map.put("priceId",priceId);
			map.put("companyId", companyId);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialPriceManageQueryList(map, curPage, Constant.PAGE_SIZE,priveDesc);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护查询
	public void materialPriceManageTypeQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String command = CommonUtils.checkNull(request
					.getParamValue("command"));
			if (command.equals("1")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("companyId", logonUser.getCompanyId().toString());
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao
						.getMaterialPriceManageTypeQueryList(map, curPage,
								Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			act.setForword(MATERIAL_PRICE_MANAGE_TYPE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护新增pre
	public void materialPriceManageTypeAddPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MATERIAL_PRICE_MANAGE_TYPE_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护新增
	public void materialPriceManageTypeAdd() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String priceCode = CommonUtils.checkNull(request
					.getParamValue("priceCode"));
			String priceDesc = CommonUtils.checkNull(request
					.getParamValue("priceDesc"));
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate"));

			// 价格保存
			TtVsPricePO po = new TtVsPricePO();
			po.setPriceId(new Long(SequenceManager.getSequence("")));
			po.setPriceCode(priceCode);
			po.setPriceDesc(priceDesc);
			po.setStartDate(DateTimeUtil.stringToDate(startDate));
			po.setEndDate(DateTimeUtil.stringToDate(endDate));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			po.setCompanyId(logonUser.getCompanyId());

			dao.insert(po);

			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护修改pre
	public void materialPriceManageTypeModPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String priceId = CommonUtils.checkNull(request
					.getParamValue("priceId"));

			TtVsPricePO po = new TtVsPricePO();
			po.setPriceId(new Long(priceId));
			List<PO> list = dao.select(po);
			po = (TtVsPricePO) list.get(0);

			String startDate = DateTimeUtil.parseDateToDate(po.getStartDate());
			String endDate = DateTimeUtil.parseDateToDate(po.getEndDate());

			act.setOutData("po", po);
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(MATERIAL_PRICE_MANAGE_TYPE_MOD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护修改
	public void materialPriceManageTypeMod() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String priceId = CommonUtils.checkNull(request
					.getParamValue("priceId"));
			String priceCode = CommonUtils.checkNull(request
					.getParamValue("priceCode"));
			String priceDesc = CommonUtils.checkNull(request
					.getParamValue("priceDesc"));
			String startDate = CommonUtils.checkNull(request
					.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request
					.getParamValue("endDate"));

			TtVsPricePO condition = new TtVsPricePO();
			condition.setPriceId(new Long(priceId));

			TtVsPricePO value = new TtVsPricePO();
			value.setPriceCode(priceCode);
			value.setPriceDesc(priceDesc);
			value.setStartDate(DateTimeUtil.stringToDate(startDate));
			value.setEndDate(DateTimeUtil.stringToDate(endDate));
			value.setUpdateBy(logonUser.getUserId());
			value.setUpdateDate(new Date());

			dao.update(condition, value);

			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格类型维护删除
	public void materialPriceManageTypeDel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String[] ids = request.getParamValues("priceId");
			if (ids != null && ids.length != 0) {
				for (int i = 0; i < ids.length; i++) {
					Long id = new Long(ids[i]);
					TtVsPricePO po = new TtVsPricePO();
					po.setPriceId(id);
					dao.delete(po);
				}
			}
			act.setOutData("returnValue", 1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格导入pre
	public void materialPriceManageImportPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> types = dao.getPriceTypeList(logonUser.getCompanyId());
			act.setOutData("types", types);
			act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// 物料价格导入临时表
	public void materialPriceManageExcelOperate() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));

			// 清空临时表中的数据
			TmpVsPriceDtlPO po = new TmpVsPriceDtlPO();
			po.setUserId(logonUser.getUserId());
			dao.delete(po);

			long maxSize = 1024 * 1024 * 5;
			insertIntoTmp(request, "uploadFile", 3, 3, maxSize);

			List<ExcelErrors> el = getErrList();
			if (null != el && el.size() > 0) {
				act.setOutData("errorList", el);
				act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL);
			} else {
				List<Map> list = getMapList();
				// 将数据插入临时表
				insertTmp(list, new Long(priceId), logonUser.getUserId());
				// 校验临时表数据
				List<ExcelErrors> errorList = checkData(logonUser.getUserId(), logonUser.getCompanyId());
				if (null != errorList) {
					act.setOutData("errorList", errorList);
					act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL);
				} else {
					act.setOutData("priceId", priceId);
					act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_SUCCESS_URL);
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE, "文件读取错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 把所有导入记录插入TMP_VS_PRICE_DTL
	 */
	private void insertTmp(List<Map> list, Long priceId, Long userId)
			throws Exception {
		if (null == list) {
			list = new ArrayList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(key, cells, priceId, userId);
			}
		}

	}

	/*
	 * 每一行插入TMP_VS_PRICE_DTL 数字只截取30位
	 */
	private void parseCells(String rowNum, Cell[] cells, Long priceId,
			Long userId) throws Exception {

		// 获得价格po
		TtVsPricePO pricePo = new TtVsPricePO();
		pricePo.setPriceId(priceId);
		List<PO> list = dao.select(pricePo);
		pricePo = list.size() != 0 ? (TtVsPricePO) list.get(0) : null;

		// 临时表保存
		TmpVsPriceDtlPO po = new TmpVsPriceDtlPO();
		po.setNumberNo(rowNum.trim());
		po.setPriceCode(pricePo.getPriceCode());
		po.setGroupCode(subCell(cells[0].getContents().trim()));
		po.setSalesPrice(subCell(cells[1].getContents().trim()));
		po.setNoTaxPrice(subCell(cells[2].getContents().trim()));
		po.setUserId(userId);
		dao.insert(po);
	}

	/*
	 * 将输入字符截取最多30位
	 */
	private String subCell(String orgAmt) {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}

	/*
	 * 校验临时表中数据是否符合导入标准
	 */
	private List<ExcelErrors> checkData(Long userId, Long companyId) {
		TmpVsPriceDtlPO pricePo = new TmpVsPriceDtlPO();
		pricePo.setUserId(userId);
		List<PO> list = dao.select(pricePo);

		ExcelErrors errors = null;
		TmpVsPriceDtlPO po = null;
		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;

		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for (int i = 0; i < list.size(); i++) {
			errors = new ExcelErrors();
			// 取得TmpVsPriceDtlPO
			po = (TmpVsPriceDtlPO) list.get(i);
			// 取得行号
			String rowNum = po.getNumberNo();
			// 校验销售价格
			if (!CheckUtil.checkFormatNumber(po.getSalesPrice())) {
				isError = true;
				errorInfo.append("销售价格不是数字,");
			}
			// 校验不含税价格
			if (!CheckUtil.checkFormatNumber(po.getNoTaxPrice())) {
				isError = true;
				errorInfo.append("不含税价格不是数字,");
			}
			if (errorInfo.length() > 0) {
				String info = errorInfo.substring(0, errorInfo.length() - 1);
				errors.setRowNum(new Integer(rowNum));
				errors.setErrorDesc(info);
				errorList.add(errors);
				errorInfo.delete(0, errorInfo.length());
			}
		}

		Map<String, Object> conMap = new HashMap<String, Object>();
		conMap.put("userId", userId.toString());
		conMap.put("companyId", companyId.toString());
		// 配额导入校验物料代码是否存在
		List<Map<String, Object>> notExistsMaterialList = dao.quotaImportCheckMaterial(conMap);
		if (null != notExistsMaterialList && notExistsMaterialList.size() > 0) {
			for (int i = 0; i < notExistsMaterialList.size(); i++) {
				Map<String, Object> map = notExistsMaterialList.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("配置代码不存在");
				errorList.add(err);
			}
		}
		// 临时表校验重复数据
		List<Map<String, Object>> dumpList = dao.talbeCheckDump(conMap);
		if (null != dumpList && dumpList.size() > 0) {

			String r1 = "";
			String r2 = "";
			List<String> tmp = new ArrayList<String>();
			String s1 = "";
			String s2 = "";
			for (int i = 0; i < dumpList.size(); i++) {
				Map<String, Object> map = dumpList.get(i);
				r1 = map.get("ROW_NUMBER1").toString();
				r2 = map.get("ROW_NUMBER2").toString();
				s1 = r1 + "," + r2;
				s2 = r2 + "," + r1;
				if (tmp.contains(s1) || tmp.contains(s2)) {
					continue;
				} else {
					isError = true;
					ExcelErrors err = new ExcelErrors();
					err.setRowNum(Integer.parseInt(r1));
					err.setErrorDesc("与" + r2 + "行数据重复");
					errorList.add(err);
					tmp.add(s1);
				}

			}
		}

		if (isError) {
			return errorList;
		} else {
			return null;
		}
	}

	public void materialPriceManageTempQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String userId = logonUser.getUserId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);

			// 配额计算查询
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialPriceManageImportTempList(map, curPage,Constant.PAGE_SIZE_MAX);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * 导入业务表
	 */
	public void importExcel() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String userId = logonUser.getUserId().toString();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId", userId);

			// 查询要插入到业务表的临时表数据
			dao.insertTtVsPriceDtl(map);
			act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_COMPLETE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "配置价格导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
