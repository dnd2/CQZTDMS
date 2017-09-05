package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.ordermanage.resourceQuery.ResourceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ResourceQuery extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(ResourceQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final ResourceQueryDAO dao = new ResourceQueryDAO ();
	public static final ResourceQueryDAO getInstance() {
		return dao;
	}
	private final String  resourceQueryInitUrl = "/jsp/sales/ordermanage/orderaudit/resourceQueryInit.jsp";
	//private final String  VehicleDetailUrl = "/jsp/sales/storageManage/vehicleDetail.jsp";
	private final String LOCK_INIT_URL="/jsp/sales/ordermanage/orderaudit/resourceQueryDetail.jsp";
	private final String UN_ENTITY_DETAIL_URL="/jsp/sales/ordermanage/orderaudit/orderResourceQueryUnentity.jsp";
	
	/**
	 * FUNCTION		:	可利用资源查询面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void resourceQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> wareHouseList = dao.getWareHouseList(logonUser.getCompanyId().toString());// 仓库列表
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(poseId.toString());
			act.setOutData("wareHouseList", wareHouseList);
			act.setOutData("areaList", areaList);
			act.setForword(resourceQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	可利用资源查询:结果展示
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void resourceQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String groupCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//物料组
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));					//业务范围
			String resStatus = CommonUtils.checkNull(request.getParamValue("resStatus"));			//资源状态
			String warseId = CommonUtils.checkNull(request.getParamValue("warehouseId"));			//库存组织

            String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));

			
			if("".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao
						.getPoseIdBusiness(poseId.toString());
				for (int i=0; i< areaList.size();i++) {
					areaId += areaList.get(i).get("AREA_ID").toString()+",";
				}
				if (!"".equals(areaId)) {
					areaId = areaId.substring(0, areaId.length()-1);
				}
			}
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = ResourceQueryDAO.getResourceQueryList(warseId,resStatus , areaId , groupCode , oemCompanyId , Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * 锁定资源明细查询
	 */
	public void resourceQueryDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id=act.getRequest().getParamValue("material_id");
			List<Map<String,Object>> list=dao.getLockResourceDetail(material_id);
			act.setOutData("list", list);
			act.setForword(LOCK_INIT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"资源锁定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 锁定资源明细查询
	 */
	public void resourceDownLoad(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			ResponseWrapper response=act.getResponse();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String groupCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//物料组
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));					//业务范围
			String resStatus = CommonUtils.checkNull(request.getParamValue("resStatus"));			//资源状态
			String warseId = CommonUtils.checkNull(request.getParamValue("warehouseId"));			//库存组织
			if("".equals(areaId)) {
				Long poseId = logonUser.getPoseId();
				List<Map<String, Object>> areaList = MaterialGroupManagerDao
						.getPoseIdBusiness(poseId.toString());
				for (int i=0; i< areaList.size();i++) {
					areaId += areaList.get(i).get("AREA_ID").toString()+",";
				}
				if (!"".equals(areaId)) {
					areaId = areaId.substring(0, areaId.length()-1);
				}
			}
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<Map<String, Object>> resList = ResourceQueryDAO.getResourceDownList(warseId,resStatus , areaId , groupCode , oemCompanyId);
			// 导出的文件名
			String fileName = "可利用资源.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("可利用资源查询", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 4, y);
			sheet.addCell(new jxl.write.Label(0, y, "可利用资源",wcf));
			++y;
			
			sheet.addCell(new Label(0, y, "物料代码"));
			sheet.addCell(new Label(1, y, "物料名称"));
			sheet.addCell(new Label(2, y, "颜色"));
			sheet.addCell(new Label(3, y, "库存数量"));
			sheet.addCell(new Label(4, y, "锁定资源数量"));
			sheet.addCell(new Label(5, y, "可用库存（库存数量-锁定资源数量）"));
			int length=resList.size();
			for(int i=0;i<length;i++){
				++y;
				 Map<String,Object> maps=resList.get(i);
				sheet.addCell(new Label(0, y, maps.get("MATERIAL_CODE").toString()));
				sheet.addCell(new Label(1, y, maps.get("MATERIAL_NAME").toString()));
				sheet.addCell(new Label(2, y, maps.get("COLOR_NAME")==null?"":maps.get("COLOR_NAME").toString()));
				sheet.addCell(new Label(3, y, maps.get("RESOURCE_AMOUNT").toString()));
				sheet.addCell(new Label(4, y, maps.get("REQ_AMOUNT").toString()));
				sheet.addCell(new Label(5, y, maps.get("AVA_STOCK").toString()));
				
			}
			workbook.write();
			workbook.close();
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"导出异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 未满足常规订单明细查询
	 */
	public void resourceQueryUnentityDetail(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String material_id=act.getRequest().getParamValue("material_id");
			List<Map<String,Object>> list=dao.getUnFitNormalOrderDetail(material_id);
			act.setOutData("list", list);
			act.setForword(UN_ENTITY_DETAIL_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"资源锁定明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
