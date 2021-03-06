<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%String contextPath=request.getContextPath();%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<title>层维护--新增 </title>
</head>

<body onload="changeSub();">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;基础信息管理&gt;仓储相关信息维护&gt;层维护>新增
	</div>

	<form name="frm" method="post" id="frm">
		<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
			<tr> 
				<th colspan="8"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
	        </tr>
		 	<tr>
		 		<td align="right">库房：</td>
	            <td align="left">
	                <select name="WH_ID" id="WH_ID" class="short_sel" onchange="changeSub();">
	                  <c:forEach items="${wareHouseList}" var="wareHouse">
	                      <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                  </c:forEach>
	                </select>
	            </td>
		 		<td align="right">货位排代码：</td> 
		   		<td align="left">
				  <select name="LINE_ID" id="LINE_ID" class="short_sel" onchange="changeSubShelf();">
		                 <option selected value=''>-请选择-</option>
		             </select>
			  	</td>
				  <td align="right">货架代码：</td> 
				  <td align="left">
					  <select name="SHELF_ID" id="SHELF_ID" class="short_sel">
		                  <option selected value=''>-请选择-</option>
		              </select>
				  </td>  
		  		<td align="right">层数：</td>
			   	<td align="left">
				  <input type="text" id="FLOOR_CODE" name="FLOOR_CODE" class="middle_txt" datatype="0,is_null,30" maxlength="20" size="15" />
				</td> 
			 </tr>
            <tr style="line-height: 40px;">
                <td colspan="8" align=center>
                    <input type="button" id="saveButton" class="normal_btn" onclick="saveAdd();"  value="保存"/>&nbsp;&nbsp;
                    <input type="button" class="normal_btn" id="goBack"  onclick="back();"  value="返回"/>
                </td>
            </tr>
		</table>
		<!-- 基本信息end -->
	</form>
</body>
<script type="text/javascript">
	//添加--begin
	function saveAdd(){
		if(!submitForm("frm")){
			return;
		}
		MyConfirm("确认添加该信息！",save);
	}	
	function save()	{ 
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/addSave.json",addBack,'frm','queryBtn'); 
	}
	function addBack(json)	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			frm.action = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/init.do";
			frm.submit();
		}else if(json.returnValue == 3){
			disabledButton(["saveButton","goBack"],false);
			MyAlert("该货位层名字已被占用，请重新输入！");
		}else{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function back(){
		frm.action="<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/init.do";
		frm.submit();
	}
	//添加--end
	jQuery.noConflict();
	function changeSub(){
		var whId = jQuery("#WH_ID").val();
		if(whId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_LINE&column=WH_ID&parentId="+whId;
			makeCall(url2,forBack,para);
		}else{
			jQuery("#LINE_ID").find("option").remove();
			jQuery("#LINE_ID").append("<option value=''>-请选择-</option>");
		}
	}
	function forBack(json){
		jQuery("#LINE_ID").find("option").remove(); 
		jQuery("#LINE_ID").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var id = json.subcode[i].LINE_ID;
			var str = json.subcode[i].LINE_CODE;
			jQuery("#LINE_ID").append("<option value='"+id+"'>"+str+"</option>");
		}
	}
	function changeSubShelf(){
		var lineId = jQuery("#LINE_ID").val();
		if(lineId!=""){
			var url2 = "<%=contextPath%>/parts/storageManager/partFloorMgr/PartFloorMgr/getSubCode.json";
			var para = "table=TT_PART_LOCATION_SHELF&column=LINE_ID&parentId="+lineId;
			makeCall(url2,forBackShelf,para);
		}else{
			jQuery("#SHELF_ID").find("option").remove();
			jQuery("#SHELF_ID").append("<option value=''>-请选择-</option>");
		}
	}
	function forBackShelf(json){
		jQuery("#SHELF_ID").find("option").remove(); 
		jQuery("#SHELF_ID").append("<option value=''>-请选择-</option>");
		for(var i=0;i<json.subcode.length;i++){
			var id = json.subcode[i].SHELF_ID;
			var str = json.subcode[i].SHELF_CODE;
			jQuery("#SHELF_ID").append("<option value='"+id+"'>"+str+"</option>");
		}
	}
</script>
</html>
