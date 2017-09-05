<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date" %>
<%
	String contextPath = request.getContextPath();
	SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>新增客户回访信息</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 回访管理 &gt;新增客户回访</div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="seId" name="seId" />
		<input type="hidden" id="ctmId" name="ctmId" />
		<input type="hidden" id="vinStr" name="vinStr" />
		<table width="100%" class="tab_edit">
			<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />客户回访信息</th>
			
			<tr>
				<td width="20%" align="right" >客户名称：</td>
				<td width="30%" align="left" >
					<textarea rows="3" cols="20" readonly name="ctmname" id ="ctmname"></textarea>
					<input class="normal_btn" type="button" value="选择" name="alertCusScreen" id="alertCusScreen" onclick="alertCusScreenEvent()"/>
					<input class="normal_btn" type="button" value="清除" name="clearSel" id="clearSel" onclick="clearEvent();"/>
				</td>
				<td width="20%" align="right" >回访电话：</td>
				<td width="30%" align="left">
					<textarea rows="3" cols="20" readonly name="tele" id ="tele"></textarea>
				</td>
			</tr>
			<tr>
				<td align="right" >回访类型：</td>
				<td><script type="text/javascript">
		           genSelBoxExp("rvType",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","true",'');
					  </script>
				</td>
				<td align="right" >无人接听次数：</td>
				<td align="left">
					<input class="middle_txt" type="text" id="rvTimes" name="rvTimes" datatype="1,is_digit,20" maxlength="20"/>
				</td>
			</tr>
			<tr>
				<td align="right" >回访人：</td>
				<td align="left" >
					<input class="middle_txt" type="text" id="seName" name="seName" datatype="0,is_null,20" readonly />
					<input name="search" id="search" class="normal_btn"  onclick="querySet();" type="button" value="选择" />
				</td>
				<td align="right" >回访状态：</td>
				<td align="left" >
					<script type="text/javascript">
				           genSelBoxExp("rvStatus",<%=Constant.RV_STATUS%>,null,true,"short_sel","","true",'<%=Constant.RV_STATUS_5%>,<%=Constant.RV_STATUS_6%>');
				           function doCusChange(value){
				        	   if(value == '<%=Constant.RV_STATUS_1%>'){
				        		  document.getElementById("isShow").style.display = "none";
				        	   } else {
				        		  document.getElementById("isShow").style.display = "block";
				        	   }
				           }
					  </script>
				</td>
			</tr>
			<tr id="isShow">
				<td align="right" >回访结果：</td>
				<td align="left">
					<script type="text/javascript">
			           genSelBoxExp("rvResult",<%=Constant.RD_IS_ACCEPT%>,null,true,"short_sel","","true",'');
				   </script>
				</td>
				<td align="right" >是否满意</td>
				<td align="left" >
					 <script type="text/javascript">
				           genSelBoxExp("rvSatisfAction",<%=Constant.GRADE_TYPE%>,null,true,"short_sel","","true",'');
					  </script>
				</td>						
			</tr>
			<tr>
				<td align="right" >生成日期：</td>
				<td align="left" >
					<input  name="rvDate" id="rvDate" readonly
		              value="<%=simpleFormate.format(new Date())%>" 
		              type="text" class="short_txt" datatype="0,is_date,10" />
		              <input class="time_ico" onclick="showcalendar(event, 'rvDate', false);" type="button" value=" " />
				</td>
				<td align="right" >回访问卷:</td>
				<td align="left" >
					<input type="hidden" name="qrId" id="qrId" />
					<input class="middle_txt" type="text" name="qrName" id="qrName"  datatype="0,is_null,25" maxlength="25" readOnly/>
					<input class="normal_btn" type="button" value="选择" name="selQuestion" id="selQuestion" onclick="choiseQuestion();"/>
				</td>						
			</tr>
			<tr>
				<td align="right" >备注：</td>
				<td align="left"  colspan="3">
					<textarea id="remark" name="remark" rows="5" style="width: 95%"></textarea>
				</td>
			</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					<input class="normal_btn" type="reset" value="重置" name="rsBtn" id="rsBtn" />
					&nbsp;
        		</td>
			</tr>
		</table>
		
	</form>
	<script type="text/javascript">
	
		function querySet(){
				OpenHtmlWindow('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/choiseSet.do',800,500);
		}
		
		function setSeat(seId,seName){
			document.getElementById("seId").value = seId;
			document.getElementById("seName").value = seName;
		}
		
		function save(){
			//验证
			if(valid()){
				//document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/saveCrmReturn.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.isSuccess != null && json.isSuccess=='0'){
				parent.MyAlert("操作成功!<br/>"+json.message);
			}else if(json.isSuccess != null && json.isSuccess=='1'){
				parent.MyAlert("保存失败,请联系管理员!");
			}
			window.location.href = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/addReviewInit.do";
			document.getElementById("saveButton").disabled = false;
		}
		
		function alertCusScreenEvent(){
			openWindowDialog('<%=contextPath%>/customerRelationships/complaintConsult/ComplaintAcceptYx/showCustomListMultiple.do');
		}
		
		//问卷选择
		function choiseQuestion(){
			OpenHtmlWindow('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/showQuestion.do',800,500);
		}
		
		function choiseReturn(qrId,qrName){
			$("qrId").value = qrId;
			$("qrName").value = qrName;
		}
		
		function openWindowDialog(targetUrl){
		    var height = 800;
		    var width = 1000;
		    var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		    var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		    var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		    winHandle = window.open(targetUrl,null,params);
		    return winHandle;
	 	}
		
		function changeData(ctmName,phone,ctmId,vinStr){
			setTextData('ctmname',checkNull(ctmName),true);
			setTextData('tele',checkNull(phone),false);
			setTextData('ctmId',checkNull(ctmId),true);
			setTextData('vinStr',checkNull(vinStr),true);
		}
		
		function clearEvent(){
			$("ctmname").value = "";
			$("tele").value = "";
			$("ctmId").value = "";
			$("vinStr").value = "";
		}
		
		function checkNull(str){
			if(str == '' || str == 'null' || str == null) {
				str = '';
			}
			return str;
		}
		function setTextData(id,value,isdisabled){
			if(value == null) {
				value = ""; 
				retrun;
			}
			
			document.getElementById(id).value = value;
			document.getElementById(id).readonly = isdisabled;
		}
		
		//非空验证
		function valid(){
			if(!submitForm('fm')) {
				return false;
			}
			var customerId = document.getElementById("ctmname").value;
			var vinStr = document.getElementById("vinStr").value;
			if(customerId == "" || vinStr == ""){
				MyAlert("请选择客户!");
				return false;
			}
			var tel = document.getElementById("tele").value;
			if(tel == ""){
				MyAlert("请输入回访电话!");
				return false;
			}
			var rvType = document.getElementById("rvType").value;
			if(rvType == ""){
				showTip(document.getElementById("rvType"),"请选择回访类型",getTip());
				return false;
			}
			var reId = document.getElementById("seId").value;
			var reName = document.getElementById("seName").value;
			if(reId == "" || reName == ""){
				MyAlert("请选择回访人!");
				return false;
			}
			var rvStatus = document.getElementById("rvStatus").value;
			if(rvStatus == ""){
				showTip(document.getElementById("rvStatus"),"请选择回访状态",getTip());
				return false;
			}
			if(document.getElementById("isShow").style.display == "block"){
				var rvResult = document.getElementById("rvResult").value;
				if(rvResult == ""){
					showTip(document.getElementById("rvResult"),"请选择回访结果",getTip());
					return false;
				}
				var rvSatisfAction = document.getElementById("rvSatisfAction").value;
				if(rvSatisfAction == ""){
					showTip(document.getElementById("rvSatisfAction"),"请选择是否满意",getTip());
					return false;
				}
			}
			return true;
		}
	</script>
</body>
</html>