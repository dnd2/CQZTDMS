<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
    String dealerId = request.getParameter("dealerId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function doOriginType(){
		var radio=document.getElementById("60151017");
	    radio.checked=true;
		}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>销售线索分派</title>
</head>
<body onunload='javascript:destoryPrototype();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>团队管理>线索自动分配人员维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input type="hidden" name="leadsCode" id="leadsCode" value="" />
		<input type="hidden" name="leadsCode2" id="leadsCode2" value="" />
		<input type="hidden" name="leadsGroup2" id="leadsGroup2" value="" /> 
		<input type="hidden" name="leadsCodeGroup2" id="leadsCodeGroup2" value="" />   
	<table class="table_query" width="95%" height="100" align="center">
		<tr>
		<font style="font-weight: bold;font-size:130%;">线索来源类型：</font>
		</tr>
		<tr>
			<c:forEach items="${leadsTypeList }" var="leadsTypeList" varStatus="status">
			<td>
			<input type="radio" id=${leadsTypeList.CODE_ID } name="leadsOrigin" value=${leadsTypeList.CODE_ID }  
			<c:if test="${OriginType==leadsTypeList.CODE_ID}"> checked="checked" </c:if> 
			onclick="checkLeadsOrigin()"/>
			<font style="font-weight: bold;font-size:100%;">${leadsTypeList.CODE_DESC } </font>
			</td>
			<c:if test="${status.count%4==0}"> 
			<tr> </tr> 
			</c:if> 
		    </c:forEach>
		</tr>
  	</table>
  		
  		
  	<table id="qcguwen"  style="display: block" class="table_query" width="95%" height="100" align="center">
  				
  		<tr>
  		<font style="font-weight: bold;font-size:130%;">配置顾问人员列表：</font>
  		</tr>
		<tr>
			<c:forEach items="${adviserList }" var="adviserList" varStatus="status">
			<td>
			<input type="checkbox" name="adviser"  value=${adviserList.USER_ID } 
			<c:if test="${OriginType==adviserList.LEADS_ORIGIN}"> checked="checked" </c:if> 
			onclick=""/>
			${adviserList.NAME }
			</td>
			<c:if test="${status.count%5==0}"> 
			<tr> </tr> 
			</c:if> 
		    </c:forEach>
		</tr>
  </table>
  
  
  
  
  <table id="baocun"  style="display: block"  class="table_query" width="95%" height="100" align="center">
					<tr>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					
							<td align="left">
								<input name="queryBtn" type="button" class="normal_btn"
									onclick="doSave();" id="saveButton" value="保存" />
							</td>
						
						
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td colspan="8">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				</table>
				
				
  			
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript" > 


	function doCheckOrigin(){
		MyAlert(222);
		var selectArray=document.getElementsByName('leadsOrigin');
		var originType = '';
		if(isSelectCheckBox(selectArray)){
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					originType = originType + selectArray[i].value; 
				}
			}
		}
		
		window.location.href="<%=contextPath%>/crm/sysUser/DealerSysLeads/doUserOriginSave.do?originType="+originType;
		}

	function doSave(){
		var selectArray=document.getElementsByName('leadsOrigin');
		var originType = '';
		var userId='';
		var selectArray2=null;
		if(isSelectCheckBox(selectArray)){
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					originType = originType + selectArray[i].value; 
				}
			}
		}

		selectArray2=document.getElementsByName('adviser');

		if(isSelectCheckBox(selectArray2)){
			for(var i=0;i<selectArray2.length;i++) {
				if(selectArray2[i].checked) {
					userId = userId + selectArray2[i].value + ",";
				}
			}
			
		}
		document.getElementById("saveButton").disabled = true;
		window.location.href="<%=contextPath%>/crm/sysUser/DealerSysLeads/doUserOriginSave.do?originType="+originType+"&userId="+userId;
		MyAlert('保存成功!');
		}

	function checkLeadsOrigin(){
		var selectArray=document.getElementsByName('leadsOrigin');
		var selectArray2=document.getElementsByName('adviser');
		var qcguwen = document.getElementById("qcguwen");
		var ycguwen = document.getElementById("ycguwen");
		var baocun= document.getElementById("baocun");
		var originType=null;
		if(isSelectCheckBox(selectArray)){
			for(var i=0;i<selectArray.length;i++) {
				if(selectArray[i].checked) {
					originType=selectArray[i].value;
				}
			}
		}

		window.location.href="<%=contextPath%>/crm/sysUser/DealerSysLeads/doCarInit.do?originType="+originType;
	
		}
	


	function batchAuditing(selectArray)
	{
		//数组转为字符串
		var leadsGroup = '';
		for(var i=0;i<selectArray.length;i++) {
			if(selectArray[i].checked) {
				leadsGroup = leadsGroup + selectArray[i].value + ",";
			}
		}
		leadsGroup=leadsGroup.substring(0,leadsGroup.length-1);
		window.location.href="<%=contextPath%>/crm/dealerleadsmanage/DlrLeadsManage/dlrLeadsAllotByDlrInit.do?leadsGroup="+leadsGroup; 
	}
	//检测是否选择了checkBox
	function isSelectCheckBox(cbArray){
		if(cbArray!=null && cbArray.length>0){
			for(var i=0;i<cbArray.length;i++){
				if(cbArray[i].checked)
					return true;
			}
		}else{
			return false;
		}
		return false;
	}

</script>    
</body>
</html>