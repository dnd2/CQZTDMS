<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infoservice.mvc.context.ActionContext" %>
<%@ page import="com.infodms.dms.bean.AclUserBean" %>
<%@ page import="com.infodms.dms.bean.CruiServiceBasicHeaderInfoBean" %>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>特殊外出费用申报</title>
<% 
   String contextPath = request.getContextPath();
   ActionContext act = ActionContext.getContext();
   AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
   String logonName = logonUser.getName();
   CruiServiceBasicHeaderInfoBean beanInfo=(CruiServiceBasicHeaderInfoBean)request.getAttribute("beanInfo");
%>
</head>
<script type="text/javascript">
function window.onload(){
	var retCode='<%=request.getAttribute("retCode")%>';
	if(retCode=="data_error_001"){
       MyAlert("无法获取巡航数据");
       
	}
}
</script>
<body onload="doInit();">
 <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;特殊外出费用申报</div>
 <form method="post" name ="fm" id="fm">
  <input type="hidden" id="fjids" name="fjids"/>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航单据编码：</td>
       <td><%=CommonUtils.checkNull(beanInfo.getCr_no())%></td>
       <td align="right">巡航制单人：</td>
       <td><%=CommonUtils.checkNull(beanInfo.getCreate_by())%></td>
       <td align="right">巡航制单日期：</td>
       <td><%=CommonUtils.checkNull(beanInfo.getMake_date())%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航经销商名称：</td>
       <td><%=CommonUtils.checkNull(beanInfo.getDealer_name())%></td>
       <td align="right">生产工厂：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("produce_name"))%></td>
       <td align="right">费用渠道：</td>
       <td><%=CommonUtils.checkNull(request.getAttribute("code_name"))%></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">巡航目的地：</td>
       <td><%=CommonUtils.checkNull(beanInfo.getCr_whither())%></td>
       <td align="right"></td>
       <td></td>
       <td align="right"></td>
       <td></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出开始日期：</td>
       <td>
          <input name="out_start_date" id="out_start_date" value="" type="text" class="short_txt" datatype="0,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_start_date', false);" blurback="true">
       </td>
       <td align="right">外出结束日期：</td>
       <td>
          <input name="out_end_date" id="out_end_date" value="" type="text" class="short_txt" datatype="0,is_date,10" group="out_start_date,out_end_date" hasbtn="true" callFunction="showcalendar(event, 'out_end_date', false);" blurback="true">
       </td>
       <td align="right">外出天数：</td>
       <td id="cal_out_days"></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">外出人数：</td>
       <td>
          <input type="text" name="out_person_num" id="out_person_num" value="" datatype="0,isDigit,5" class="short_txt"/>
       </td>
       <td align="right">出差人员：</td>
       <td>
          <textarea id="out_name" name="out_name" rows="2" cols="20" datatype="0,is_textarea,60"></textarea>
       </td>
       <td align="right">总里程：</td>
       <td><input type="text" name="single_mileage" id="single_mileage" value="" datatype="0,isMoney,10" class="short_txt"/></td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">过路过桥费：</td>
       <td>
          <input type="text" name="road_bridge_fee" id="road_bridge_fee" value="" datatype="0,isMoney,10" blurback="true" class="short_txt" />
       </td>
       <td align="right">车辆或交通补助：</td>
       <td>
          <input type="text" name="vehicle_extra_fee" id="vehicle_extra_fee" value="" datatype="0,isMoney,10" blurback="true" class="short_txt"/>
       </td>
       <td align="right">住宿费：</td>
       <td>
          <input type="text" name="lodging_fee" id="lodging_fee" value="" datatype="0,isMoney,10" blurback="true" class="short_txt"/>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">餐补费：</td>
       <td>
          <input type="text" name="eat_extra_fee" id="eat_extra_fee" value="" datatype="0,isMoney,10" blurback="true" class="short_txt"/>
       </td>
       <td align="right">人员补助：</td>
       <td>
          <input type="text" name="person_extra_fee" id="person_extra_fee" value="" datatype="0,isMoney,10" blurback="true" class="short_txt"/>
 
       </td>
       <td align="right">总费用：</td>
       <td>
          <input type="text" name="total_fee" id="total_fee" value="" class="short_txt" readonly="readonly"/>
       </td>
     </tr>
     <tr bgcolor="F3F4F8">
       <td align="right">申请内容：</td>
       <td colspan="3">
          <textarea id="apply_content" name="apply_content" rows="4" cols="50" datatype="0,is_textarea,600"></textarea>
       </td>
     </tr>
  </table>
  <!-- 添加附件 -->
  <table class="table_info" border="0" id="file">
    <tr>
        <th>
		<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
		&nbsp;附件列表：
		</th>
		<th><span><input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
		</th>
	</tr>
	<tr>
		<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
	</tr>
  </table>
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
     <tr>
       <th colspan="12"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆信息
         <input type="button" class="normal_btn" onclick="addVehicleInfo()" value="新增"/><div id="test"></div>
       </th>
     </tr>
     <tr bgcolor="F3F4F8">
       <th align="center">VIN</th>
       <th align="center">发动机号</th>
       <th align="center">车型</th>
       <th align="center">生产日期</th>
       <th align="center">里程</th>
       <th align="center">客户姓名</th>
       <th align="center">客户电话</th>
       <th align="center">销售日期</th>
       <th align="center">备注</th>
       <th align="center">操作</th>
     </tr>
     <tbody id="itemTable">
	 </tbody>
  </table>
  <table class="table_list">
    <tr > 
      <th height="12" align=center>
       <input type="button" id="saveBtn" name="saveBtn" onclick="save()" class="normal_btn" style="width=8%" value="保存"/>
        &nbsp;&nbsp;
       <input type="button" id="reportBtn" name="reportBtn" onclick="report()" class="normal_btn" style="width=8%" value="上报"/>
        &nbsp;&nbsp;
       <input type="button" onclick="closeMe();" class="normal_btn" style="width=8%" value="返回"/></th>
    </tr>
  </table>
  <!-- 资料显示区结束 -->
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<script type="text/javascript">
var vin_str="";//选择的vin列表
var add_id=1;

function closeMe(){
	goSearchPage();
}
//增加车辆信息
function addVehicleInfo(){
	var addTable = document.getElementById("itemTable");
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	insertRow.insertCell(4);
	insertRow.insertCell(5);
	insertRow.insertCell(6);
	insertRow.insertCell(7);
	insertRow.insertCell(8);
	insertRow.insertCell(9);
	insertRow.insertCell(10);
	insertRow.insertCell(11);
	addTable.rows[length].cells[0].innerHTML =  "<td><input type='text' name='vin"+length+"' id='vin"+length+"' value='' class='middle_txt' readonly='readonly' onclick='showVIN("+length+")'></td>";
	add_id=length;
	addTable.rows[length].cells[1].id="engine_no"+length;
	addTable.rows[length].cells[2].id = "vehicle_type"+length;
	addTable.rows[length].cells[3].id = "factory_date"+length;
	addTable.rows[length].cells[4].id = "vin_mileage"+length;
	addTable.rows[length].cells[5].id = "customer_name"+length;
	addTable.rows[length].cells[6].id = "customer_phone"+length;
	addTable.rows[length].cells[7].id = "sale_date"+length;
	addTable.rows[length].cells[8].id = "remark"+length;
	addTable.rows[length].cells[9].innerHTML = "<input type='button' class='normal_btn'  value='删除'  name='button42' onClick='javascript:delItem("+length+");'/>";
}
//获取VIN的方法 调用索赔单维护VIN显示
function showVIN(row_id){
	var goUrl="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/getDetailByVinForward.do?row_id="+row_id;
	OpenHtmlWindow(goUrl,800,500);
}
//获取子页面传过来的数据
function setVIN(row_id,VIN,LICENSE_NO,MODEL_NAME,SERIES_NAME,REARAXLE_NO,GEARBOX_NO,ENGINE_NO,COLOR,PRODUCT_DATE,PURCHASED_DATE,CUSTOMER_NAME,CERT_NO,MOBILE,ADDRESS_DESC,HISTORY_MILE,MODEL_ID,BRAND_NAME){
    if(VIN==null||VIN==""){
    	MyAlert("获得车辆信息失败，请重新选择！");
       return;
    }
   var flag=checkVinIsSelected(VIN);
   if(flag=="1"){
   	  MyAlert("VIN已经添加过，请重新选择！");
   	  return;
   }
    var v_PRODUCT_DATE=formatDate(PRODUCT_DATE);
    var v_PURCHASED_DATE=formatDate(PURCHASED_DATE);
    var v_MOBILE=formatNull(MOBILE);

	document.getElementById("vin"+row_id).value=VIN;
	document.getElementById("engine_no"+row_id).innerHTML = ENGINE_NO+'<input type="hidden" id="engine_no'+VIN+'" name="engine_no'+VIN+'" value="'+ENGINE_NO+'"/>';
	document.getElementById("vehicle_type"+row_id).innerHTML = MODEL_NAME+'<input type="hidden" id="vehicle_type'+VIN+'" name="vehicle_type'+VIN+'" value="'+MODEL_NAME+'"/>';
	document.getElementById("factory_date"+row_id).innerHTML = v_PRODUCT_DATE+'<input type="hidden" id="factory_date'+VIN+'" name="factory_date'+VIN+'" value="'+PRODUCT_DATE+'"/>';
	document.getElementById("vin_mileage"+row_id).innerHTML = '<input type="text" id="vin_mileage'+VIN+'" name="vin_mileage'+VIN+'" class="short_txt" value="'+HISTORY_MILE+'" datatype="0,is_digit,5" />';
	document.getElementById("customer_name"+row_id).innerHTML = '<input type="text" id="customer_name'+VIN+'" name="customer_name'+VIN+'" class="short_txt" value="'+CUSTOMER_NAME+'" datatype="0,is_letter_cn,30" />';
	document.getElementById("customer_phone"+row_id).innerHTML = '<input type="text" id="customer_phone'+VIN+'" name="customer_phone'+VIN+'" class="short_txt" value="'+v_MOBILE+'" datatype="0,is_phone,30" />';
	document.getElementById("sale_date"+row_id).innerHTML = v_PURCHASED_DATE+'<input type="hidden" id="sale_date'+VIN+'" name="sale_date'+VIN+'" value="'+PURCHASED_DATE+'"/>';
	document.getElementById("remark"+row_id).innerHTML = '<textarea "remark'+VIN+'" name="remark'+VIN+'" rows="1" cols="15" datatype="0,is_textarea,200"></textarea>';

	//将新添加的vin缓存到vin_str中
	if(vin_str==null||vin_str==""){
		vin_str=VIN;
    }else{
    	vin_str+=(","+VIN);
    }
}
//删除项目
function delItem(row_id){
	var addTable = document.getElementById("itemTable");
	var del_vin=document.getElementById("vin"+row_id).value;
    //删除
	var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/delVinInfo.json?del_vin="+del_vin+"&xh_ord_id="+<%=beanInfo.getId()%>+"&row_id="+row_id;
	makeNomalFormCall(submit_url,CallForDel,'fm','createOrdBtn');
}
//删除回调
function CallForDel(json){
	var retCode=json.retCode;
	var row_id=json.row_id;
	var addTable = document.getElementById("itemTable");
	var del_vin=document.getElementById("vin"+row_id).value;
	var addTable = document.getElementById("itemTable");
	if(retCode=="success"){
	   vin_str=delVinStr(del_vin);
	   addTable.removeChild(addTable.rows[row_id]);
       MyAlert("删除成功！");
	}else{
	   MyAlert("删除失败！");
    }
}
//将VIN从vin_str去除
function delVinStr(del_vin){
	var temp="";
	var arr=vin_str.split(",");
	for(var i=0;i<arr.length;i++){
       if(arr[i]!=del_vin){
    	   temp+=arr[i]+",";
       }
	}
	return temp.substring(0,temp.length-1);
}
//验证vin是否已经添加
function checkVinIsSelected(newVin){
	if(vin_str.length==0){
		return "0";
	}
	var arr=vin_str.split(",");
	for(var i=0;i<arr.length;i++){
       if(arr[i]==newVin){
          return "1";
       }
	}
	return "0";
}
//获得总费用
function blurBack(obj){
	if(obj.indexOf("fee")>0){
	   var road_bridge_fee=Number(formatNumber(document.getElementById("road_bridge_fee").value));
	   var vehicle_extra_fee=Number(formatNumber(document.getElementById("vehicle_extra_fee").value));
	   var lodging_fee=Number(formatNumber(document.getElementById("lodging_fee").value));
	   var eat_extra_fee=Number(formatNumber(document.getElementById("eat_extra_fee").value));
	   var person_extra_fee=Number(formatNumber(document.getElementById("person_extra_fee").value));
	   var total_fee=Number(formatNumber(document.getElementById("total_fee").value));
	   total_fee=road_bridge_fee+vehicle_extra_fee+lodging_fee+eat_extra_fee+person_extra_fee;
	   document.getElementById("total_fee").value=total_fee;
	}else if(obj.indexOf("date")>0){
		var out_start_date=document.getElementById("out_start_date").value;
		var out_end_date=document.getElementById("out_end_date").value;
		if(out_start_date==null||out_start_date==''||out_end_date==null||out_end_date=='') return;

		calBetweenDays(out_start_date,out_end_date);
	}
}
//回写外出天数功能
function calendarCallBack(obj){
	var out_start_date=document.getElementById("out_start_date").value;
	var out_end_date=document.getElementById("out_end_date").value;
	if(out_start_date==null||out_start_date==''||out_end_date==null||out_end_date=='') return;

	calBetweenDays(out_start_date,out_end_date);
}
function calBetweenDays(start_day,end_day){
	oDate1 = new Date(start_day.split("-").join("/"));//转换为2002/12/18格式 
	oDate2 = new Date(end_day.split("-").join("/"));
	if((oDate1 - oDate2)>0){
		document.getElementById("cal_out_days").innerHTML=" 天";
	}else{
		var out_days=parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24)+parseInt(1);
		document.getElementById("cal_out_days").innerHTML=out_days+"天";
	}
}
//格式化时间
function formatDate(value){
  return String.format(value.substring(0,10));
}
//格式化取出空的现象
function formatNumber(value){
	if(value==null||value==""||value=="undefined"||typeof(value)== "undefined") return 0;
	return value;
}
//格式化取出空的现象
function formatNull(value){
	if(value==null||value==""||value=="undefined"||typeof(value)== "undefined") return "";
	return value;
}
//保存
function save(){
	if(document.getElementById("out_name").value.length<=0||document.getElementById("out_name").value.replace(' ','')==''){
       MyAlert("您还没有填写出差人员!");
       return;
	}
	if(document.getElementById("apply_content").value.length<=0||document.getElementById("apply_content").value.replace(' ','')==''){
		MyAlert("您还没有填写申请内容!");
       return;
	}
	if(vin_str.length<=0){
		MyAlert("您还没有添加车辆信息!");
       return;
	}
	if(!submitForm('fm')) {
		return false;
	}
	var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/saveOutFeeApplyOrd.json?vin_str="+vin_str+"&xh_ord_id="+<%=beanInfo.getId()%>;
	makeNomalFormCall(submit_url,CallForSave,'fm','createOrdBtn');
}
function CallForSave(json){
	var retCode=json.retCode;
	if(retCode=="success"){
	   MyConfirm("保存成功!",goSearchPage,"");
	}else if(retCode=="failure_001"){
       MyAlert("保存失败，无法获得登陆人当前信息！");
	}
}
//上报
function report(){
	MyConfirm("确认已经保存数据了？",callReport,"");
}
function callReport(){
	var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/reportOrdStatus.json?xh_ord_id="+<%=beanInfo.getId()%>;
	makeNomalFormCall(submit_url,CallAfterReport,'fm','createOrdBtn');
}
function CallAfterReport(json){
	var retCode=json.retCode;
	if(retCode=="success"){
	   document.getElementById("saveBtn").disabled="disabled";
	   document.getElementById("reportBtn").disabled="disabled";
	   MyConfirm("上报成功!",goSearchPage,"");
	}else if(retCode=="failure_001"){
       MyAlert("上报失败，无法获得登陆人当前信息！");
	}else if(retCode=="failure_002"){
       MyAlert("该特殊费用申请单不存在，请先保存再上报！");
	}else if(retCode=="failure_003"){
       MyAlert("该特殊费用申请单还没有保存，请先保存再上报！");
	}else if(retCode=="failure"){
       MyAlert("上报失败！");
	}
}
function doInit(){
	  loadcalendar();
}
//回到查询页面
function goSearchPage(){
	fm.action="<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/queryListPage.do";
    fm.submit();
}
</script>
</BODY>
</html>