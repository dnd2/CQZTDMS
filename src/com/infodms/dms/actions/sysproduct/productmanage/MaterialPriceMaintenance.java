
package com.infodms.dms.actions.sysproduct.productmanage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.Util;
import com.infodms.dms.dao.productmanage.MaterialPriceDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;



/**
 * @Description 物料价格维护(车厂价格)
 * @author  ranj
 * @date 2013-12-12 上午11:59:09
 * @version 2.0
 */
public class MaterialPriceMaintenance {
	private Logger logger = Logger.getLogger(MaterialPriceMaintenance.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final MaterialPriceDao dao = MaterialPriceDao.getInstance();

	private final String MATERIAL_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialPriceMaintenanceQuery.jsp";// 物料价格查询页面
	private final String MATERIAL_Imp_QUERY_URL = "/jsp/sysproduct/productmanage/MaterialPriceimport.jsp";// 物料价格导入页面
	private final String MATERIAL_EDIT_URL = "/jsp/sysproduct/productmanage/editMat.jsp";// 物料价格修改页面

	/**
	 * 加载物料价格维护
	 */
	public void MaterialPriceInit(){
		
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);
			act.setForword(MATERIAL_MANAGE_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 物料价格修改初始化
	 */
	public void editMatInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String matId=CommonUtils.checkNull(request.getParamValue("matId"));
			Map<String,Object> map=dao.getMat(matId);
			act.setOutData("map", map);
			act.setForword(MATERIAL_EDIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 物料价格修改
	 */
	public void editMat(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id=CommonUtils.checkNull(request.getParamValue("MATERIAL_ID"));
			String vhcl_price=CommonUtils.checkNull(request.getParamValue("VHCL_PRICE"));
			TmVhclMaterialPO t =new TmVhclMaterialPO();
			t.setMaterialId(Long.parseLong(material_id));
			TmVhclMaterialPO t1 =new TmVhclMaterialPO();
			t1.setComVhclPrice(Double.parseDouble(vhcl_price));
			dao.update(t, t1);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 加载导入页面
	 */
	public void materialManageImportInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(MATERIAL_Imp_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料导入（车厂价）");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料价格查询
	 */
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
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			String companyId = logonUser.getCompanyId().toString();
			String areaId=Util.notNull(request.getParamValue("YIELDLY"));

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("materialCode", materialCode);
			map.put("materialName", materialName);
			map.put("status", status);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("areaId", areaId);
			map.put("poseId", logonUser.getPoseId().toString());

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1;
			PageResult<Map<String, Object>> ps = dao
					.getMaterialManageQueryList(map, curPage,
							Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "物料查询（车厂价）");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
         

	/**
	 * 导入模板下载
	 * @author wenyd
	 * @since  2010-08-20
	 */
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		YearPlanDao dao=YearPlanDao.getInstance();
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
			// 区域组织id
			String orgId=String.valueOf(logonUser.getOrgId());
			// 公司ID
			String companyId = String.valueOf(logonUser.getCompanyId());
			String areaId = request.getParamValue("buss_area");
			
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("物料代码");
			listHead.add("物料价格");
			
			list.add(listHead);
			
			
			// 导出的文件名
			String fileName = "物料价格维护导入模板(车厂价).xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		}
		/**
		 * 导出
		 * @author wenyd
		 * @since  2010-08-20
		 */
		public void resourcesAuditDown(){
			OutputStream os = null;
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String areaIds = MaterialGroupManagerDao
							.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			try{
				
				String materialCode = CommonUtils.checkNull(request
						.getParamValue("materialCode"));
				String materialName = CommonUtils.checkNull(request
						.getParamValue("materialName"));
				String status = CommonUtils.checkNull(request
						.getParamValue("status"));
				String groupCode = CommonUtils.checkNull(request
						.getParamValue("groupCode"));
				String yieldly = CommonUtils.checkNull(request
						.getParamValue("YIELDLY"));
				String companyId = logonUser.getCompanyId().toString();
				
				

				Map<String, Object>  map = new HashMap<String, Object>();
				map.put("materialCode", materialCode);
				map.put("materialName", materialName);
				map.put("status", status);
				map.put("groupCode", groupCode);
				map.put("companyId", companyId);
				map.put("areaId", yieldly);

				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1;
				PageResult<Map<String, Object>> ps = dao
						.getMaterialManageQueryList(map, curPage,
								9999);
					act.setOutData("ps", ps);
					
					
					ResponseWrapper response = act.getResponse();
					// 导出的文件名
					String fileName = "物料价格维护.csv";
					// 导出的文字编码
					fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
					response.setContentType("Application/text/csv");
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
					// 定义一个集合
					List<List<Object>> list = new LinkedList<List<Object>>();
					// 标题
					List<Object> listTemp = new LinkedList<Object>();
					
					listTemp.add("物料代码");
					listTemp.add("物料名称");
					listTemp.add("物料价格");
					listTemp.add("状态");
					list.add(listTemp);
					List<Map<String, Object>> rslist = ps.getRecords();
					if(rslist!=null){
					for (int i = 0; i < rslist.size(); i++) {
						map = rslist.get(i);
						List<Object> listValue = new LinkedList<Object>();
						listValue = new LinkedList<Object>();
						
						listValue.add(map.get("MATERIAL_CODE") != null ? map.get("MATERIAL_CODE") : "");
						listValue.add(map.get("MATERIAL_NAME") != null ? map.get("MATERIAL_NAME") : "");
						listValue.add(map.get("VHCL_PRICE") != null ? map.get("VHCL_PRICE") : "");
						String Status=null;
						if(map.get("STATUS") != null){
							if(Integer.parseInt(map.get("STATUS").toString())==Constant.STATUS_ENABLE){
								Status="有效";
							}else{
								Status="无效";
							}
							
						}else {
							Status="";
							
						}
						listValue.add(Status);
						list.add(listValue);
						System.out.println(map.get("UPDATEDATE"));
						
					}
					}
					os = response.getOutputStream();
					CsvWriterUtil.writeCsv(list, os);
					os.flush();			
			} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"物料价格维护下载失败");
				logger.error(logonUser,e1);
				act.setException(e1);
				}
			
			
		
}
	}
