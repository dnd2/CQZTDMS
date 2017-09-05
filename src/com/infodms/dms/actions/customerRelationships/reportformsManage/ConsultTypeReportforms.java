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
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClientSearchCondition;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ClientInforManageDao;
import com.infodms.dms.dao.customerRelationships.ConsultTypeReportformsDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 咨询类别统计
 * @author wangming
 *
 */
public class ConsultTypeReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//咨询类别统计初始化页面
	private final String consultTypeReportformsUrl = "/jsp/customerRelationships/reportformsManage/consultTypeReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void consultTypeReportformsInit(){
		try{
			act.setForword(consultTypeReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询类别统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryConsultTypeReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  				
			
			ConsultTypeReportformsDao dao = ConsultTypeReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			int tatal = dao.getTotalConsultTypeReportforms(dealName, dateStart,dateEnd);
			if(tatal  == 0){
				act.setOutData("ps", new PageResult<Map<String,Object>>());
			}else{
				PageResult<Map<String,Object>> consultTypeReportformsData = dao.queryConsultTypeReportforms(dealName,dateStart,dateEnd,tatal,Constant.PAGE_SIZE_MAX,curPage);
				act.setOutData("ps", consultTypeReportformsData);
			}	
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"咨询类别统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	
	public void consultTypeReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  	
			
			ConsultTypeReportformsDao dao = ConsultTypeReportformsDao.getInstance();
			int tatal = dao.getTotalConsultTypeReportforms(dealName, dateStart,dateEnd);
			List<Map<String, Object>> consultTypeReportformsData = dao.queryConsultTypeReportformsList(dealName,dateStart,dateEnd,tatal);
			
			if(tatal == 0){
				act.setForword(consultTypeReportformsUrl);
			}else if(tatal>10000 && tatal>0) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
				act.setForword(consultTypeReportformsUrl);
			}else{
				consultTypeReportformsDataToExcel(consultTypeReportformsData);
				act.setForword(consultTypeReportformsUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void consultTypeReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[4];
		head[0]="类别";
		head[1]="类别内容";
		head[2]="咨询人次";
		head[3]="占咨询总量的百分比（%）";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[4];
					detail[0] = CommonUtils.checkNull(map.get("TYPENAME"));
					detail[1] = CommonUtils.checkNull(map.get("BIZCONTENT"));
					detail[2] = CommonUtils.checkNull(map.get("COUNTDESC"));
					detail[3] = CommonUtils.checkNull(map.get("COUNTRATE"));
					
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "咨询类别统计.xls";
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
