package com.infodms.dms.actions.customerRelationships.reportformsManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.YawpAnswerReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 不满意问题统计
 * @author wangming
 *
 */
public class YawpAnswerReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//不满意问题统计初始化页面
	private final String yawpAnswerReportformsUrl = "/jsp/customerRelationships/reportformsManage/yawpAnswerReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void YawpAnswerReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			//act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//小区及省分
			//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//报表类型
			//act.setOutData("reportTypeList", getReportType());
			//车型
			act.setOutData("allModelList", commonUtilActions.getAllModel());
			act.setForword(yawpAnswerReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"回访结果统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryYawpAnswerReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String modeId = CommonUtils.checkNull(request.getParamValue("modeId"));  
			
			
			YawpAnswerReportformsDao dao = YawpAnswerReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> yawpAnswerReportformsData = dao.queryYawpAnswerReportforms(dealName,dateStart,dateEnd,modeId,Constant.PAGE_SIZE_MAX,curPage);
			act.setOutData("ps", yawpAnswerReportformsData);
				
				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"不满意问题统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void yawpAnswerReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			String modeId = CommonUtils.checkNull(request.getParamValue("modeId"));  
			
			YawpAnswerReportformsDao dao = YawpAnswerReportformsDao.getInstance();
			List<Map<String,Object>> yawpAnswerReportformsData = dao.queryYawpAnswerReportformsList(dealName,dateStart,dateEnd,modeId);
			
			
			if(yawpAnswerReportformsData!=null&&yawpAnswerReportformsData.size()>0){
				yawpAnswerReportformsDataToExcel(yawpAnswerReportformsData);
				if(yawpAnswerReportformsData.size()>10000) {act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");}
			}
			act.setForword(yawpAnswerReportformsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void yawpAnswerReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[2];
		head[0]= "较集中的问题";
		head[1]="人次";

		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[2];
					detail[0] = CommonUtils.checkNull(map.get("ANSWER"));
					detail[1] = CommonUtils.checkNull(map.get("TOTAL"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "不满意问题统计.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

	
}
