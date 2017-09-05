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
<TITLE>质量信息跟踪卡</TITLE>
<SCRIPT LANGUAGE="JavaScript">
	var myPage;
	//查询路径
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationQuery.json";
				
	var title = null;

	var columns = [
					//{id:'id',header: "经销商代码", width:'10%', dataIndex: 'dealerCode'},
					//{header: "经销商名称", width:'11%', dataIndex: 'dealerName'},
					{header: "索赔申请单号", width:'15%', dataIndex: 'claimNo'},
					{header: "工单号-行号", width:'15%', dataIndex: 'roNo',renderer:roLine},
					{header: "索赔类型", width:'7%', dataIndex: 'claimType',renderer:getItemValue},
					{header: "提交次数", width:'5%', dataIndex: 'submitTimes'},
					{header: "VIN", width:'15%', dataIndex: 'vin'},
					{header: "申请日期", width:'15%', dataIndex: 'createDate',renderer:formatDate},
					{header: "申请状态", width:'15%', dataIndex: 'status',renderer:getItemValue},
					{id:'action', width:'5%',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink1,align:'center'}
		      ];
	//设置超链接  begin      
	function doInit()
	{
   		loadcalendar();
	}
	//行号加工单号
	function roLine(value,metadata,record) {
		return value+"-"+record.data.lineNo;
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
		if (record.data.status=='<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>') {
			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID="
				+ value + "\",800,500)'>[明细]</a>");
		}else {
  			return String.format("<a href=\"<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillModifyForward.do?ID="
				+ value + "\">[修改]</a>");
		}
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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：质量信息跟踪卡&gt;质量信息跟踪卡</div>
  <form method="post" name ="fm" id="fm">
    <TABLE  class="table_query">
          <tr>
            <td  class="table_query_2Col_label_5Letter">索赔单号：</td>
            <td><input name="CLAIM_NO" id="CLAIM_NO" value="" type="text" datatype="1,is_digit_letter,20" class="middle_txt" />
            </td>
            <td  class="table_query_2Col_label_7Letter">工单号-行号：</td>
            <td  align="left" ><input type="text" name="RO_NO" id="RO_NO" datatype="1,is_digit_letter,19"  value="" class="middle_txt"/> - <INPUT name="LINE_NO" id="LINE_NO" value="" type="text"  datatype="1,is_digit" class="mini_txt"/></td>	            
          </tr>
          <tr>
            <td  class="table_query_2Col_label_5Letter">索赔类型：</td>
            <td >
			<script type="text/javascript">
	              genSelBoxExp("CLAIM_TYPE",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","true",'');
	       </script>
 		    </td> 	         
 			<td  class="table_query_2Col_label_7Letter">VIN：</td>
 			<td  align="left">
 			<!--  <input name="VIN" id="VIN" datatype="1,is_vin" type="text"  value="" class="middle_txt"/>-->
 			<textarea name="VIN" cols="18" rows="3" datatype="1,is_digit_letter"></textarea>
 			</td>
          </tr>                   
          <tr>
             <td class="table_query_2Col_label_5Letter">申请日期：</td>
             <td nowrap="nowrap">
            <input class="short_txt" type="text" name="RO_STARTDATE" id="RO_STARTDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_STARTDATE', false);"/>
            至
            <input class="short_txt" type="text" name="RO_ENDDATE" id="RO_ENDDATE"  datatype="1,is_date,10" group="RO_STARTDATE,RO_ENDDATE" hasbtn="true" callFunction="showcalendar(event, 'RO_ENDDATE', false);"/>
  			</td>
            <td  class="table_query_2Col_label_7Letter">申请单状态：</td>
  			<td >
  			<script type="text/javascript">
	              genSelBoxExp("STATUS",<%=Constant.CLAIM_APPLY_ORD_TYPE%>,"",true,"short_sel","","true",'<%=Constant.CLAIM_APPLY_ORD_TYPE_02%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_04%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_07%>,<%=Constant.CLAIM_APPLY_ORD_TYPE_05%>');
	       </script>
  			</td>
        
          </tr>
    	  <tr>
            <td colspan="4" align="center" nowrap><input id="queryBtn" class="normal_btn" type="button" name="button" value="查询"  onClick="__extQuery__(1);" />
			<input class="normal_btn"  type="button" name="button1" value="新增"  onClick="location='infoAddForward.do';"/>
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
</html>