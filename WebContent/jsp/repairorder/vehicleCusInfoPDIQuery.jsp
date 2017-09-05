<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<TITLE>索赔单维护</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/repairOrder/RoMaintainMain/vehicleCusInfoQuery.json?action=query";
				
	var title = null;

	var columns = [
	           		{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"checkId\")' />", width:'3%',align:'center',sortable: false,dataIndex: 'VIN',renderer:myCheckBox},
					{header: "VIN", width:'11%', dataIndex: 'VIN',renderer:showDetail},
					{header: "发动机号", width:'15%', dataIndex: 'ENGINE_NO'},
					{header: "车牌号", width:'7%', dataIndex: 'LICENSE_NO'},
					{header: "是否指定PDI", width:'15%',dataIndex: 'IS_PDI',renderer:isPDI},
					{header: "是否内销车", width:'10%', dataIndex: 'IS_DOMESTIC',renderer:isInCar},
					{header: "车型配置", width:'15%', dataIndex: 'MODEL_NAME'},
					{header: "车主姓名", width:'15%', dataIndex: 'CUS_NAME'},
					{header: "销售日期", width:'15%', dataIndex: 'SALES_DATE',renderer:formatDate},
					{header: "生产日期", width:'15%', dataIndex: 'PRODUCT_DATE',renderer:formatDate},
					{header: "产地", width:'15%', dataIndex: 'AREA_NAME'},
					{header: "行驶里程数", width:'15%', dataIndex: 'MILEAGE'},
					{header: "经销商代码", width:'15%', dataIndex: 'DEALER_CODE'},
					{header: "经销商购车日期", width:'15%', dataIndex: 'PURCHASED_DATE'},
					{header: "保养次数", width:'5%', dataIndex: 'FREE_TIMES'},
					{header: "车辆状态", width:'15%', dataIndex: 'LIFE_CYCLE',renderer:getItemValue}
		      ];
	function myCheckBox(value,metaDate,record){
		var input='<input type="checkbox" id="checkId" name="checkId" value="'+value+'" />';
		return String.format(input);
	}
	function isPDI(value,meta,record){
		if(value==0){
			return String.format("否");
		}
		return String.format("是");
	}
	function isInCar(value,meta,record){
		if(value==0){
			return String.format("否");
		}
		return String.format("是");
	}
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	function showDetail(value,meta,record){
		var url="<a href=\"javascript:OpenHtmlWindow('<%=contextPath%>/repairOrder/RoMaintainMain/vehicleCusInfoDetail.do?vehicleId="+record.data.VEHICLE_ID+"&VIN="+record.data.VIN+"',1350,450)\">[" + value + "]</a>";
  		return String.format(url);

  		
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		return value+"-"+record.data.LINE_NO;
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
				+ value + "\">[明细]</a>");
			}
			if(rostatus!=<%=Constant.RO_STATUS_02%>){
					if(record.data.forlStatus==<%=Constant.RO_FORE_01%>){
						return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=4&ID="
							+ value + "\">[明细]</a>");
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
		return String.format("<a href=\"<%=contextPath%>/repairOrder/RoMaintainMain/roBalanceDetail.do?type=4&action=detail&ID="
				+ value + "\">[明细]</a>");
			

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
//设置超链接 end
</script>
</HEAD>
<BODY onload="doInit()">
<div class="navigation"><img src="../../img/nav.gif" />&nbsp;当前位置：售后服务管理 >车辆信息管理 >车辆车主信息查询</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_7Letter">VIN：</td>
            <td><input name="VIN" id="VIN" value="" type="text" datatype="1,is_digit_letter,50" class="middle_txt" />
            </td>
            <td rowspan="1" class="table_query_2Col_label_7Letter" >车牌号：</td>
 			<td rowspan="1"  align="left">
 			<input name="CAR_NO" id="CAR_NO" value="" type="text" datatype="1,is_null,50" class="middle_txt" />
 			</td>
          </tr>
                         
          <tr>
            <td class="table_query_2Col_label_7Letter">车辆状态：</td>
             <td nowrap="nowrap">
           <script type="text/javascript">
           genSelBoxExp("CAR_STATUS",<%=Constant.CAR_STATUS%>,"",true,"short_sel","","false",'');
	        </script>
  			</td>
            <td  class="table_query_2Col_label_7Letter">产地：</td>
  			<td >
  			 <select name="yieldly" id="yieldly">
  			 <option value="" >
    				-请选择-
    			  </option>
	              <c:forEach var="areaPO" items="${areaPO}" >
 				  <option value="${areaPO.areaId}" >
    				<c:out value="${areaPO.areaName}"/>
    			  </option>
    			 </c:forEach>
             </select>
  			</td>
          </tr>
           <tr>
            <td class="table_query_2Col_label_7Letter">车主姓名：</td>
             <td nowrap="nowrap">
          <input name="CUS_NAME" id="CUS_NAME" value="" type="text" class="middle_txt" />
  			</td>
            <td  class="table_query_2Col_label_7Letter">是否PDI:&nbsp;&nbsp;</td>
  			<td >
  				<select name="isPDI">
  					<option value="">-请选择-</option>
  					<option value="0">否</option>
  					<option value="1">是</option>
  				</select>
  			</td>
          </tr>
  
    	  <tr>
            <td colspan="4" align="center" nowrap>
            <input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
            &nbsp;
            <input id="queryBtn" class="long_btn" type="button" name="button" value="批量转PDI"  onClick="changePDI();" />
            &nbsp;
            <input id="queryBtn" class="long_btn" type="button" name="button" value="批量转内销车"  onClick="changeInCar();" />
            &nbsp;
            <input id="queryBtn" class="long_btn" type="button" name="button" value="批量反转PDI"  onClick="nochangePDI();" />
            &nbsp;
            <input id="queryBtn" class="long_btn" type="button" name="button" value="批量反转内销车"  onClick="nochangeInCar();" />
			</td>
            <td  align="right" ></td>
  </table>
	<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
</BODY>
<script type="text/javascript">
  function clrDlr()
  {
	  document.getElementById('dealerCode').value='';
	  document.getElementById('dealerId').value='';
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
  $('CREATE_DATE_STR').value=showMonthFirstDay();
  $('CREATE_DATE_END').value=showMonthLastDay();
  $('RO_CREATE_DATE').value=showMonthFirstDay();
  $('DELIVERY_DATE').value=showMonthLastDay();
  function changePDI(){
	  commmon(1);
  }
  function changeInCar(){
	  commmon(2);
  }
  function nochangePDI(){
	  commmon(3);
  }
  function nochangeInCar(){
	  commmon(4);
  }
  function commmon(type){
	  var checkids=document.getElementsByName("checkId");
		 var ids="";
		 for (var i=0; i<checkids.length;i++){
			 if(checkids[i].checked){
				 ids+=checkids[i].value+",";
			 }
		 }
		 if(ids==""){
			 MyAlert("提示:请先选择！");
			 return;
		 }
		var url='<%=request.getContextPath()%>/feedbackmng/apply/ServiceCarApply/servicecarapplyChange.json?ids='+ids+'&type='+type;
		makeNomalFormCall(url,backCommon,'fm','queryBtn');
  }
  function backCommon(json){
	  var avl = json.returnValue;
		if(avl==1){
			__extQuery__(1);
			MyAlert("转化成功！");
		}else{
			MyAlert("转化失败！请联系管理员！");
		}
  }
</script>
</html>