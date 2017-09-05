package com.infodms.dms.actions.customerRelationships.complaintConsult;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.customerRelationships.baseSetting.typeSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 大区投诉查询ACTIONS
 * @ClassName     : ComplaintSearch 
 * @Description   : 投诉查询
 * @author        : wangming
 * CreateDate     : 2013-5-7
 */
public class ODepartmentComplaintSearch {
	private static Logger logger = Logger.getLogger(ODepartmentComplaintSearch.class);
	//投诉查询页面
	private final String ComplaintSearch = "/jsp/customerRelationships/complaintConsult/oDepartmentComplaintSearch.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	//投诉受理初始化
	public void oDepartmentComplaintSearchInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//处理特殊添加工单状态
			List<Map<String, Object>> statusList = commonUtilActions.getTcCode(Constant.COMPLAINT_STATUS);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("CODE_ID", "unClose");
			map.put("CODE_DESC", "未关闭");
			statusList.add(map);
			act.setOutData("stautsList", statusList);
			//报怨类型
			typeSet ts = new typeSet();
			act.setOutData("bclist", ts.getTypeSelect(Constant.TYPE_COMPLAIN));
			act.setForword(ComplaintSearch);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"投诉查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryODepartmentComplaintSearch(){
		act.getResponse().setContentType("application/json");
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level"));  	
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));  				

			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			
			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
			List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
			
			String orgid = "";
			for(Map map : orgDepart){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			for(Map map : org){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfoByOrgids(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgid,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void oDepartmentComplaintSearchDownExcel(){
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String level = CommonUtils.checkNull(request.getParamValue("level")); 
			String dealUser = CommonUtils.checkNull(request.getParamValue("dealUser"));  	
			String accUser = CommonUtils.checkNull(request.getParamValue("accUser"));  	
			String biztype = CommonUtils.checkNull(request.getParamValue("biztype"));  				
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));
			String region = CommonUtils.checkNull(request.getParamValue("region"));  	
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  				
			String checkStart = CommonUtils.checkNull(request.getParamValue("checkStart"));  	
			String checklEnd = CommonUtils.checkNull(request.getParamValue("checklEnd"));				

			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			
			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForDepart(logonUser.getUserId());	
			List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
			
			String orgid = "";
			for(Map map : orgDepart){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			for(Map map : org){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
				
			List<Map<String, Object>> complaintAcceptData =  dao.queryComplaintInfoByOrgids(vin,name,tele,level,dealUser,accUser,biztype,
					status,dealStart,dealEnd,dateStart,dateEnd,region,pro,checkStart,checklEnd,orgid);
				complaintAcceptDataToExcel(complaintAcceptData);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void complaintAcceptDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[36];
		head[0]="抱怨单编号";
		head[1]="抱怨级别";
		head[2]="抱怨类别";
		head[3]="省市";
		head[4]="地市";
		head[5]="客户姓名";
		head[6]="联系电话";
		head[7]="抱怨内容";
		head[8]="抱怨对象";
		head[9]="抱怨时间";
		head[10]="受理人";
		head[11]="车种";
		head[12]="VIN号";
		head[13]="购车时间";
		head[14]="行驶里程";
		head[15]="里程范围";
		head[16]="处理部门";
		head[17]="处理人";
		head[18]="签收日期";
		head[19]="转出时间";
		head[20]="回访结果";
		head[21]="回访人";
		head[22]="回访日期";
		head[23]="关闭时间";
		head[24]="处理时长(小时)";
		head[25]="规定处理期限(天)";
		head[26]="延期天数(天)";
		head[27]="规定及时关闭时间";
		head[28]="是否及时关闭";
		head[29]="是否一次处理满意";
		head[30]="是否正常关闭";
		head[31]="处理过程";
		head[32]="最终反馈日期";
		head[33]="处理结果";
		head[34]="状态";
		head[35]="当前处理人";
		
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		
		List<Map<String, Object>> cpns = tcCodeDao.getTcCodesByType(Constant.COMPLAINT_STATUS.toString());
		List<Map<String, Object>> cpis = tcCodeDao.getTcCodesByType(Constant.GRADE_TYPE);
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[36];
					detail[0] = CommonUtils.checkNull(map.get("CPNO"));
					detail[1] = CommonUtils.checkNull(map.get("CPLEVEL"));
					detail[2] = CommonUtils.checkNull(map.get("BIZCONT"));
					detail[3] = CommonUtils.checkNull(map.get("PRO"));
					detail[4] = CommonUtils.checkNull(map.get("CITY"));
					detail[5] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[6] = CommonUtils.checkNull(map.get("PHONE"));
					detail[7] = CommonUtils.checkNull(map.get("CPCONT"));
					detail[8] = CommonUtils.checkNull(map.get("CPOBJECT"));
					detail[9] = CommonUtils.checkNull(map.get("CREATEDATE"));
					detail[10] = CommonUtils.checkNull(map.get("ACUSER"));
					detail[11] = CommonUtils.checkNull(map.get("SNAME"));
					detail[12] = CommonUtils.checkNull(map.get("VIN"));
					detail[13] = CommonUtils.checkNull(map.get("BDATE"));
					detail[14] = CommonUtils.checkNull(map.get("CPMILEAGE"));
					detail[15] = CommonUtils.checkNull(map.get("MILEAGERANGE"));
					detail[16] = CommonUtils.checkNull(map.get("ORGNAME"));
					detail[17] = CommonUtils.checkNull(map.get("DEALUSER"));
					detail[18] = CommonUtils.checkNull(map.get("CPACCDATE"));
					detail[19] = CommonUtils.checkNull(map.get("TURNDATE"));
					detail[20] = CommonUtils.checkNull(map.get("CRCONET"));
					detail[21] = CommonUtils.checkNull(map.get("CUSER"));
					detail[22] = CommonUtils.checkNull(map.get("CDATE"));
					detail[23] = CommonUtils.checkNull(map.get("CPCDATE"));
					detail[24] = CommonUtils.checkNull(map.get("DEALTIME"));
					detail[25] = CommonUtils.checkNull(map.get("CPLIMIT"));
					detail[26] = CommonUtils.checkNull(map.get("DELAYDATE"));
					detail[27] = CommonUtils.checkNull(map.get("SHOULDCLOSETIME"));
					detail[28] = CommonUtils.checkNull(map.get("ISTIMELYCLOSE"));
					detail[29] = CommonUtils.checkNull(map.get("CPISONCESF"));
					detail[30] = CommonUtils.checkNull(map.get("ISNORMALCLOSE"));
					detail[31] = CommonUtils.checkNull(map.get("DEALCONTENT"));
					detail[32] = CommonUtils.checkNull(map.get("LASTDEALDATE"));
					detail[33] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CPSF")),cpis);
					detail[34] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("STATUS")),cpns);
					detail[35] = CommonUtils.checkNull(map.get("DUSER"));
					
					list1.add(detail);
		    	}
		    }
	    }
	    this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "抱怨查询清单.xls";
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
			if(list != null && list.size() > 0)
			{
				int pageSize=list.size()/30000;
				for (int z = 1; z < list.size() + 1; z++) {
					String[] str = list.get(z - 1);
					for (int i = 0; i < str.length; i++) {
						ws.addCell(new Label(i, z, str[i]));
					}
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
