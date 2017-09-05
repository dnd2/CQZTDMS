/**
 * 
 */
package com.infodms.dms.actions.sales.oemstorage;

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
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.oemstorage.OemStorageManageDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author Administrator
 * 
 */
public class OrderResourceReserveQuery {

	public Logger logger = Logger.getLogger(OrderResourceReserveQuery.class);
	private final OemStorageManageDao dao = OemStorageManageDao.getInstance();
	private final OrderAuditDao auditDao = OrderAuditDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String ORDER_RESOURCE_RESERVE_QUERY_URL = "/jsp/sales/oemstorage/oemOrderResourceReserveQuery.jsp";

	/**
	 * 资源保留查询页面初始化
	 */
	public void orderResourceReserveQueryPre() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Long poseId = logonUser.getPoseId();
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			List<Map<String, Object>> warehouseList = auditDao.getWareHouseList(companyId
					.toString(), areaIds);
			List<Map<String, Object>> areaBusList = MaterialGroupManagerDao
					.getPoseIdBusiness(logonUser.getPoseId().toString());
			List<Map<String, Object>> batchNOList = auditDao.getBatchNOList();
			act.setOutData("warehouseList", warehouseList);
			act.setOutData("areaBusList", areaBusList);
			act.setOutData("batchNOList", batchNOList);
			act.setForword(ORDER_RESOURCE_RESERVE_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"保留资源查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 资源保留查询
	 */
	public void orderResourceReserveQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode"); // 物料CODE
			String batchNo = request.getParamValue("batchNo"); // 批次号
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("warehouseId", warehouseId);
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("batchNo", batchNo);
			map.put("companyId", companyId.toString());
			map.put("areaIds", areaIds);

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderResourceReserveQueryList(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"保留资源查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 资源保留查询--下载
	 */
	public void orderResourceReserveExport() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			String warehouseId = request.getParamValue("warehouseId"); // 仓库ID
			String groupCode = request.getParamValue("groupCode"); // 物料组CODE
			String materialCode = request.getParamValue("materialCode"); // 物料CODE
			String batchNo = request.getParamValue("batchNo"); // 批次号
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());

			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("warehouseId", warehouseId);
			paraMap.put("groupCode", groupCode);
			paraMap.put("materialCode", materialCode);
			paraMap.put("batchNo", batchNo);
			paraMap.put("companyId", companyId.toString());
			paraMap.put("areaIds", areaIds);

			// 导出的文件名
			String fileName = "资源保留.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("仓库名称");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("批次号");
			listTemp.add("保留量");
			listTemp.add("订单号");
			listTemp.add("开票经销商");
			listTemp.add("订单状态");
			list.add(listTemp);
			// 将page对象转换成LIST形式
			List<Map<String, Object>> rslist = dao.getOrderResourceReserveExportList(paraMap);
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(CommonUtils.checkNull(map.get("WAREHOUSE_NAME")));
				listValue.add(CommonUtils.checkNull(map.get("MATERIAL_CODE")));
				listValue.add(CommonUtils.checkNull(map.get("MATERIAL_NAME")));
				listValue.add(CommonUtils.checkNull(map.get("BATCH_NO")));
				listValue.add(CommonUtils.checkNull(map.get("AMOUNT")));
				listValue.add(CommonUtils.checkNull(map.get("ORDER_NO")));
				listValue.add(CommonUtils.checkNull(map.get("BEALER_NAME")));
				listValue.add(CommonUtils.checkNull(map.get("ORDER_STATUS")));
				list.add(listValue);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"保留资源查询下载");
			logger.error(logonUser, e1);
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
}
