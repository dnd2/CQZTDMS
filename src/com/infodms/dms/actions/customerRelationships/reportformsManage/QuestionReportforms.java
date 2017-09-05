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
import com.infodms.dms.dao.customerRelationships.QuestionReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 问卷模版统计
 * @author wangming
 *
 */
public class QuestionReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//问卷模版统计初始化页面
	private final String questionReportformsUrl = "/jsp/customerRelationships/reportformsManage/questionReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void QuestionReportformsInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//抱怨对象
			//act.setOutData("cpObjectList", commonUtilActions.getTmOrgPOAndTop());
			//小区及省分
			//act.setOutData("tmOrgSmallList", commonUtilActions.getSmallTmOrgPO());
			//报表类型
			//act.setOutData("reportTypeList", getReportType());
			QuestionReportformsDao dao = QuestionReportformsDao.getInstance();
			//问卷
			act.setOutData("questionList", dao.getQuestionList());
			act.setForword(questionReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"问卷模版统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryQuestionReportforms(){
		try{
			String qrId = CommonUtils.checkNull(request.getParamValue("qrId"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			
			
			QuestionReportformsDao dao = QuestionReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> questionReportformsData = dao.queryQuestionReportforms(qrId,dateStart,dateEnd,Constant.PAGE_SIZE_MAX,curPage);
			if(questionReportformsData!=null){
				List<Map<String, Object>> list = questionReportformsData.getRecords();
				if( list != null && list.size()>0){
					list.get(list.size()-1).put("QUESTION", "合计");
					act.setOutData("ps", questionReportformsData);
				}
			}
			act.setOutData("ps", questionReportformsData);
				
				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"问卷模版统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	public void questionReportformsExcel(){
		try{
			String qrId = CommonUtils.checkNull(request.getParamValue("qrId"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));   
			
			QuestionReportformsDao dao = QuestionReportformsDao.getInstance();
			List<Map<String,Object>> questionReportformsData = dao.queryQuestionReportformsList(qrId,dateStart,dateEnd);
			
			
			if(questionReportformsData!=null && questionReportformsData.size()>0){
				questionReportformsData.get(questionReportformsData.size()-1).put("QUESTION", "合计");
				questionReportformsDataToExcel(questionReportformsData);
				if(questionReportformsData.size()>10000) {act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");}
			}
			act.setForword(questionReportformsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void questionReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[3];
		head[0]= "问题";
		head[1]="答案";
		head[2]="数量";

		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[3];
					detail[0] = CommonUtils.checkNull(map.get("QUESTION"));
					detail[1] = CommonUtils.checkNull(map.get("ANSWER"));
					detail[2] = CommonUtils.checkNull(map.get("TOTAL"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "问卷模版统计.xls";
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
