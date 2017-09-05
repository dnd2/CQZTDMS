package com.infodms.yxdms.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import com.infodms.dms.actions.claim.basicData.BaseImport;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.exception.BizException;
import com.infodms.yxdms.service.CommonService;
import com.infodms.yxdms.service.MainTainService;
import com.infodms.yxdms.service.impl.CommonServiceImpl;
import com.infodms.yxdms.service.impl.MainTainServiceImpl;
import com.infodms.yxdms.utils.BaseAction;
import com.infoservice.po3.bean.PageResult;
/**
 * 基础数据维护action
 * @author yuewei
 *
 */
public class MainTainAction extends BaseAction{
	private  CommonService  commonservice  = new CommonServiceImpl();
	private MainTainService maintainservice = new MainTainServiceImpl();
	CommonUtilActions commonUtilActions = new CommonUtilActions();//查询车型组
	PageResult<Map<String, Object>> list=null;
	
	public void delYsqPart(){
	   String part_Type =	request.getParamValue("type");
	   if (part_Type.equals(Constant.YSQ_PART_TYPE_01.toString())) {//易损件
		   int res=maintainservice.delYsqPart(getParam("id"));
		   setJsonSuccByres(res);
	    }else if (part_Type.equals(Constant.YSQ_PART_TYPE_02.toString())) {//存留件
	    	int res=maintainservice.delYsqPart1(request,loginUser);
			   setJsonSuccByres(res);
		}
	}
	public void downPartLabExecl(){
		maintainservice.downExecl(response,request,loginUser);
	}
	
	public void addMailListSure(){
		int res=maintainservice.addMailListSure(request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void addMailList(){
		List listpo=  commonservice.findSelectList(null, null, null, "select v.root_org_name from vw_org_dealer_service v group by v.root_org_name order by v.root_org_name desc");
		act.setOutData("listpo", listpo);
		sendMsgByUrl("maintain", "add_mail_list", "通讯录人员管理新增页面");
	}
	
	//通讯录人员管理
	public void mailList(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = maintainservice.mailList(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "mail_list", "通讯录人员管理查询页面");
	}
	
	//通讯录人员管理
	public void mailListTemp(){
		String query = getParam("query");
		if ("true".equals(query)){
			list = maintainservice.mailList(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "mail_list_temp", "通讯录人员管理查询页面");
	}
	
	public void addEmergency(){
		request.setAttribute("type", "add");
		if("1".equals(getParam("type"))){
			int res=maintainservice.addEmergency(request,loginUser);
			setJsonSuccByres(res);
		}else{
			sendMsgByUrl("maintain", "add_emergency", "紧急调件人员新增维护页面");
		}
	}
	
	public void viewEmergency(){
		Map<String,Object> map =maintainservice.viewEmergency(request,getParam("id"));
		act.setOutData("po", map);
		request.setAttribute("type", "view");
		sendMsgByUrl("maintain", "add_emergency", "紧急调件人员新增维护页面");
	}
	
	public void emergencyMainTainList(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = maintainservice.emergencyMainTain(request, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "emergency_main_tain_show", "紧急调件人员维护查询页面");
	}
	/**
	 * 紧急调件人员维护
	 */
	public void emergencyMainTain(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = maintainservice.emergencyMainTain(request, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "emergency_main_tain", "紧急调件人员维护查询页面");
	}
	
	public void insertYsqPart(){
		int res=maintainservice.insertYsqPart(request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void addYsqPart(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = maintainservice.addYsqPartData(request, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "add_ysq_part", "添加预授权易损件的维护查询页面");
	}
	//预授权易损件的维护
	public void ysqPartInit(){
		sendMsgByUrl("maintain", "ysq_part_list", "预授权易损件的维护查询页面");
	}
	public void ysqPartData(){
		list=maintainservice.ysqPartData(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	public void insertRalation(){
		request.setAttribute("part_id", getParam("part_id"));
		request.setAttribute("part_code", getParam("part_code"));
		int res= maintainservice.insertRalation(request,loginUser);
		setJsonSuccByres(res);
	}
	
	public void statusDel(){
		int res=maintainservice.statusDel(request);
		setJsonSuccByres(res);
	}
	
	public void findaddLabour(){
		list=maintainservice.findaddLabour(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	
	public void goToRelationJsp(){
		request.setAttribute("part_id", getParam("part_id"));
		request.setAttribute("part_code", getParam("part_code"));
		sendMsgByUrl("maintain", "add_relation_lab", "工时配件维护查询页面");
	}
	
	public void goToLabRalation(){
		request.setAttribute("part_id", getParam("part_id"));
		request.setAttribute("part_code", getParam("part_code"));
		sendMsgByUrl("maintain", "lab_relation", "工 时配件维护查询页面");
	}
	
	public void LabRalation(){
		list=maintainservice.findLabPartRelation(request,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", list);
	}
	
	public void labPart(){
		String query = getParam("query");
		if ("true".equals(query)) {
			list = maintainservice.addPart(request, Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", list);
		}else{
			sendMsgByUrl("maintain", "lab_part_relation", "工时配件维护查询页面");
		}
	}
	
	public void checkIsFirst(){
		Map<String,Object> map = maintainservice.checkIsFirst(request);
		act.setOutData("map", map);
	}
	
	public void keepFitTemplate(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=maintainservice.keepFitTemplateData(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		List listpo=  commonservice.findSelectList(null, null, null, "select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name");
		act.setOutData("listpo", listpo);
		sendMsgByUrl("maintain", "keepFit_list", "免费保养模板维护查询页面");
	}
	public void view(){
		try {
			Map<String,Object> po=maintainservice.findKeepFit(request);
			act.setOutData("t", po);
			List<Map<String,Object>> labours2= maintainservice.findLaboursById(request);
			List<Map<String,Object>> parts2= maintainservice.findPartsById(request);
			if(checkListNull(labours2)){
				act.setOutData("labours2", labours2);
			}
			if(checkListNull(parts2)){
				act.setOutData("parts2", parts2);
			}
			request.setAttribute("type", "view");
			sendMsgByUrl("maintain", "add_mainTain", "免费保养模板维护查询页面");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void publish(){
		int res=maintainservice.publish(request);
		setJsonSuccByres(res);
	}
	public void updateInit(){
		try {
			Map<String,Object> po=maintainservice.findKeepFit(request);
			act.setOutData("t", po);
			List<Map<String,Object>> labours2= maintainservice.findLaboursById(request);
			List<Map<String,Object>> parts2= maintainservice.findPartsById(request);
			if(checkListNull(labours2)){
				act.setOutData("labours2", labours2);
			}
			if(checkListNull(parts2)){
				act.setOutData("parts2", parts2);
			}
			request.setAttribute("type", "update");
			sendMsgByUrl("maintain", "add_mainTain", "免费保养模板维护查询页面");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteMainTain(){
		int res=maintainservice.deleteMainTain(request);
		setJsonSuccByres(res);
	}
	public void addMainTain(){
		request.setAttribute("type", "add");
		List listpo=  commonservice.findSelectList(null, null, null, "select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name");
		act.setOutData("listpo", listpo);
		sendMsgByUrl("maintain", "add_mainTain", "免费保养模板维护查询页面");
	}
	public void addMainTainCommit() throws BizException{
		int res = maintainservice.addMainTainCommit(request,loginUser);
		if(res==-1){
			throw new BizException(null);
		}
		List listpo=  commonservice.findSelectList(null, null, null, "select g.package_code,g.package_name from vw_material_group g group by g.package_code,g.package_name");
		act.setOutData("listpo", listpo);
		sendMsgByUrl("maintain", "keepFit_list", "免费保养模板维护查询页面");
	}
	public void addLabour(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=maintainservice.addLabour(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "add_labour", "工时列表查询页面");
	}
	public void addPart(){
		String flag = getParam("query");
		if("true".equals(flag)){
			list=maintainservice.addPart(request,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		}
		sendMsgByUrl("maintain", "add_part", "配件列表查询页面");
	}
	public void addPartSpecial(){
		sendMsgByUrl("activity", "showMainPartCode", "配件列表查询页面");
	}
	public void queryPartCode(){
		PageResult<Map<String,Object>>  ps = maintainservice.queryPartCode(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("ps", ps);
		
	}
	public void getKeepFitData(){
		List<Map<String,Object>> labours2= maintainservice.findLaboursById(request);
		List<Map<String,Object>> parts2= maintainservice.findPartsById(request);
		act.setOutData("keep_fit_no", getParam("keep_fit_no"));
		if(checkListNull(labours2)){
			act.setOutData("labours2", labours2);
		}
		if(checkListNull(parts2)){
			act.setOutData("parts2", parts2);
		}
	}
	
	//=====================2015.4.22 lj
	public void importLabPartRelation(){
		sendMsgByUrl("maintain", "importLabPart", "配件工时维护关联维护导入页面");
	}
	//导入工时维护关联关系
	public void uploadLabPart(){
		try {
			List<Map<String,String>> errorInfo= new ArrayList<Map<String,String>>();
			long maxSize=1024*1024*5;
			BaseImport b=new BaseImport();
			int errNum=b.insertIntoTmp(request, "uploadFile",7,7,maxSize);
			String err="";
			
			if(errNum!=0){
				switch (errNum) {
				case 1:
					err+="文件列数过多!";
					break;
				case 2:
					err+="空行不能大于三行!";
					break;
				case 3:
					err+="文件不能为空!";
					break;
				case 4:
					err+="文件不能为空!";
					break;
				case 5:
					err+="文件不能大于!";
					break;
				default:
					break;
				}
			}
			if(!"".equals(err)){
				act.setOutData("error", err);
				sendMsgByUrl("maintain", "inputerror", "导入报错页面");
			}else{
				List<Map> list=b.getMapList();
				List<Map<String,String>> voList = new ArrayList<Map<String,String>>();
				loadVoList(voList,list, errorInfo);//检测是否存在
				if(errorInfo.size()>0){
					act.setOutData("errorInfo", errorInfo);
					sendMsgByUrl("maintain", "inputerror", "导入报错页面");
				}else{
					act.setOutData("list", voList);
					sendMsgByUrl("maintain", "importSureLabandPart", "导入工时关联维护确认页面");
				}
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = null;
			if(e instanceof BizException){
				e1 = (BizException)e;
			}else{
				new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(loginUser,e1);
			act.setException(e1);
		}
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadVoList(List<Map<String,String>> voList,List<Map> list,List<Map<String,String>> errorInfo) throws Exception{
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
					Map<String, String> tempmap = new HashMap<String, String>();
					if ("".equals(cells[0].getContents().trim())) {//配件代码
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "配件代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
//					} else if (cells[0].getContents().trim().length() !=16) {
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "配件代码");
//						errormap.put("3", "配件代码必须16位!");
//						errorInfo.add(errormap);
					 }else{
						List listTtasrolabour = maintainservice.findTtasrolabour(cells[0].getContents().trim());
						if (listTtasrolabour.isEmpty()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "配件代码");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} 
					}
					tempmap.put("1", cells[0].getContents().trim());
					if ("".equals(cells[1].getContents().trim())) {//工时代码
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "工时代码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
//					} else if (cells[1].getContents().trim().length() !=11) {
//						Map<String, String> errormap = new HashMap<String, String>();
//						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
//						errormap.put("2", "工时代码");
//						errormap.put("3", "工时代码必须11位!");
//						errorInfo.add(errormap);
					 }else{
						List listTtaswrwrlabinfo =	maintainservice.findTtaswrwrlabinfo(cells[1].getContents().trim());
						if (listTtaswrwrlabinfo.isEmpty()) {
							Map<String, String> errormap = new HashMap<String, String>();
							errormap.put("1", "第" + (i + 1) + "页,第" + key+ "行");
							errormap.put("2", "该车型组配对的工时代码");
							errormap.put("3", "不存在");
							errorInfo.add(errormap);
						} 
					}
					tempmap.put("2", cells[1].getContents().trim());
					voList.add(tempmap);
			}	
		}
	}
	//插入数据
	public void importLabAndpartAdd(){
		maintainservice.insertlabpartRelation(request,loginUser);
		setJsonSuccByres(1);
	}
	
	//插入数据
	public void insertoldoutpart(){
		int res =maintainservice.insertoldoutpart(request,loginUser);
		setJsonSuccByres(1);
	}
	
	
	public void WinterMaintenance(){
		String type = getParam("type");
		act.setOutData("modelList", commonUtilActions.getAllModel());
		act.setOutData("statusList", commonUtilActions.getTcCode("1068"));
		if ("query".equals(type)) {
			PageResult<Map<String, Object>> ps =maintainservice.queryWinterMaintenancelist(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
		    act.setOutData("ps", ps);
		}
		sendMsgByUrl("maintain", "winter_Mainten_list", "冬季保养维护列表");
	}
	
 	@SuppressWarnings("unchecked")
	public void winterMaintenAdd(){
 		act.setOutData("modelList", commonUtilActions.getAllModel());//车型
 		String type = getParam("type");
 		if ("update".equals(type)) {//修改跳转设置
 			PageResult<Map<String, Object>> ps = maintainservice.queryWinterDetail(request,loginUser);
 			List<Map<String, Object>> ps1 = maintainservice.queryWinterById(request,loginUser);
 			List<Map<String, Object>> list = ps.getRecords();
 			List<Map<String, Object>> list1 = maintainservice.queryWinterById(list.get(0).get("ID").toString(),loginUser);//配置
 			act.setOutData("type", type);
 			act.setOutData("winterDetail", ps.getRecords());
 			act.setOutData("ps", ps);
 			if (DaoFactory.checkListNull(ps1)) {
 				String[] dealerId = new String[ps1.size()];
 				String dealerCode = "";
 				if (null!=dealerId) {
 				  for (int i=0;i<ps1.size();i++) {
 					dealerId[i] = ps1.get(i).get("DEALER_ID").toString();
 					dealerCode+=  ","+ps1.get(i).get("DEALER_CODE").toString();
 				  }
 				   act.setOutData("dealerId", dealerId);
 				   act.setOutData("dealerCode", dealerCode);
 				}
 				}
 			String str = "";
  			String packagecode = "";
 			if (DaoFactory.checkListNull(list1)) {
 				System.out.println(list1.get(0).get("PACKAGE_NAME"));
 				for (int j = 0; j < list1.size(); j++) {
 					if (str == "") {
 						str = list1.get(j).get("PACKAGE_NAME").toString();
 					} else {
 						str = str + "," + list1.get(j).get("PACKAGE_NAME").toString();
 					}
 					if (packagecode == "") {
 						packagecode = list1.get(j).get("PACKAGE_CODE").toString();
 					} else {
 						packagecode = packagecode + "," + list1.get(j).get("PACKAGE_CODE").toString();
 					}
				}
 				   act.setOutData("packagename", str);
 				   act.setOutData("packagecode", packagecode);
			}
		}else {
			act.setOutData("winterDetail", new ArrayList());
		}
 		act.setOutData("mod", "false");
		sendMsgByUrl("maintain", "winter_Mainten_Add", "冬季保养维护新增页面");
	}
	public void winterMaintenDealerQuery() {
			PageResult<Map<String, Object>> ps = maintainservice.queryDealer(request,loginUser,Constant.PAGE_SIZE, getCurrPage());
			if (ps.getRecords()!=null) {
			String dealerCode = "";
			String[] dealerId = new String[ps.getRecords().size()];
			for (int i=0;i<ps.getRecords().size();i++) {
				dealerId[i] = ps.getRecords().get(i).get("DEALER_ID").toString();
				dealerCode= dealerCode +","+ps.getRecords().get(i).get("DEALER_CODE").toString();
			}
			act.setOutData("dealerId", dealerId);
			act.setOutData("dealerCode", dealerCode);
			}
			act.setOutData("ps", ps);
			
		}
	public void winterMaintenDealerAdd() {
	  String res =	maintainservice.insertWinterMaintenDealer(request, loginUser);
	  act.setOutData("result",res);
	  
	}
	
	public void winterMaintenUpdate() {
		String res =	maintainservice.UpdateWinterMainten(request, loginUser);
	  act.setOutData("result",res);
	}
	public void winterMaintenView() {
		List<Map<String, Object>> list =	maintainservice.winterMaintenView(request, loginUser);
	    act.setOutData("winterDetail",list);
	    sendMsgByUrl("maintain", "winter_Mainten_Detail", "冬季保养维护明细页面");
		}
	public void choosepageCode() {
		PageResult<Map<String, Object>>  list =	maintainservice.querychoosepageCode(request, loginUser,Constant.PAGE_SIZE,getCurrPage());
	    act.setOutData("ps",list);
		}
	
	public void queryConfiguration() {
		PageResult<Map<String, Object>>  list =	maintainservice.queryConfiguration(request, loginUser,Constant.PAGE_SIZE,getCurrPage());
	    act.setOutData("ps",list);
	    act.setOutData("model_code", getParam("model_code"));
	    sendMsgByUrl("maintain", "winter_Main_chooseConfiguration", "冬季保养维护配置选择");
		}

	public void queryConfigurationBycode() {
		PageResult<Map<String, Object>>  list =	maintainservice.queryConfigurationBycode(request, loginUser,Constant.PAGE_SIZE,getCurrPage());
	    List<Map<String, Object>> list1=list.getRecords();
	    String group_name="";
		if (DaoFactory.checkListNull(list1)) {
			for (int i = 0; i < list1.size(); i++) {
				group_name+=",";
				group_name=group_name+list1.get(i).get("PACKAGE_NAME").toString();
			}
		}
		act.setOutData("package_name",group_name);
		}
	public void queryConfigurationById(){
		PageResult<Map<String, Object>>  list =	maintainservice.queryConfigurationById(request, loginUser,Constant.PAGE_SIZE,getCurrPage());
		act.setOutData("list",list.getRecords());
		 sendMsgByUrl("maintain", "winter_Main_ConfigurationView", "冬季保养配置明细");
	}
	
	
}
