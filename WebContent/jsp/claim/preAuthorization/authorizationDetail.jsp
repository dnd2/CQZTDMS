<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>索赔申请创建</title>
	</HEAD>
	<BODY >
		<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔预授权&gt;索赔单预授权明细
		</div>

		<form method="post" name="fm" id="fm">
			<input type = "hidden"  value = "true" id ="isVin" name ="isVin" />
			<input type = "hidden"  value = "${foInfoMap.ID }" id ="foId" name ="foId" />
			<input type = "hidden"  value = "${foInfoMap.RO_NO }" id ="RO_NO" name ="RO_NO" />
			<input type = "hidden"  value = "${vinInfo.SERIES_ID }" id ="SERIES_ID" name ="SERIES_ID" />
			<input type = "hidden"  value = "${vinInfo.MODEL_ID }" id ="MODEL_ID" name ="MODEL_ID" />
			<input type = "hidden"  value = "${foInfoMap.MAIN_PART_ID }" id ="PART_ID" name ="PART_ID" />
			<input type = "hidden" value = "${vinInfo.MAKER_NAME }" id ="MAKER_SHOTNAME" name ="MAKER_NAME"/>
			<input type = "hidden" value = "${vinInfo.VIN }" id ="VILIDATE_VIN" name ="VILIDATE_VIN"/>
			<input type = "hidden" value = "<fmt:formatDate value="${vinInfo.RO_CREATE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>" id ="RO_CREATE_DATE" name ="RO_CREATE_DATE"/>
			<input type = "hidden" id="ttt" name = "ttt"/>  
			  
			<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter"> 预授权号： </td>
					<td >
						<input type='text' name='FO_NO' id='FO_NO' value="${foInfoMap.FO_NO }" class="middle_txt" readonly="readonly"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_5Letter">车型：</td>
					<td>
						<input type='text' name='MODEL_NAME' id='MODEL_NAME' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.MODEL_NAME }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">服务站代码：</td>
					<td>
						<input type='text' name='SERVICE_CODE' id='SERVICE_CODE' 
							 class="middle_txt" readonly="readonly" value="${userInfo.DEALER_CODE }"/>
					</td>	
					
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter"> VIN： </td>
					<td>
						<input type='text' name='vin' id='vin'     maxlength="18"
							 class="middle_txt" value ="${vinInfo.VIN }" readonly="readonly"
							 datatype="1,is_vin"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_5Letter">配置：</td>
					<td >
						<input type='text' name='PACKAGE_NAME' id='PACKAGE_NAME' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.PACKAGE_NAME }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">
						<span class="zi">服务站简称：</span>					</td>
					<td>
						<input type='text' name='DEALER_SHORTNAME' id='DEALER_SHORTNAME' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.DEALER_SHORTNAME }"/>
					</td>
				</tr>
				<tr>
				<td align="right" class="table_edit_3Col_label_5Letter">
						<span class="zi">进厂里数：</span> </td>
					
					<td>
						<input type='text' name='MILEAGE' id='MILEAGE'
							 class="middle_txt" readonly="readonly" value ="${foInfoMap.IN_MILEAGE  }" />
					</td>
					
					
					<td align="right" class="table_edit_3Col_label_5Letter">颜色：</td>
					<td>
						<input type='text' name='CAR_COLOR' id='CAR_COLOR' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.COLOR }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_6Letter">索赔员：</td>
					<td>
						<input type='text' name='USER_NAME' id='USER_NAME' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.NAME }"/>
					</td>
				</tr>
				<tr>
					<td align="right" class="table_edit_3Col_label_5Letter">三包预警：</td>
					<td>
						<input type='text' name='vrLevel' id='vrLevel' 
							 class="middle_txt" readonly="readonly" value ="${vrLevel }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_5Letter">车辆用途：</td>
					<td >
						<input type='text' name='CAR_USE_DESC' id='CAR_USE_DESC' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.CAR_USE_DESC }"/>
					</td>
					
					<td align="right" class="table_edit_3Col_label_6Letter">索赔员电话：</td>
					<td >
						<input type='text' name='HAND_PHONE' id='HAND_PHONE' 
							 class="middle_txt" readonly="readonly" value ="${userInfo.HAND_PHONE }"/>
					</td>
				</tr>
				<tr>
					
					<td align="right" class="table_edit_3Col_label_5Letter">发动机号：</td>
					<td >
						<input type='text' name='ENGINE_NO' id='ENGINE_NO' 
							 class="middle_txt" readonly="readonly" value ="${vinInfo.ENGINE_NO }"/>
					</td>
					<td align="right" class="table_edit_3Col_label_5Letter">购车日期：</td>
					<td >
						<input type='text' name='INVOICE_DATE' id='INVOICE_DATE' 
							 class="middle_txt" readonly="readonly" value ="<fmt:formatDate value="${vinInfo.INVOICE_DATE }" type="date" dateStyle="full" pattern="yyyy-MM-dd"/>"/>
					</td>
					
				</tr>
				
				
			</table>
			
			<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					作业项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						维修类型
					</td>
					<td>
						主损件代码
					</td>
					<td>
						主损件名称
					</td>
					<td>
						供应商代码
					</td>
					<td>
						最大预估金额
					</td>
					<td>
						是否回运
					</td>
					
				</tr>
				<tbody id="itemTable">
					<tr align="center" class="table_list_row1">
					<td>
						<script type="text/javascript">
						genSelBoxExp("REPAIR_TYPE",<%=Constant.REPAIR_TYPE%>,"${foInfoMap.APPROVAL_TYPE }",false,"min_sel","disabled ","false",'<%=Constant.REPAIR_TYPE_04%>,<%=Constant.REPAIR_TYPE_05%>,<%=Constant.REPAIR_TYPE_08%>');
				       	</script>
					</td>
					<td>
						<input type="text" name="PART_NO" class="middle_txt"   id="PART_NO" value="${partInfo.PART_NO }" datatype="0,is_null" readonly="readonly" />
					</td>
					<td>
						<input type="text" name="PART_NAME" class="middle_txt"   id="PART_NAME" value="${partInfo.PART_NAME }" datatype="0,is_null"  readonly="readonly"/>
					</td>
					<td>
						<input type="text" name="MAKER_CODE" class="short_txt"   id="MAKER_CODE" value = "${foInfoMap.MAKER_CODE }" datatype="0,is_null" readonly="readonly" />
					</td>
					<td>
						<input type="text" name="MAX_AMOUNT" class="short_txt"   id="MAX_AMOUNT" value = "${foInfoMap.MAX_AMOUNT }" datatype="1,isMoney,10" readonly="readonly" />
					</td>
					<td>
						<script type="text/javascript">
						genSelBox("IS_RETURN",<%=Constant.IS_RETURN%>,"${foInfoMap.IS_RETURN }",false,"min_sel","disabled","false",'');
				       	</script>
					</td>
					
				</tr>
					
				</tbody>
			</table>
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
				申请内容
				</th>
				<tr>
					<td  class="tbwhite">
						故障描述：
					</td>
					<td  class="tbwhite">
						<textarea readonly="readonly"  name="ERROR_DESC" id="ERROR_DESC" style="font-weight: bold;" datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='38'>${foInfoMap.ERROR_DESC }</textarea>
					</td>
					
					<td  class="tbwhite">
						原因分析及处理结果：
					</td>
					<td  class="tbwhite">
						<textarea readonly="readonly"  name='ERROR_REASON' style="font-weight: bold;" id="ERROR_REASON"	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='38'>${foInfoMap.ERROR_REASON }</textarea>
					</td>
					<td  class="tbwhite" style="display: none">
						处理方案：
					</td>
					<td  class="tbwhite" style="display: none">
						<textarea readonly="readonly"  name='ERROR_RESULT'  style="font-weight: bold;"id='ERROR_RESULT'	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'>${foInfoMap.REMARK }</textarea>
					</td>
				</tr>
				
				</table>
				<c:if test="${operType == '2' || foInfoMap.REPORT_STATUS != '11561001' }">
				<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
				审核内容
				</th>
				<tr>
					<td  class="tbwhite">
						审核结果：
					</td>
					<td  class="tbwhite">
						<textarea name="OPINION" id="OPINION" style="font-weight: bold;" <c:if test="${ operType == '1' || operType== '3' || operType== '4'}"> readonly="readonly"  </c:if> datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'>${foInfoMap.OPINION }</textarea>
					</td>
					
				</tr>
				
				</table>
				</c:if>
			<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" disabled onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
</table>
			
          <table id="compensationMoney" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					补偿费
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						申请金额
					</td>
					<td>
						审核金额
					</td>
					<td>
						申请备注
					</td>
					
<!-- 					<td> -->
<!-- 						<input id="itemBtn" type="button" class="normal_btn" value="新增" -->
<!-- 							name="button422" onClick="addCompensation();" /> -->
<!-- 					</td> -->
				</tr>
				<tbody id="itemTableAccessories">
					<td><input type="text" name="APPLY_AMOUNT" class="middle_txt" readonly="readonly"   id="APPLY_AMOUNT" value="${foInfoMap.APPLY_AMOUNT }" datatype="1,isMoney,10"  maxlength="10" /></td>
					<td><input name="AUDIT_AMOUNT" id="AUDIT_AMOUNT"  type="text" <c:if test="${operType != '2'}">readonly="readonly"</c:if> class="middle_txt" value="${foInfoMap.AUDIT_AMOUNT }" datatype="1,isMoney,10" maxlength="10" /></td>
					<td><input name="APPLY_REMARK" id="APPLY_REMARK" readonly="readonly"  value="${foInfoMap.APPLY_REMARK }" type="text" class="middle_txt" datatype="1,is_null,120"/></td>
				</tbody>
			</table>
			
			<table id="trailerMoney" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="<%=contextPath %>/img/subNav.gif" />
					背车费
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						申请金额
					</td>
					<td>
						审核金额
					</td>
					<td>
						审核备注
					</td>
					
<!-- 					<td> -->
<!-- 						<input id="itemBtn" type="button" class="normal_btn" value="新增" -->
<!-- 							name="button422" onClick="addTrailer();" /> -->
<!-- 					</td> -->
				</tr>
				<tbody id="itemTableAccessories">
					<td><input type="text" name="OUT_APPLY_AMOUNT" class="middle_txt" readonly="readonly"  id="OUT_APPLY_AMOUNT" maxlength="10" value = "${foInfoMap.OUT_APPLY_AMOUNT }" datatype="1,isMoney,10" /></td>
					<td><input type="text" name="OUT_AUDIT_AMOUNT" <c:if test="${operType != '2'}">readonly="readonly"</c:if> class="middle_txt"   id="OUT_AUDIT_AMOUNT" datatype="1,isMoney,10" value = "${foInfoMap.OUT_AUDIT_AMOUNT }"/></td>
					<td><input type="text" name="OUT_APPLY_REMARK" class="middle_txt" readonly="readonly"  id="OUT_APPLY_REMARK" value = "${foInfoMap.OUT_APPLY_REMARK }"  datatype="1,is_null,120" /> </td>
				</tbody>
			</table>
          
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2" align=center>
<!-- 						<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp; -->
<!-- 		                <input class="normal_btn" type="button" id="btn2" value="授权历史" onclick="auditingHistory();"/>&nbsp; -->
<!-- 		                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp; -->
						<c:if test="${operType == '2' }">
							<input type="button" onClick="confirmAudit('1');" id="audit_but" name= "audit_but" class="normal_btn"
							style="" value="通过" />&nbsp;
						<input type="button" onClick="confirmAudit('2');" id="back_but" name= "back_but" class="normal_btn"
							style="" value="退回" />&nbsp;
						<input type="button" onClick="confirmAudit('3');" id="reject_but" name= "reject_but" class="normal_btn"
							style="" value="拒绝" />&nbsp;
						</c:if>
						<c:if test="${operType == '3' }">
							<input type="button" onClick="confirmAudit('4');" id="waste_but" name= "waste_but" class="normal_btn"
							style="" value="废弃" />&nbsp;
						</c:if>
						<c:if test="${operType != '4' }">
						<input type="button" onClick="goBack();" id="hist_but" name= "hist_but" class="normal_btn"
							style="" value="返回" />
						</c:if>
					</td>
<!-- 					<td colspan="2"> -->
<!-- 						<input class="long_btn" type="button" id="maintain_btn" value="保养状态判定" onclick="maintainStateSet();"/>&nbsp; -->
<!-- 						<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/> -->
<!-- 					</td> -->
				</tr>
			</table>
			<script type="text/javascript">
		
		//修改
		function confirmAudit(operType){
			if(!submitForm('fm')) {
				return false;
			}
			
			$("ttt").value = operType;
			if(operType == '1'){
				MyConfirm("是否审核？",auditPreAuthoriza);
			}else if(operType == '2'){
				MyConfirm("是否退回？",backPreAuthoriza);
			}else if(operType == '3'){
				MyConfirm("是否拒绝？",rejectPreAuthoriza);
			}else if(operType == '4'){
				MyConfirm("是否废弃？",wastePreAuthoriza);
			}
			
		}
			
		 //索赔单审核
		  function auditPreAuthoriza()
		  {
			 	$("audit_but").disabled = true;
			 	$("reject_but").disabled = true;
			 	$("back_but").disabled = true;
			 	$("hist_but").disabled = true;
				var url = "<%=contextPath%>/claim/preAuthorization/Authorization/auditPreAuthoriza.json";
				sendAjax(url,auditBack,'fm');
		  }
		  
		//索赔单退回
		  function backPreAuthoriza()
		  {
				$("audit_but").disabled = true;
			 	$("reject_but").disabled = true;
			 	$("back_but").disabled = true;
			 	$("hist_but").disabled = true;
				var url = "<%=contextPath%>/claim/preAuthorization/Authorization/backPreAuthoriza.json";
				sendAjax(url,auditBack,'fm');
		  }
		
		//索赔单拒绝
		  function rejectPreAuthoriza()
		  {
				$("audit_but").disabled = true;
			 	$("reject_but").disabled = true;
			 	$("back_but").disabled = true;
			 	$("hist_but").disabled = true;
				var url = "<%=contextPath%>/claim/preAuthorization/Authorization/rejectPreAuthoriza.json";
				sendAjax(url,auditBack,'fm');
		  }
		
		//索赔单废弃
		  function wastePreAuthoriza()
		  {
				$("waste_but").disabled = true;
				$("hist_but").disabled = true;
				var url = "<%=contextPath%>/claim/preAuthorization/Authorization/wastePreAuthoriza.json";
				sendAjax(url,auditBack,'fm');
		  }
		  
		  function auditBack(json){
			if(json.succ=="1"){
				parent.MyAlert(json.msg);
				goBack();
			}else{
				var tt = $("ttt").value;
				if(tt!='4'){
					$("audit_but").disabled = "";
				 	$("reject_but").disabled = "";
				 	$("back_but").disabled = "";
				}else{
					$("waste_but").disabled = "";
				}
				$("hist_but").disabled = "";
				MyAlert("提示："+json.msg);
			}
		}
			
	//背车费
     function addTrailer(){
         var addTable = document.getElementById("trailerMoney");
    		var rows = addTable.rows;
    		var length = rows.length;
    		var insertRow = addTable.insertRow(length);
    		insertRow.className = "table_list_row1";
    		insertRow.insertCell(0);
    		insertRow.insertCell(1);
       		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" name="trailerAmount" class="middle_txt"   id="trailerAmount" datatype="0,isMoney,10" maxlength="10" /></td>';
   			addTable.rows[length].cells[1].innerHTML =  '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="removeTR(this);"/></td>';
   	 }
	
   //增加补偿费		
     function addCompensation(){
         var addTable = document.getElementById("compensationMoney");
    		var rows = addTable.rows;
    		var length = rows.length;
    		var insertRow = addTable.insertRow(length);
    		insertRow.className = "table_list_row1";
    		insertRow.insertCell(0);
    		insertRow.insertCell(1);
    		insertRow.insertCell(2);
    		insertRow.insertCell(3);
       		addTable.rows[length].cells[0].innerHTML =  '<td><input type="text" name="applyAmount" class="middle_txt"   id="applyAmount" datatype="0,isMoney,10" maxlength="10" /></td>';
   			addTable.rows[length].cells[1].innerHTML =  '<td><input name="auditPrice" id="auditPrice"  type="text" class="middle_txt" datatype="0,isMoney,10" maxlength="10" /></td>';
   			addTable.rows[length].cells[2].innerHTML =  '<td><input name="applyRemark" id="applyRemark" value="" type="text" class="middle_txt"/></td>';
   			addTable.rows[length].cells[3].innerHTML =  '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="removeTR(this);"/></td>';
   	 }
     
   //删除行其他项目
		function removeTR(obj) {
		 	var s = obj.parentElement.parentElement.parentElement;
		 	s.removeChild(obj.parentElement.parentElement);
		 	
		 }
   
   		//清空VIN信息
   		function clearVinInfo(){
   			$("MODEL_NAME").value = '' ;
			$("PACKAGE_NAME").value = '' ;
			$("CAR_COLOR").value='';
			$("CAR_USE_DESC").value='';
			$("INVOICE_DATE").value='';
			$("ENGINE_NO").value='';
			$("vrLevel").value='';
			disabledButton(["chooseOrder"],true);
			$("isVin").value = "false";
			$("SERIES_ID").value = "";
			$("MODEL_ID").value = "";
			$("VILIDATE_VIN").value ="";
			$("RO_CREATE_DATE").value ="";
			clearPartInfo();
			
			var repairType = $("REPAIR_TYPE").value;
			if(repairType == <%=Constant.REPAIR_TYPE_09%>){
				document.getElementById("chooseMainPart").style.display = 'none';
  			}else{
  				document.getElementById("chooseMainPart").style.display = '';
  			}
   		}
   		
   		//选中维修类型
   		function doCusChange(obj){
   			var isVin = $("isVin").value;
  			if(obj == <%=Constant.REPAIR_TYPE_09%>){
  				if(isVin == "true"){
  					disabledButton(["chooseOrder"],false);
  				}else{
  					disabledButton(["chooseOrder"],true);
  				}
  				document.getElementById("chooseMainPart").style.display = 'none';
  			}else{
  				disabledButton(["chooseOrder"],true);
  				document.getElementById("chooseMainPart").style.display = '';
  			}
  			clearPartInfo();
   		}
   
		function blurBack (obj) {
			    if (obj=="vin") {
			    	oneVIN();
			    }
	    }
		
		//校验工单和行号在索赔单表中是否有重复
		function oneVIN() {
			var vin = document.getElementById("vin").value;
			
			var urlPdi = '<%=contextPath%>/claim/preAuthorization/Authorization/isPdi.json';
			var pattern=/^([A-Z]|[0-9]){17,17}$/;
			if (vin!=null&&vin!='') {
				if(pattern.exec(vin)) {
		    		makeCall(urlPdi,isPdiBack,{vin:vin});
		    	}else{
		    		clearVinInfo();
					MyAlert("输入的不是有效VIN格式！");
		    	}
			}else {
				clearVinInfo();
			}
		}
		
		//验证PDI回调函数
		function isPdiBack(json) {
			var vin = document.getElementById("vin").value;
			var url = '<%=contextPath%>/claim/preAuthorization/Authorization/getDetailByVin.json';
			if(json.succ == "1"){
				makeCall(url,oneVINBack,{vin:vin});
			}else{
				MyAlert(json.msg);
				clearVinInfo();
			}
		}
		
		//查询vin回调函数
		function oneVINBack(json) {
			if (json.records==null){
			MyAlert("未找到该车辆信息!");
				$("vin").value="";
				$("vin").focus();
				clearVinInfo();
				
			}else {
				var record = json.records;
	    		$("vin").value =getNull(record.VIN) ;
	    		$("MODEL_NAME").value = getNull(record.MODEL_NAME) ;
	    		$("PACKAGE_NAME").value = getNull(record.PACKAGE_NAME) ;
	    		$("CAR_COLOR").value=getNull(record.COLOR);
	    		$("CAR_USE_DESC").value=getNull(record.CAR_USE_DESC);
	    		$("INVOICE_DATE").value=getNull(record.INVOICE_DATE)==""?"":record.INVOICE_DATE.substring(0,10);
				$("ENGINE_NO").value=getNull(record.ENGINE_NO);
				$("vrLevel").value=getNull(json.vrLevel);
				$("SERIES_ID").value = getNull(record.SERIES_ID);
				$("MODEL_ID").value = getNull(record.MODEL_ID);
				$("MILEAGE").value = getNull(record.MILEAGE);
				//如果遇到变换VIN则要清空配件表
				if($("VILIDATE_VIN").value =="" || $("VILIDATE_VIN").value != getNull(record.VIN)){
					clearPartInfo();
				}
				$("VILIDATE_VIN").value = getNull(record.VIN);
				  
				$("isVin").value = "true";
				 var repairType = $("REPAIR_TYPE").value;
				if(repairType == <%=Constant.REPAIR_TYPE_09%>){
					disabledButton(["chooseOrder"],false);
					document.getElementById("chooseMainPart").style.display = 'none';
	  			}else{
	  				disabledButton(["chooseOrder"],true);
	  				document.getElementById("chooseMainPart").style.display = '';
	  			}
	    }
	 }
		
		function getNull(data) {
			if (data==null) {
				return '';
			}else {
				return data;
			}
		}
		
		//返回
		function goBack(){
			history.back();
		}
		
		//关联工单
		function selectOrder() {
			var vin = $("vin").value;
			var repairType = $("REPAIR_TYPE").value;
			OpenHtmlWindow('<%=contextPath%>/claim/preAuthorization/Authorization/authoriChooseOrdInit.do?vin='+vin+'&repairType='+repairType,800,500);
		}
		
		//清空配件信息
		function clearPartInfo(){
			$("RO_NO").value = "";
			$("PART_NO").value = "";
			$("PART_NAME").value = "";
			$("PART_ID").value = "";
			$("MAKER_CODE").value = "";
			$("MAKER_SHOTNAME").value = "";
		}
		//选择关联单
		function showOrder(obj){
			var orderData = obj.split("_");
			$("RO_NO").value = orderData[0];
			$("PART_NO").value = orderData[1];
			$("PART_NAME").value = orderData[2];
			$("IS_RETURN").value = orderData[3];
			$("PART_ID").value = orderData[4];
			$("RO_CREATE_DATE").value = orderData[5];
		}
		
		//弹出选择新增配件页面
		function addPart(){
			var isVin=$("isVin").value;
			if("false"==isVin){
				MyAlert("提示：请先输入正确VIN");
				return;
			}
			var series_id=$("series_id").value;
			var model_id=$("model_id").value;
			OpenHtmlWindow('<%=contextPath%>/OrderAction/addPart.do?model_id='+model_id+'&series_id='+series_id,850,550);
		}
		
		//选择配件
		function setMainPartCode(part_id,part_code,part_name,claim_price_param,val){
			$("PART_NO").value = part_code;
			$("PART_NAME").value = part_name;
			$("PART_ID").value = part_id;
			$("MAKER_CODE").value = "";
			$("MAKER_SHOTNAME").value = "";
		}
		
		//弹出选择经销商页面
		function choose_producer_code(){
			var part_code = $("PART_NO").value;
			if(part_code == "" || part_code == null){
				MyAlert("请先选择配件");
				return;
			}
			OpenHtmlWindow('<%=contextPath%>/ClaimAction/supplierCodeByPartCode.do?partcode='+part_code,800,500);
		}
		
		//选择供应商
		function setMainSupplierCode(maker_code,maker_shotname){
			$("MAKER_CODE").value = maker_code;
			$("MAKER_SHOTNAME").value = maker_shotname;
		}
		
   
    </script>
			<!-- 资料显示区结束 -->

		</form>
	</body>
</html>
