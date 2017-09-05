<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运单回厂确认 </title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>运单回厂确认
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center">		
  		<td class="right">产地：</td>
  		<td align="left">
  			<select id="areaId" name="areaId" class="u-select">
  				<option value="">--请选择--</option>
  				<c:forEach items="${areaList}" var="po">
  					<option value="${po.AREA_ID }">${po.AREA_NAME }</option>
  				</c:forEach>
  			</select>
  		</td>
		<td class="right">选择经销商：</td>
		<td align="left">
      		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
			<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
		</td>
 </tr>
 <tr class="csstr" align="center">			
		<td class="right">承运商：</td> 
	  <td align="left">
		 <select name="LOGI_NAME" id="LOGI_NAME" class="selectlist" >
		 	<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  		</td>
	  		<td class="right">运单号：</td>
		<td align="left">
			<input type="text" maxlength="20"  name="billNo" datatype="1,is_digit_letter,30" maxlength="30" id="billNo" class="middle_txt"/>
		</td>
 </tr>
 <tr>
 	<td class="right" nowrap="true">发运时间：</td>
   <td align="left" nowrap="true">
			<input name="sendStartDate" type="text" maxlength="20"  class="middle_txt" id="sendStartDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendStartDate', false);" />  	
             &nbsp;至&nbsp;
             <input name="sendEndDate" type="text" maxlength="20"  class="middle_txt" id="sendEndDate" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sendEndDate', false);" /> 
		</td>
		<td></td>	
		<td></td>	
 </tr>
 <tr align="center">
 		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
 		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
   		  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   		
   		</td>
 </tr>
</table>
<table class="table_query" align="center" style="display: none" id="tab_remark" >
		    <tbody>
		      <tr>
		        <th nowrap="nowrap" align=left>
		        	<img class=nav src="<%=contextPath %>/img/subNav.gif"/> 
		        	回厂确认
		        </th>
		      </tr>
		    </tbody>
		  </table>
		    <table class=table_query id="tab_remark1" style="display: none">
		    	<tr align="left">
		    		<td align="left" nowrap="nowrap">
		    			实际到达时间:
		    			<input name="arrive_time" type="text" maxlength="20"  class="middle_txt" id="arrive_time" value="${sqlDate}" readonly="readonly"/> 
						<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'arrive_time', false);" />
						备注：
						<input name="remark" type="text"  class="middle_txt" datatype="1,is_textarea,1000" maxlength="1000" id="remark" style="width: 300px"/> 
			 			<input type="button" id="queryBtn" class="normal_btn"  value="批量确认" onclick="returnBatchCheck()" /> 
		    		</td>
		    	</tr>
		    </table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->

</form>
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/sendBillReturnListQuery.json";
	var title = null;
	var columns = [
					{header: "序号",align:'center',renderer:getIndex},
	                {id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"billIDs\")' />", width:'6%',sortable: false,dataIndex: 'BILL_ID',renderer:myCheckBox},
					{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
					{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
					{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
					//{header: "发票号",dataIndex: 'INVOICE',align:'center'},
					{header: "发运时间",dataIndex: 'BILL_CRT_DATE',align:'center'},
					{header: "承运商",dataIndex: 'LOGI_FULL_NAME',align:'center'},
					{header: "车辆数量",dataIndex: 'VEH_NUM',align:'center'},
					{header: "已确认数量",dataIndex: 'BACKCRM_NUM',align:'center'},
					{id:'action',header: "操作", align:'center',sortable: false,dataIndex: 'BILL_ID',renderer:myLink}
		      ];
	

	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	//操作 
	function myLink(value,meta,record){
	       return String.format(
	       		 "<a href=\"<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/toReturnCheck.do?billId="
	               +value+"\">[明细确认]</a>");
	}
	function doInit(){
		  //初始化时间控件
		//__extQuery__(1);
	}
	function customerFunc(){
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("billIDs");
		if(arrayObj.length>0){//大于0表表示有数据，备注显示
			document.getElementById("tab_remark").style.display="";
			document.getElementById("tab_remark1").style.display="";
		}else{
			document.getElementById("tab_remark").style.display="none";
			document.getElementById("tab_remark1").style.display="none";
		}
	}
	function myCheckBox(value,metaDate,record){
		return String.format("<input type=\"checkbox\" name='billIDs'  value='" + value+ "' />");
		
	}
	function returnBatchCheck(){
		var arrive_time = document.getElementById("arrive_time");
		var temp = document.getElementsByName("billIDs");
		var flag = false;
		if(temp != null){
			for(var i = 0 ; i < temp.length ; i++){
				if(temp[i].checked){
					flag = true;
				}
			}
		}
		if(!flag){
			MyAlert("请选择需要回厂确认的运单!");
			return false;
		}
		if(arrive_time.value==""){
			MyAlert("请选择实际到达时间!");
			return false;
		}
		MyConfirm("是否对选中的运单进行回厂确认？",subR);
	}
	function subR(){
		var URL = "<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/batchReturnCheck.json";
		makeNomalFormCall(URL,showResult,'fm');
	}
	function showResult(json){
		if(json.value){
			if(json.value == "1"){
				parent.MyAlert("操作成功!");
				window.location.href = "<%=contextPath%>/sales/storage/sendmanage/SendBillReturnCheck/sendBillReturnCheckInit.do";
			}
		}
	}
</script>
</body>
</html>
