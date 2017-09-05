/**********************************************************************
* <pre>
* FILE : OemVhclDetailQuery.java
* CLASS : OemVhclDetailQuery
* AUTHOR : 
* FUNCTION : 详细车籍查询
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-07|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.oemstorage;

import java.io.OutputStream;
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
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-7
 * @author 
 * @mail   
 * @version 1.0
 * @remark 详细车籍查询
 */
public class OemVhclDetailQuery {
	
	public Logger logger = Logger.getLogger(OemVhclDetailQuery.class);   
	OemStorageManageDao dao  = OemStorageManageDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	ResponseWrapper response = act.getResponse();
	RequestWrapper request = act.getRequest();
	private final String initUrl = "/jsp/sales/oemstorage/oemVhclDetailQuery.jsp";
	/**
	 * 详细车籍查询页面初始化
	 */
	public void vhclDetailQueryInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"详细车籍查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 详细车籍查询
	 */
	public void vhclDetailQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = request.getParamValue("dealerCode");			//经销商CODE
			String groupCode =request.getParamValue("groupCode");				//物料组CODE
			String vin = request.getParamValue("vin");							//VIN
			
			//add by WHX,2012.09.25
			//发动机号
			//=========================================================Start
			String enginNo = CommonUtils.checkNull(request.getParamValue("enginNo")) ;
			//=========================================================End
			
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getVhclDetail(enginNo, areaIds.toString(),areaId, dealerCode, groupCode, vin, companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"详细车籍查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 详细车籍模板下载
	 */
	public void vhclLoadDetailQuery(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);

		// 导出的文件名
		String fileName = "详细车籍下载.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			String dealerCode = request.getParamValue("dealerCode");			//经销商CODE
			String groupCode =request.getParamValue("groupCode");				//物料组CODE
			String vin = request.getParamValue("vin");							//VIN
			
			//add by WHX,2012.09.25
			//发动机号
			//=========================================================Start
			String enginNo = CommonUtils.checkNull(request.getParamValue("enginNo")) ;
			//=========================================================End
			
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			StringBuffer areaIds = new StringBuffer();
			List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			for (int i = 0; i < areaBusList.size(); i++) {
				if (!"".equals(String.valueOf(areaBusList.get(i).get("AREA_ID")))) {
					areaIds.append(String.valueOf(areaBusList.get(i).get("AREA_ID")));
				}
				if (i<areaBusList.size()-1) {
					areaIds.append(",");
				}
			}
			//Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("颜色");
			listTemp.add("批次号");
			listTemp.add("车辆VIN号");
			listTemp.add("车辆价格");
			listTemp.add("当前结点");
			listTemp.add("库存状态");
			listTemp.add("车辆状态");
			list.add(listTemp);
			List<Map<String, Object>> results  = dao.getLoadVhclDetail(enginNo, areaIds.toString(),areaId, dealerCode, groupCode, vin, companyId,999999, Constant.PAGE_SIZE);
			
			if(results!=null && !results.equals("")){
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("COLOR_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("BATCH_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("VHCL_PRICE")));
				listTemp.add(CommonUtils.checkNull(record.get("NODE_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("LIFE_CYCLE")));
				listTemp.add(CommonUtils.checkNull(record.get("LOCK_STATUS")));
				list.add(listTemp);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);		
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"详细车籍查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
