<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
var cloMainPart =1;
</script>
<TITLE>索赔单维护</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/RepairOrderAction/repairOrderQuerylist.json?action=query";
				
	var title = null;

	var columns = [
					{header: "大区", width:'10%', dataIndex: 'AREA_NAME'},
					{id:'id',header: "经销商代码", width:'10%', dataIndex: 'DEALER_CODES'},
					{header: "经销商名称", width:'11%', dataIndex: 'DEALER_SHORTNAME'},
					//{header: "索赔申请单号", width:'15%', dataIndex: 'claimNo'},
					{header: "工单号", width:'15%', dataIndex: 'RO_NO'},
					{header: "活动代码", width:'15%', dataIndex: 'CAMPAIGN_CODE'},
					{header: "结算基地", width:'7%', dataIndex: 'BALANCE_YIELDLY',renderer:getItemValue},
					{header: "车牌号", width:'7%', dataIndex: 'LICENSE'},
					//{header: "提交次数", width:'5%', dataIndex: 'submitTimes'},
					{header: "购车日期", width:'15%', dataIndex: 'PURCHASED_DATE'},
					{header: "VIN", width:'15%', dataIndex: 'VIN'},
					{header: "物料名称", width:'15%', dataIndex: 'MATERIAL_NAME'},
					{header: "车辆颜色", width:'15%', dataIndex: 'COLOR_NAME'},
					{header: "车系", width:'15%', dataIndex: 'GROUP_NAME'},
					{header: "车型", width:'15%', dataIndex: 'MODEL'},
					{header: "车主", width:'15%', dataIndex: 'OWNER_NAME'},
					{header: "车主电话", width:'15%', dataIndex: 'OWNER_PHONE'},
					{header: "送修人", width:'15%', dataIndex: 'DELIVERER'},
					{header: "送修人电话", width:'15%', dataIndex: 'DELIVERER_PHONE'},
					{header: "工单开始时间", width:'15%', dataIndex: 'RO_CREATE_DATE',renderer:formatDate},
					{header: "进厂里程数", width:'15%', dataIndex: 'IN_MILEAGE'},
					{header: "工单状态", width:'15%', dataIndex: 'RO_STATUS',renderer:getItemValue},
					{header: "单据保养次数", width:'15%', dataIndex:'FREE_TIMES'},
					//{header: "申请日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					//{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	function clrTxt(value1,value2){
		document.getElementById(value1).value = "";
		document.getElementById(value2).value = "";
	 
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		return value+"-"+record.data.LINE_NO;
	}
	function formatNum(value,metadata,record){
		if(record.data.REPAIR_TYPE_CODE!=<%=Constant.REPAIR_TYPE_04%>){
		return "";
		}else{
		return value;
		}
	}
	//单位代码渲染函数
	function dezero(value,metadata,record) {
		if (value==0){
			return "";
		}else {
			return value;
		}
	}
	//格式化时间为YYYY-MM-DD
	function formatDate(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
	//修改的超链接设置
	function myLink1(value,meta,record){
		/*var rostatus=record.data.RO_STATUS;
			if(rostatus==<%=Constant.RO_STATUS_02%>){
			return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=4&ID="
				+ value + "&gdid=" + record.data.GDID +"\">[明细]</a>");
			}
			if(rostatus!=<%=Constant.RO_STATUS_02%>){
					if(record.data.forlStatus==<%=Constant.RO_FORE_01%>){
						return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=4&ID="
							+ value + "&gdid=" + record.data.GDID + "\">[明细]</a>");
					}
					if(record.data.forlStatus==0){
						return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?type=4&ID="
								+ value + "\">[修改]</a>");
					}
				}
			if(rostatus==<%=Constant.RO_STATUS_01%>&&record.data.forlStatus!=<%=Constant.RO_FORE_01%>){
				return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?type=4&ID="
						+ value + "\">[修改]</a>");
			}
			else{

				return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roModifyForward.do?type=4&ID="
						+ value + "\">[明细]</a>");
			}*/
			var urlView="<%=contextPath%>/OrderAction/orderView.do?ro_no="+record.data.RO_NO+"&id="+value;
		return String.format("<a href='"+urlView+"' onclick=''>[明细]</a>");
			

	}
	//设置超链接
	function mySelect(value,meta,record){
  		return String.format(
         "<a href=\"#\" onclick='sel(\""+record.data.orderId+"\")'>"+ value +"</a>");

	}
	//具体操作
	function sel(){
		MyAlert("超链接！");
	}
	
	//取得已经选择的checkbox，拼接成字符串，各项目以,隔开
	function getCheckedToStr(name) {
		var str="";
		var chk = document.getElementsByName(name);
		if (chk==null){
			return "";
		}else {
		var l = chk.length;
		for(var i=0;i<l;i++){        
			if(chk[i].checked)
			{            
			str = chk[i].value+","+str; 
			}
		}
			return str;
		}
	}
	//上报
	function submitId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认上报？",submitApply,[str]);
		}else {
			MyAlert("请选择至少一条要上报的申请单！");
		}
	}
	//上报操作
	function submitApply (str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplySubmit.json?orderIds='+str,returnBack0,'fm','queryBtn');
		
	}
	//上报回调函数
	function returnBack0(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("上报成功！");
		}else{
			MyAlert("上报失败！请联系管理员！");
		}
	}
	//删除
	function deleteId(){
	var str=getCheckedToStr("orderIds");
		if (str!=""){
		MyConfirm("确认删除？",deleteApply,[str]);
		}else {
			MyAlert("请选择至少一条要删除的申请单！");
		}
	}
	function deleteApply(str) {
		
			makeNomalFormCall('<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyDelete.json?orderIds='+str,returnBack,'fm','queryBtn');
		
	}
	//删除回调函数
	function returnBack(json){
		var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
	//回调函数
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			goBack();
			MyConfirm("新增成功！点击确认返回查询界面或者点击左边菜单进入其他功能！","window.location.href = '<%=request.getContextPath()%>/sysmng/orgmng/DlrInfoMng/queryAllDlrInfo.do'");
		}else if(json.ACTION_RESULT == '2'){
			MyAlert("新增失败！请重新载入或者联系系统管理员！");
		}
	}
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyQuery.json";
	}
	function queryPer(){
	var star = $('RO_CREATE_DATE').value;
	var end = $('DELIVERY_DATE').value;
	var vin = $('VIN').value;
	  if((star==""||end =="")&&vin==""){
	  	MyAlert("查询时间和VIN至少选择一个!");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
	 	 __extQuery__(1);
	  }
	}
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation"><img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;车辆信息管理&gt;维修工单登记<span style="color: red;font-weight: bold;">(只能查询2015-5-25之后的数据)</span></div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">工单号：</td>
            <td><input name="RO_NO" id="RO_NO" value="" type="text" class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_7Letter" >VIN：</td>
 			<td  align="left">
 			  <input name="vin" id="VIN" maxlength="20" type="text"  value="" class="middle_txt"/>
 			  <span style="color: red">*</span>
 			<!--<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>-->
 			</td>
          </tr>
          <tr>
            <td  class="table_query_2Col_label_7Letter"></td>
            <td >
 		    </td>
            <td  class="table_query_2Col_label_7Letter">车型：</td>
            <td  align="left">
				<select id="model" name="model" class="short_sel">
					<option value=''>-请选择-</option>
					<c:forEach  items="${model}" var="mode1">
						<option value="${mode1.MODEL_CODE}" title="${mode1.MODEL_CODE}">${mode1.MODEL_CODE}</option>
					</c:forEach>
				</select>
 		    </td> 	      	         
          </tr>                   
          <tr>
            <td class="table_query_2Col_label_7Letter">开工单日期：</td>
  			<td align="left" nowrap="true">
			<input name="RO_CREATE_DATE" value="${startDate}" type="text" class="short_time_txt" id="RO_CREATE_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'RO_CREATE_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="DELIVERY_DATE" value="${endDate}" type="text" class="short_time_txt" id="DELIVERY_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'DELIVERY_DATE', false);" /> 
			 <span style="color: red">*</span>
		</td>	
            <td  class="table_query_2Col_label_7Letter">申请单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("RO_STATUS",<%=Constant.RO_STATUS%>,"<%=Constant.RO_STATUS_02%>",true,"short_sel","","false",'');
	        </script>
  			</td>
          </tr>
          <tr>
          	<td class="table_query_2Col_label_7Letter">经销商：</td>
             <td nowrap="nowrap">
           			<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" readonly="readonly"/>
            		<input class="mini_btn" type="button" id="dealerBtn" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');"/> 
           			<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           			<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerId', 'dealerCode');"/>
  			</td>
  			<td  class="table_query_2Col_label_7Letter">活动代码：</td>
            <td><input name="ACTIVITY_CODE" id="ACTIVITY_CODE" value="" type="text" class="middle_txt" />
            </td>
          </tr>
           <tr>
  			<td class="table_query_2Col_label_7Letter">活动主题代码：</td>
            <td align="left"><input name="ACTIVITY_MAIN" id="ACTIVITY_MAIN" value="" type="text" class="middle_txt" />
            </td>
             <td  class="table_query_2Col_label_7Letter"></td>
  			<td >
  			</td>
          </tr>
          		   <tr>
  			<td class="table_query_2Col_label_7Letter">所选区域：</td>
            <td align="left">
            	<input type="text" name="area_code" id="area_code" class="middle_txt" readonly="readonly"/>
 				<input type="hidden" name="area_id" id="area_id"/>
			    <input class="mini_btn" type="button"  value="&hellip;" onclick="showOrg('area_code' ,'area_id' ,true,'');"/> 
				<input type="button" class="normal_btn" value="清空" onclick="clrTxt('area_code','area_id');"/>
            </td>
             <td  class="table_query_2Col_label_7Letter">选择物料组：</td>
  			<td align="left">

	       <input type="text"  name="groupCode" size="15" id="groupCode" class="middle_txt" readonly="readonly"/>
	      <input name="button3" type="button" class="mini_btn" onclick="showGroup();" value="...." />
           <input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
          </td>

          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="queryPer();" />
			 <input id="export" class="normal_btn" type="button" name="button" value="导出"  onClick="toExport();" />
			<span style="color: red">注：时间和VIN至少输入一个</span></td>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
<script type="text/javascript">
function showGroup(){
	OpenHtmlWindow('<%=request.getContextPath()%>/repairOrder/RoMaintainMain/showGroup.do',900,700);
}

function setMainCode(str){
	$('groupCode').value=str;
}
function toExport(){

	var star = $('RO_CREATE_DATE').value;
	var end = $('DELIVERY_DATE').value;
	var vin = $('VIN').value;
	  if((star==""||end =="")&&vin==""){
	  	MyAlert("查询时间和VIN至少选择一个!");
	 	 return false;
	  }else if(star>end){
	  	MyAlert("开始时间不能大于结束时间");
	  	return false;
	  }else {
	   var s1 = star.replace(/-/g, "/");
		var s2 = end.replace(/-/g, "/");
		var d1 = new Date(s1);
		var d2 = new Date(s2);
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24));
		fm.action = "<%=request.getContextPath()%>/RepairOrderAction/repairOrderQueryExport.do";
		fm.submit();
}
	  }

  function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }     
 // $('CREATE_DATE_STR').value=showMonthFirstDay();
  //$('CREATE_DATE_END').value=showMonthLastDay();
 // $('RO_CREATE_DATE').value=showMonthFirstDay();
 // $('DELIVERY_DATE').value=showMonthLastDay();
 
</script>
</html>