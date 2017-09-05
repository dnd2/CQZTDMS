package com.infodms.dms.actions.sales.tsimng;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.tsilogmng.TsiLogDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class TsiLogManager {
	public Logger logger = Logger.getLogger(TsiLogManager.class);
	ActionContext act = ActionContext.getContext();
	private static final String queryTiLogUrl = "/jsp/sales/tsilogmng/tsiTableContent.jsp";;
	private static final String querySet  =  "/jsp/sales/tsilogmng/tsiQuerySet.jsp";
	
	public void initTiTableContent(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryTiLogUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "接口表查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void queryTiTable(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySet);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"选择接口表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	public void querySetQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TsiLogDao dao = TsiLogDao.getTiLogDao();
		try {
			String tableName = act.getRequest().getParamValue("tableName");
			String tabName = act.getRequest().getParamValue("tabName");
			RequestWrapper request = act.getRequest();
			int pageSize = 50 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getTableList(tableName, tabName, pageSize,  curPage);
				act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"日志查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	public void queryTableColumnInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TsiLogDao dao = TsiLogDao.getTiLogDao();
		try {
			String tableName = act.getRequest().getParamValue("tableName");
			RequestWrapper request = act.getRequest();
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				List<Map<String, Object>> list = dao.getTableInfo(tableName);
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"日志表信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryTableContent(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TsiLogDao dao = TsiLogDao.getTiLogDao();
		RequestWrapper request = act.getRequest();
		try {
			String tableName = request.getParamValue("tableName");
			String dateStart = request.getParamValue("dateStart");
			String endDate =  request.getParamValue("endDate");
			int pageSize = 50 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.queryTableContent(tableName,dateStart,endDate, pageSize,  curPage);
				act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"日志查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void exportTableColumnInfo(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TsiLogDao dao = TsiLogDao.getTiLogDao();
		RequestWrapper request = act.getRequest();
		try {
			String tableName = request.getParamValue("tableName");
			String dateStart = request.getParamValue("dateStart");
			String endDate =  request.getParamValue("endDate");
			List<Map<String, Object>> list = dao.exportTableContent(tableName,dateStart,endDate);
			List<Map<String, Object>> columnList = dao.getTableInfo(tableName);
			
			String[] head = new String[columnList.size()];
			String[] columns = new String[columnList.size()];
			for(int i=0;i<columnList.size();i++ ){
				Map<String,Object>  map = columnList.get(i);
				head[i] = CommonUtils.checkNull(map.get("COMMENTS"));
				columns[i]=  CommonUtils.checkNull(map.get("COLUMN_NAME"));
			}
			try {
				ToExcel.toReportExcel(ActionContext.getContext().getResponse(), request,"日志查询.xls", head,columns,list);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"日志导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
}
