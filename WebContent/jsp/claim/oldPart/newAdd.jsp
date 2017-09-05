<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/fmt" prefix="fmt" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>索赔旧件管理</title>
<% String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<script type="text/javascript">
	function wrapOut(){
		var a = $("transportTable");
		deleteRow1(a);
		$("dealer_id").value="";
		$("dealer_code").value="";
	}
	function deleteRow1(a){
		if(a.childNodes.length>0){
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
			for(var j=0;j<a.childNodes.length;j++){
				a.removeChild(a.childNodes[j]);
				
			}
		}
	}
	function showAppList(){
		var chooseType=$('chooseType').value;
		var val="";
		if(chooseType=="1"){//按服务站
			var val=$("dealer_id").value;
			if(val==""){
				MyAlert("提示：请先选择服务站代码再新增！");
				return;
			}
		}
		OpenHtmlWindow('<%=contextPath%>/claim/oldPart/EmergencyDevice/showAppList.do?dealer_id='+val,800,500);
	}
	function setBackData(check){
		for(var i =0;i<check.length;i++){
			if(check[i].checked){
				var addTable = document.getElementById("transportTable");
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
				var values=check[i].value.split(",");
				 var ids= document.getElementsByName('part_id');
				for(var j =0;j<ids.length;j++){
					if(ids[j].value==values[1]){
						MyAlert("提示：您有选择重复的配件,我们将自动过滤选择重复的件！");
						return;
					}
				} 
				addTable.rows[length].cells[0].innerHTML =  '<td><input type="hidden" class="middle_txt" name="claim_id" value='+values[0]+' /><input type="hidden" class="middle_txt" name="part_id" value='+values[1]+' /><input type="hidden" class="middle_txt" name="claim_no" value='+values[2]+' />'+values[2]+'</td>';
				addTable.rows[length].cells[1].innerHTML =  '<td><input type="hidden" class="middle_txt" name="vin" value='+values[3]+' />'+values[3]+'</td>';
				addTable.rows[length].cells[2].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="part_code" value='+values[4]+' />'+values[4]+'</td>';
				addTable.rows[length].cells[3].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="part_name" value='+values[5]+'/>'+values[5]+'</td>';
				addTable.rows[length].cells[4].innerHTML =  '<td nowrap="true"><input type="hidden" class="middle_txt" name="problem_reason" value='+values[6]+' />'+values[6]+'</td>';
				addTable.rows[length].cells[5].innerHTML =  '<td><input type="button" class="normal_btn" value="删除" onclick="deleteRow(this);" /></td>'; 

			}
		}
	}
	//JS删除整行代码
	function deleteRow(obj){
		var row = obj.parentNode.parentNode; //所在行    
		var tb = row.parentNode.parentNode; //当前表格   
		var rowIndex = row.rowIndex; //所在行下标   
		tb.deleteRow(rowIndex); //删除当前行
	}
	function checkDis(){
		if($('type').value=="3"){
			$('btn1').style.display='none';
			$('btn2').style.display='none';
			$('jj').style.display='none';
			$('qc').style.display='none';
			$('xz').style.display='none';
			var txtN = document.getElementsByTagName("input"); 
			for(i=0;i<txtN.length;i++){  
				if(txtN[i].type=="text"){ 
				txtN[i].readOnly=true;   
				} 
			}
			$('borrowReason').readOnly=true;   
			$('productAddr').disabled=true;   
		} 
	}
	function goBack(){
		fm.action="<%=contextPath%>/claim/oldPart/EmergencyDevice/listShow.do";
		fm.submit();
	}
	function saveOrUpdare(type){
		var alertStr="";
		var str="productAddr,applyDept,borrowPerson,borrowNo,borrowDept,consigneePerson,consigneeAddr,consigneeEmail,borrowReason";
		var textStr="产地,申请部门,调件人,调件员工编号,调件单位,收件人,收件地址,收件人邮编,调件原因";
        if(str!=""){
			var strs= str.split(",");
			var textStrs= textStr.split(",");
			for(var i=0;i<strs.length;i++){
				if(document.getElementById(strs[i]).value.length==0){
					alertStr+=" ["+textStrs[i]+"] ";
				}
			}
			if(alertStr!=""){
				MyAlert("提示：请填写必填字段或选择{"+alertStr+"}");
				return;
			}
		}
        var part_id = document.getElementsByName("part_id"); 
       	if(0==part_id.length){
       		MyAlert("提示：点击红色新增项来请选择调件！");
       		return;
       	}
		fm.action="<%=contextPath%>/claim/oldPart/EmergencyDevice/saveOrUpdare.do?type="+type;
		fm.method='post';
	    fm.submit();
	}
	function showList(){
		OpenHtmlWindow('<%=contextPath%>/MainTainAction/emergencyMainTainList.do',800,500);
	}
	function setMainTain(PRODUCT_ADDR,APPLY_DEPT,BORROW_NO,BORROW_DEPT,CONSIGNEE_PERSON,CONSIGNEE_PHONE,CONSIGNEE_ADDR,CONSIGNEE_EMAIL,BORROW_PHONE,BORROW_REASON){
		$("applyDept").value=APPLY_DEPT;
	//	$("borrowPerson").value=BORROW_PERSON;
		
		$("borrowNo").value=BORROW_NO;
		$("borrowDept").value=BORROW_DEPT;
		$("consigneePerson").value=CONSIGNEE_PERSON;
		
		$("consigneePhone").value=CONSIGNEE_PHONE;
		$("consigneeAddr").value=CONSIGNEE_ADDR;
		$("consigneeEmail").value=CONSIGNEE_EMAIL;
		
		$("borrowPhone").value=BORROW_PHONE;
		$("borrowReason").value=BORROW_REASON;
	}
</script>
</head>
<body onload="checkDis();">
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;紧急调件新增
<input id="type" value="${type}" type="hidden" />
</div>
<form name="fm" id="fm">
	<table class="table_edit" width="100%;" >
		<input id="chooseType" value="${chooseType}" name="chooseType" type="hidden" />
		<tr>
			<td width="10%"></td>
			<input name="id" id="id" value="${po.id}" type="hidden" />
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		产地：
          	</td>
          	<td width="15%">
          		<script type="text/javascript">
  					genSelBoxExp("productAddr",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
  				</script>
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >申请部门：</td>
 			 <td width="15%">
	           	<input name="applyDept" id="applyDept" maxlength="100" value="${po.applyDept }" type="text" class="middle_txt" />
 		    </td> 	
 		    <td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">调件人：</td>
             <td width="15%">
           		<input name="borrowPerson" id="borrowPerson" maxlength="25" readonly="readonly" value="${login_user }" type="text" class="middle_txt" />
            </td>    
            <td width="15%"></td>    
          </tr>
 
          <tr>
          	<td width="10%"></td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">调件员工编号：</td>
             <td width="15%">
           		<input name="borrowNo" id="borrowNo" value="${po.borrowNo }" maxlength="50" type="text" class="middle_txt" />
            </td> 
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		调件单位：
          	</td>
          	<td width="15%">
          		<input name="borrowDept" id="borrowDept" value="${po.borrowDept }" maxlength="100" type="text" class="middle_txt" />
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
          		收件人：
          	</td>
 			 <td width="15%">
	           	<input name="consigneePerson" id="consigneePerson" maxlength="50" value="${po.consigneePerson }" type="text" class="middle_txt" />
 		    </td> 	
             <td width="15%"></td>
          </tr>
          <tr>
          	<td width="10%"></td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">收件人联系电话：</td>
             <td width="15%">
           		<input name="consigneePhone" id="consigneePhone" maxlength="25" value="${po.consigneePhone }" type="text" class="middle_txt" />
            </td> 
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		收件地址：
          	</td>
          	<td width="15%">
          		<input name="consigneeAddr" id="consigneeAddr" maxlength="200" value="${po.consigneeAddr }" type="text" class="middle_txt" />
          	</td>
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter" >
          		收件人邮编：
          	</td>
 			 <td width="15%">
	           	<input name="consigneeEmail" id="consigneeEmail" maxlength="25" value="${po.consigneeEmail }" type="text" class="middle_txt" />
 		    </td> 	
             <td width="15%"></td>
          </tr>
          <tr>
          	<td width="10%"></td>
          	
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_8Letter">调件人联系电话：</td>
          	 <td width="15%" nowrap="true" >
           		<input class="middle_txt" id="borrowPhone" value="${po.borrowPhone }"  name="borrowPhone" type="text" />
				<input type="hidden" name="borrow_phone" id="borrow_phone" value="${po.borrowPhone }" />
             </td> 
          	
          	<c:if test="${chooseType==1 }">
	          	<td nowrap="true"  width="10%" class="table_query_2Col_label_5Letter">经销商代码：</td>
	             <td width="15%" nowrap="true" >
	           		<input class="middle_txt" id="dealer_code" value="${po.dealerCode }"  name="dealerCode" type="text" readonly="readonly"/>
					<input type="hidden" name="dealerId" id="dealer_id" value="${po.dealerId }" />
	 				<input type="button" id="xz" value="选择" class="min_btn"  onclick="showOrgDealer('dealer_code','dealer_id','false','','false','','10771002');"/>
	 				<input type="button" id="qc" value="清除" class="min_btn" onclick="wrapOut();"/>
	            </td> 
            </c:if>
            
            
            
            
            
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_7Letter">
          		调件原因：
          	</td>
          	<td width="55%" colspan="4" >
             	<textarea  cols="35" rows="4" class="middle_txt"  id="borrowReason"  name="borrowReason">${po.borrowReason }</textarea>
          	</td>
          </tr>
          
        <tr>
          	<td width="10%"></td>
          	
          	<td nowrap="true"  width="10%" class="table_query_2Col_label_8Letter">要求到货时间：</td>
          	 <td width="15%" nowrap="true" >
           		<input name="requireDate" type="text" value="<fmt:formatDate value="${po.requireDate }" pattern='yyyy-MM-dd'/>" id="requireDate" readonly="readonly" onfocus="calendar();" class="middle_txt"/>
				<input type="hidden" name="require_date" id="require_date" value="${po.requireDate }" />
             </td> 
       		 <td nowrap="true"  width="10%" class="table_query_2Col_label_8Letter"> 信息带出选择：</td>
          	 <td width="15%" nowrap="true" >
          	 	<a href="#" onclick="showList();">点击选择维护的信息</a>
             </td> 
          </tr>
	</table>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;调件项<a href="#"  id='jj' onclick="showAppList();" style="color: red;">【新增】</a>
</div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_edit">
		<tr>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">索赔单号</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">VIN</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">配件代码</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">配件名称</th>
			<!-- <th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">件号</th>
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">条码</th> -->
			<th width="10%" class="table_query_2Col_label_7Letter">故障描述</th>
			<c:if test="${type!=3 }">
			<th width="10%" nowrap="true"class="table_query_2Col_label_7Letter">操作</th>
			</c:if>
		<tr>
		<tbody id="transportTable">
			 <c:forEach items="${borrowSubclassList}" var="t">
				<tr>
					<td>
						<input type="hidden" class="middle_txt" name="claim_id" value="${t.claimId }" />
						<input type="hidden" class="middle_txt" name="part_id" value="${t.partId }" />
						<input type="hidden" class="middle_txt" name="claim_no" value="${t.claimNo }"/>${t.claimNo }
					</td>
					<td><input type="hidden" class="middle_txt" name="vin" value="${t.vin }" />${t.vin }</td>
					<td nowrap="true"><input type="hidden" class="middle_txt" name="part_code" value="${t.partCode }" />${t.partCode }</td>
					<td nowrap="true"><input type="hidden" class="middle_txt" name="part_name" value="${t.partName }"/>${t.partName }</td>
					<td nowrap="true"><input type="hidden" class="middle_txt" name="problem_reason" value="${t.problemReason }" />${t.problemReason }</td>
					<c:if test="${type!=3 }">
					<td><input type="button" class="normal_btn" value="删除" onclick="deleteRow(this);" /></td>
					</c:if>
				</tr>
			</c:forEach> 
		</tbody>
	</table>
	<br>
	<br>
	<%-- <!-- 添加附件 -->
			<table class="table_info" width="100%" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids" />
				<tr colspan="8">
					<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />
						&nbsp;附件列表：
					</th>
					<th id="fj">
						<span align="left"><input type="button" class="normal_btn"
								onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
					</th>
				</tr>
				<tr>
					<td width="100%" colspan="2"><jsp:include
							page="${contextPath}/uploadDiv.jsp" /></td>
				</tr>
				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table>
			<br> --%>
			<br>
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td colspan="4" align=center>
						<input type="button" onClick="saveOrUpdare(18041001);" id="btn1" class="normal_btn"
							style="" value="保存" />&nbsp;
						<input type="button" onClick="saveOrUpdare(18041002);" id="btn2"  class="normal_btn"
							style="" value="下发" />&nbsp;
						<input type="button" onClick="history.go(-1);" class="normal_btn"
							style="" value="返回" />
					</td>
				</tr>
			</table>
</form>

</html>