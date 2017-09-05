<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>投诉受理处理（管理员）</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function watchComplaintSearch(cpid,ctmid){		
		window.open('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintConsult/complaintConsultWatch.do?openPage=1&cpid='+cpid+'&ctmid='+ctmid,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=900") ;
	}
</script>
</head>
<body onload="changeBizTypeEvent(9506,'${complaintAcceptMap.BIZCONT}',false)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系 &gt; 投诉咨询管理 &gt;咨询处理（管理员）</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
		<input type="hidden" id="cpId" name="cpId" value="${complaintAcceptMap.CPID}">
		<input type="hidden" id="id" name="id" value="0">
			<th colspan="5" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉单信息</th>
			<tr>
				<td align="center" width="10%" rowspan="4">客户信息</td>
				<td width="20%" align="right" >投诉咨询单号：</td>
				<td align="left" colspan="3">
					${complaintAcceptMap.CPNO}
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" >客户名称：</td>
				<td width="25%" align="left" >
					${complaintAcceptMap.CTMNAME} <%-- <input class="normal_btn" type="button" value = "查看" onclick="watchComplaintSearch('${cpid }','${ctmid}')"/> --%>
				</td>
				<td width="20%" align="right" >客户电话：</td>
				<td width="25%" align="left">
					${complaintAcceptMap.PHONE}
				</td>
			</tr>
			<tr>
				<td align="right">联系人：</td>
				<td>${complaintAcceptMap.PERSON}</td>
				<td align="right">联系电话：</td>
				<td>${complaintAcceptMap.CPPHONE}</td>
			</tr>
			<tr>
				<td align="right" >省份：</td>
				<td align="left" >
					${complaintAcceptMap.PRO}
				</td>
				<td align="right" >城市：</td>
				<td align="left">
					${complaintAcceptMap.CITY}
				</td>
			</tr>
			<tr>
				<td align="center" rowspan="4">车辆信息</td>
				<td align="right" >VIN：</td>
				<td align="left" >
					${complaintAcceptMap.VIN}
				</td>
				<td width="15%" align="right" >是否有车：</td>
				<td colspan="3" align="left" >
					<c:choose>
						<c:when test="${complaintAcceptMap.CUSTOMER eq '0'}">
						有车
						</c:when>
						<c:otherwise>
						无车
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<%-- <td align="right" >里程范围：</td>
				<td align="left" >
					${complaintAcceptMap.MILEAGERANGE}
				</td> --%>
				<td align="right" >车辆性质：</td>
				<td align="left">
					${complaintAcceptMap.NATURE}
				</td>
				<td align="right" >行驶里程：</td>
				<td align="left">
					${complaintAcceptMap.MILEAGE}	
				</td>
			</tr>
			<tr>
				<td align="right" >车系：</td>
				<td align="left" >
					${complaintAcceptMap.SERIESID}
				</td>
				<td align="right" >车型：</td>
				<td align="left">
					${complaintAcceptMap.MODELID}
				</td>
				
			</tr>
			<tr>
				<td align="right" >生产日期：</td>
				<td align="left" >
					${complaintAcceptMap.SDATE}
				</td>
				<td align="right" >购车日期：</td>
				<td align="left">
					${complaintAcceptMap.BDATE}
				</td>
			</tr>

			<tr>
				<td align="center" rowspan="2">业务类型</td>
				<td align="right" nowrap="true">业务类型：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.BIZTYPE}
				</td>
				<td align="right" nowrap="true">内容类型：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.BIZCONT}
				</td>
			</tr>
			<tr>
				<td align="center" >投诉咨询内容</td>
				<td align="left"  colspan="4">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%;">${complaintAcceptMap.CPCONT}</textarea>
				</td>
			</tr>
			<tr >
				<td align="center">处理结果</td>
					<td valign="middle" colspan="4">
						<table width="100%" class="voidTab">
								<tr>
							      <th align="center" width="20%">处理时间</th>
							      <th align="center" width="60%">处理内容</th>
							      <th align="center" width="8%">当前处理人</th>
							      <th align="center" width="12%">处理状态</th>
						       </tr>
						            <c:forEach items="${dealRecordList}" var="dealR">
						              <tr>
						              	<td align="center">${dealR.CDDATE}</td>
						              	<td align="left">${dealR.CDCONT}</td>
						              	<td align="center">${dealR.USERNAME}</td>
						              	<td align="center">${dealR.STATUS}</td>
						              	</tr>
						             </c:forEach>
						</table>
					</td>
				</tr>
			
			<tr id="dealModelTr">
				<td align="center" >处理状态</td>
				<td align="left" colspan="4">
					<select id="dealStatus" name="dealStatus" class="short_sel"  onchange="changeDealModelEvent(this.value)">
						<option value=''>-请选择-</option>
						<option value='0'>处理中</option>
						<option value='1'>已处理</option>
					</select>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr >
				<td align="center">回复内容</td>
				<td align="left"  colspan="4">
					<textarea id="ccont" name="ccont" rows="5" style="width: 95%">${map.CP_CONTENT }</textarea>
					<font color="red">*</font>
				</td>
			</tr>
			
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					&nbsp;
					<input name="addBtn" type="button" class="normal_btn"  value="返回" onclick="history.back();" />
        		</td>
			</tr>
		</table>
		
	</form>
<script type="text/javascript">
		function check(){
			var msg ="";
			if(""==document.getElementById('dealStatus').value){
				msg+="处理状态不能为空!</br>"
			}
			if(""==document.getElementById('ccont').value){
				msg+="回复内容不能为空!</br>"
			}
			if(msg!=""){
				MyAlert(msg);
				return false;
			}else{
				return true;
			}
		}
		//   判断长度是否合格 
		// 
		// 引数 s   传入的字符串 
		//   n   限制的长度n以下 
		function WidthCheck(s, n){   
			var w = 0;   
			for (var i=0; i<s.length; i++) {   
			   var c = s.charCodeAt(i);   
			   //单字节加1   
			   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
			    w++;   
			   }   
			   else {   
			    w+=2;   
			   }   
			}   
			if (w > n) {   
			   return false;   
			}   
			return true;   
		} 
	
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/adviceUpdateSubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.success != null && json.success=='true'){
				MyAlertForFun("保存成功!",goBack);
			}else{
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		//页面跳转：
		function goBack(){
			window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptZx/getComplaintAcceptZxInit.do";
		}

	</script>
</body>
</html>