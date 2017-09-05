/**
 * FileName: MaterialPriceMaintenanceImport.java

 * Author: yudanwen
 * Date: 2013-4-7 下午02:47:55
 * Email: wyd_soul@163.com
 * 
 * Copyright ORARO Corporation 2013
 */

package com.infodms.dms.actions.sysproduct.productmanage;

import java.util.ArrayList;
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
import com.infodms.dms.dao.productmanage.MaterialPriceDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmpVsPriceDtlOemPO;
import com.infodms.dms.po.TmpVsPriceDtlPO;
import com.infodms.dms.util.CheckUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;



/**
 * @Description 物料价格维护导入
 * @author  ranjian
 * @date 2013-12-12 下午02:47:55
 * @version 2.0
 */
public class MaterialPriceMaintenanceImport extends BaseImport {
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final MaterialPriceDao dao = MaterialPriceDao.getInstance();
	public Logger logger = Logger.getLogger(MaterialPriceMaintenanceImport.class);
	private final String MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL = "/jsp/sysproduct/productmanage/materialPricecheckfailure.jsp";//导入失败页面
	private final String MATERIAL_PRICE_MANAGE_IMPORT_SUCCESS_URL = "/jsp/sysproduct/productmanage/materialPriceManageImportSuccess.jsp";//查询临时表数据页面
	private final String MATERIAL_PRICE_MANAGE_IMPORT_COMPLETE_URL = "/jsp/sysproduct/productmanage/materialPriceManageImportComplete.jsp";

	/**
	 * 物料价格维护导入临时表
	 */
	public void materialPriceMaintenanceInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
        try {
        	RequestWrapper request = act.getRequest();
        	TmpVsPriceDtlOemPO po= new TmpVsPriceDtlOemPO();
        	po.setUserId(logonUser.getUserId());
			//清空临时表中的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",2,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpYearlyPlan(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=checkData(logonUser.getUserId());
		
					if (null != errorList) {
						act.setOutData("errorList", errorList);
						act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_FAILURE_URL);
					}else{
						act.setForword(MATERIAL_PRICE_MANAGE_IMPORT_SUCCESS_URL);
					}
					
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 把所有导入记录插入TMP_YEARLY_PLAN
	 */
	private void insertTmpYearlyPlan(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCells(key, cells, userId);
			}
		}
		
	}
	
	/**
	 * 查询临时表数据
	 */
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
			PageResult<Map<String, Object>> ps = dao.getMaterialPriceManageImportTempList(map, curPage,9999);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料价格导入");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 每一行插入TMP_YEARLY_PLAN
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{

		
		TmpVsPriceDtlOemPO po= new TmpVsPriceDtlOemPO();
		po.setNumberNo(rowNum.trim());
		po.setUserId(userId);
		po.setGroupCode(cells[0].getContents());
		if(cells.length>1){
			po.setSalesPrice(cells[1].getContents().trim());
		}
		MaterialPriceDao dao=MaterialPriceDao.getInstance();
        dao.insert(po);	
	}
	
	/*
	 * 校验TMP_VS_PRICE_DTL表中数据是否符合导入标准
	 * GROUP_CODE 是否存在,是否重复
	 * GROUP_CODE 存在且物料状态为10011001执行更新操作
	 *
	 *return 错误行号 
	 */
	private List<ExcelErrors> checkData(Long userId) {
		
		TmpVsPriceDtlOemPO pricePo = new TmpVsPriceDtlOemPO();
		pricePo.setUserId(userId);
		List<TmpVsPriceDtlOemPO> list = dao.selectTmpVsPriceDtl(pricePo);

		ExcelErrors errors = null;
		TmpVsPriceDtlOemPO po = null;
		StringBuffer errorInfo = new StringBuffer("");
		boolean isError = false;

		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for (int i = 0; i < list.size(); i++) {
			errors = new ExcelErrors();
			// 取得TmpVsPriceDtlOemPO
			po = (TmpVsPriceDtlOemPO) list.get(i);
			// 取得行号
			String rowNum = po.getNumberNo();
			// 校验物料价格
			if (CheckUtil.checkNull(po.getSalesPrice())){
				isError = true;
				errorInfo.append("物料价格不能为空!");
			//code存在 且物料状态为10011001
			}
			if (!CheckUtil.checkNull(po.getSalesPrice()) && !CheckUtil.checkFormatNumber1(po.getSalesPrice())) {
				isError = true;
				errorInfo.append("物料价格没有保留2位小数,且第一位不能为0,");
			}
			// 校验物料code是否为空
			
			if (CheckUtil.checkNull(po.getGroupCode())){
				isError = true;
				errorInfo.append("物料编码格式不能为空!");
			//code存在 且物料状态为10011001
			}
			if(""!=po.getGroupCode()&&null!=po.getGroupCode()){
//				List<Map<String, Object>> notExistsMaterialList1= dao.quotaImportCheckMaterial(userId.toString(),po.getGroupCode());
//				if (null != notExistsMaterialList1 && notExistsMaterialList1.size() > 0) {
//					for (int j = 0; j < notExistsMaterialList1.size(); j++) {
//						Map<String, Object> map = notExistsMaterialList1.get(j);
//						isError = true;
//						ExcelErrors err = new ExcelErrors();
//						err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
//						err.setErrorDesc("物料代码不存在");
//						errorList.add(err);
//					}
//				}
				List <Map<String,Object>> isExist=dao.quotaImportCheckMaterial(po.getGroupCode());
				if(isExist.size()<=0||isExist==null){
					isError = true;
					errorInfo.append("物料编码不存在");
				}
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
		// 配额导入校验物料代码是否存在
		List<Map<String, Object>> notExistsMaterialList = dao.MaterialManagegroupcode2(conMap);
		if (null != notExistsMaterialList && notExistsMaterialList.size() > 0) {
			for (int i = 0; i < notExistsMaterialList.size(); i++) {
				Map<String, Object> map = notExistsMaterialList.get(i);
				isError = true;
				ExcelErrors err = new ExcelErrors();
				err.setRowNum(Integer.parseInt(map.get("ROW_NUMBER").toString()));
				err.setErrorDesc("物料编码在物料表不存在,请修改!");
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
