<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>个人信息维护</title>
<script type="text/javascript">
	function search(){
		<%-- fm.action = '<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/maintainClaimPhone.json';
		submitForm('fm'); --%>
		submitForm(fm);
	    fm.action = "<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/maintainClaimPhone.do";
	    fm.submit();
	}
	
</script>
</head>
<body onunload='' onload="loadcalendar();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理  &gt; 个人信息管理   &gt;服务站人员及联系方式 </div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input type="hidden" name="COMMAND" id="COMMAND" value="1" />
	<input type=hidden name="dealer_id" id="dealer_id" value="${DEALER_ID }" />
	<input id="contactId" name="contactId" type="hidden" value="${po.id}" />
	<table class="table_query" border="0" >
      <tr>
      	<td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">职位：</span></div></td>
        <td align="left">
            <script type="text/javascript">
            	var perPose = <%=request.getAttribute("perPose")%>;
       			genSelBoxExp("CON_PER_POSE",<%=Constant.STATION_HEAD_AND_AEGIS%>,perPose,true,"short_sel","","false",'');
           </script>
        </td>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">姓名：</span></div></td>
        <td align="left">
            <input name="CON_PER_NAME" id="CON_PER_NAME" value="${perName }" type="text" class="middle_txt" size="17" />
        </td>
        <tr>
        <tr>
      	<td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">电话：</span></div></td>
        <td align="left">
            <input name="CON_PER_PHONE" id="CON_PER_PHONE" value="${perPhone }" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">状态：</span></div></td>
        <td align="left">
            <script type="text/javascript">
            	var status = <%=request.getAttribute("status")%>
       			genSelBoxExp("CON_STATUS",<%=Constant.STATUS%>,status,true,"short_sel","","false",'');
           </script>
        </td>
        <tr>
        <td colspan="4" align="center">
        <input name="button" id="queryBtn" type="button" onclick="javascript:search();" class="normal_btn"  value="查询" />
        <input class="normal_btn" type="button" value="保存" id="save_but"  onclick="save();"/>
        </td>
      </tr>
    </table>
		<table id="itemTableId" align="center" cellpadding="0" class="table_query"
				cellspacing="1" class="table_list" border=1
				style="border-bottom: 1px solid #DAE0EE">
				<th>
					<td colspan="2" align="left">服务站24小时热电话:</td>
					<c:choose>
						<c:when test="${listPo[0].HOT_LINE_PHONE==null }">
							<td colspan="2" align="left" ondblclick="bianInput(this,true,0);">0
						</c:when>
						<c:otherwise>
							<td colspan="2" align="left" ondblclick="bianInput(this,true,${listPo[0].HOT_LINE_PHONE });">${listPo[0].HOT_LINE_PHONE }
						</c:otherwise>
					</c:choose>
					
					</td>
					<td colspan="2">
						&nbsp;
					</td>
				</th>
				<tr>
				<td>
					职位
					</td>
					<td>
						姓名
					</td>
					<td>
						电话
					</td>
					<td>
						备注
					</td>
					<td>
						状态
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('itemTable');" />
					</td>
				</tr>
				<c:if test="${!empty listPo}">
					<c:forEach items="${listPo}" var="connect">
						<tr class="table_list_row1">
							<td><script type="text/javascript">writeItemValue(${connect.PER_POSE });</script>
							</td>
							<td>${connect.PER_NAME}
							</td>
							<td>${connect.PER_PHONE }</td>
							<td>${connect.PER_REMARK }</td>
							<td><script type="text/javascript">writeItemValue(${connect.STATUS });</script></td>
							<td><a href="#" onclick="deleteData(${connect.ID})">[删 除]</a>
							<a href="#" onclick="modifyRow(this,${connect.ID },${connect.PER_POSE },'${connect.PER_NAME}','${connect.PER_PHONE }','${connect.PER_REMARK }','${connect.STATUS }')">[修 改]</a>
							</td>
						</tr>
					</c:forEach>
				</c:if>
				
				<tbody id="itemTable">
				</tbody>
			</table>
				<table class="table_query" border="0">
<!--			<tr>-->
<!--		 		<td style="width: 10%;text-align: right;">&nbsp;登陆账号：&nbsp;</td>-->
<!--		  		<td>${user.acnt}</td>-->
<!--			</tr>-->
			<tr style="display:none;">
				<td style="text-align: right;">&nbsp;修改人: &nbsp;</td>
				<td>${acnt }</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		<br/>
	</form>
	<script type="text/javascript" >
		function save(){
			if(submitForm(fm)==false)return;
			var perPhone = document.getElementsByName('PER_PHONE');
			var perName = document.getElementsByName('PER_NAME');
			for(var i=0;i<perPhone.length;i++){
				if(perPhone[i].value==''||perPhone[i].value==null){
					MyAlert('请输入站长电话！');
					return;
				}
				if(perName[i].value==''||perName[i].value==null){
					MyAlert('请输入姓名！');
					return;
				}
			}
			
			MyConfirm('确认修改?',saveDo);
			
		}
		function saveDo(){
			fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveClaimPhone.do"
			fm.submit();
			var arrPhone = document.getElementsByName('PER_PHONE');
			for(var i=0;i<arrPhone.length;i++){
				arrPhone[i].value="";
			}
			//MyAlert(arrPhone[0].value)
		}
		
		
		 // 动态生成表格
 	function addRow(tableId){
 		var sel = "genSelBoxExpforThisPage('PER_POSE',"+<%=Constant.STATION_HEAD_AND_AEGIS%>+",'',false,'','','true','')";
 		var sel2 = "genSelBoxExpforThisPage('STATUS',"+<%=Constant.STATUS%>+",'<%=Constant.STATUS_ENABLE%>',false,'','','true','')";
	    var addTable = document.getElementById(tableId);
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
			addTable.rows[length].cells[0].innerHTML =  eval(sel);
			addTable.rows[length].cells[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="PER_NAME" datatype="1,is_null" class="short_txt" value="" size="5" /></span>'+
			'<input type=hidden name="CONNECT_ID" value=""/>'
			+'<span style="color:red">*</span></td>';
			addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" name="PER_PHONE" datatype="1,is_null" class="middle_txt"   value="" size="15" maxlength="11"  /><span style="color:red">*</span></td>';
			addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" name="PER_REMARK" class="long_txt"   value="" size="20" maxlength="21" /></td>';
			addTable.rows[length].cells[4].innerHTML =  eval(sel2);
			addTable.rows[length].cells[5].innerHTML =  "<a href=\"#\" onclick=\"deleteRowPre(this,'itemTableId')\">[删 除]<a>";
	}
		 
 // 动态生成表格
 	function modifyRow(obj,connectId,pose,name,phone,remark,status){
 		var sel = "genSelBoxExpforThisPage('PER_POSE',"+<%=Constant.STATION_HEAD_AND_AEGIS%>+",'"+pose+"',false,'','','true','')";
 		var sel2 = "genSelBoxExpforThisPage('STATUS',"+<%=Constant.STATUS%>+",'"+status+"',false,'','','true','')";
 		var tr = obj.parentNode.parentNode;
 		tr.childNodes[0].innerHTML =  eval(sel);
 		tr.childNodes[1].innerHTML =  '<td><span class="tbwhite"><input type="text" name="PER_NAME" datatype="1,is_null" class="short_txt" value="'+name+'" size="5" /></span><span style="color:red">*</span>'+
 		'<input type=hidden name="CONNECT_ID" value="'+connectId+'"/>'
 		+'</td>';
 		tr.childNodes[2].innerHTML =  '<td><input type="text" name="PER_PHONE" datatype="1,is_null" class="middle_txt"   value="'+phone+'" size="15" maxlength="11"  /><span style="color:red">*</span></td>';
 		tr.childNodes[3].innerHTML =  '<td><input type="text" name="PER_REMARK" class="long_txt"   value="'+remark+'" size="20" maxlength="21" /></td>';
 		tr.childNodes[4].innerHTML =  eval(sel2);
 		tr.childNodes[5].innerHTML =  "<a href=\"#\" onclick=\"deleteRowPre(this,'itemTableId')\">[删 除]<a>";
 		
	}
	//重写下拉框JS
	//根据FIXCODE形成下拉框，除了expStr字符串中包含的状态，expStr可为多个中间用逗号隔开
function genSelBoxExpforThisPage(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
	var str = "";
	var arr;
	if(expStr.indexOf(",")>0)
		arr = expStr.split(",");
	else {
		expStr = expStr+",";
		arr = expStr.split(",");
	}
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
	if(nullFlag && nullFlag == "true"){
		str += " datatype='0,0,0' ";
	}
	// end
	str += " onChange=doCusChange(this.value);> ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<codeData.length;i++){
		var flag = true;
		for(var j=0;j<arr.length;j++){
			if(codeData[i].codeId == arr[j]){
				flag = false;
			}
		}
		if(codeData[i].type == type && flag){
			str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
		}
	}
	str += "</select>";	
	return str;
}
			//删除行
	function deleteRowPre(obj,tableId){
	
	 MyConfirm("您确定要删除?",deleteRowConfirm,[obj,tableId]);
		//deleteRowConfirm(obj,tableId);
 	} 
	
	function deleteRowConfirm(obj,tableId){
	
		 var tabl=document.all[tableId];
		 var index = obj.parentElement.parentElement.rowIndex;
		 
		// var modelId = document.getElementsByName('modelIds')[index-1].value;
		 //backToCarkd(modelId);
		 
		 tabl.deleteRow(index); 

		// countSeq(tableId);

		// countMoney();
	}
	//
	//从数据库里删除数据
	function deleteData(id){
		 MyConfirm("您确定要删除?",deleteDataDo,[id]);
	}
	function deleteDataDo(id){
		fm.action="<%=contextPath %>/sysusermng/sysuserinfo/SysPasswordManager/delClaimPhone.do?contactId="+id;
		fm.submit();		
	}
	//查看修改历史记录
	function historyData(contactId){
		var url ="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/queryContactChangeHistory.do?CONTACT_ID="+contactId;
		OpenHtmlWindow(url,800,500);
	}
	//双击变成input
	function bianInput(obj,flag,val){
		if(flag){
			obj.innerHTML = "<input type=text name='HOT_LINE_PHONE' id='HOT_LINE_PHONE' value='"+val+"' onblur='bianInput(this,false,"+val+");'/>";
			document.getElementById('HOT_LINE_PHONE').focus();
		}else{
			//obj.parentNode.innerText = val;
			submitForm(fm);
		    fm.action = "<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveDealerHotLinePhone.do";
		    fm.submit();
		}
	}
	</script>
</body>
</html>