<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<title>保养索赔单管理</title>
<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#claim_no").val("   新增后自动生成 ");
			$("#ro_no").val("   请点击选择 ");
			$("#ro_no").bind("click",function(){
				OpenHtmlWindow('<%=contextPath%>/ClaimAction/findRoKeepFit.do',800,500);
			});
		}
		if("update"==type){
			
		}
		if("view"==type){
			$("input[type='text']").attr("readonly","readonly");
			$("#report,#sure").hide();
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
		}
	});
	function backByVin(json){
		var t=json.info;
		if(null==t){
			$("#color").val("");
			$("#engine_no").val("");
			$("#package_name").val("");
			$("#model_name").val("");
			$("#model_id").val("");
			$("#guarantee_date").val("");
		}else{
			$("#color").val(t.COLOR);
			$("#engine_no").val(t.ENGINE_NO);
			$("#package_name").val(t.PACKAGE_NAME);
			$("#model_name").val(t.MODEL_NAME);
			$("#model_id").val(t.MODEL_ID);
			$("#guarantee_date").val(t.PURCHASED_DATE_ACT.substr(0,16));
		}
	}
	function sureInsert(identify){
		var temp=0;
		if(""==$("#ro_no").val()){
			MyAlert("提示：请先输入工单号带出信息再提交！");
			temp++;
			return;
		}
		if(""==$("#vin").val()){
			MyAlert("提示：请先输入vin带出信息再提交！");
			temp++;
			return;
		}
		if(""==$("#model_name").val()){
			MyAlert("提示：车型为空，请先输入vin带出信息再提交！");
			temp++;
			return;
		}
		if(""==$("#color").val()){
			MyAlert("提示：颜色为空，请先输入vin带出信息再提交！");
			temp++;
			return;
		}
		if(""==$("#package_name").val()){
			MyAlert("提示：配置为空，请先输入vin带出信息再提交！");
			temp++;
			return;
		}
		if(temp==0){
			if(0==identify){
				MyConfirm("是否确认保存？",sureInsertCommit,[0]);
			}
			if(1==identify){
				MyConfirm("是否确认上报？",sureInsertCommit,[1]);
			}
		}
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		$("#report").attr("disabled",true);
		var url="<%=contextPath%>/ClaimAction/keepFitAddSure.json?identify="+identify;
		makeNomalFormCall1(url,sureInsertCommitBack,"fm");
	}
	function sureInsertCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="保存";
		}
		if(json.identify=="1"){
			str+="上报";
		}
		if(json.succ=="1"){
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/ClaimAction/keepFitManageList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#sure").attr("disabled",false);
			$("#report").attr("disabled",false);
		}
	}
	function ro_type_count(){
		var count_part=0.0;
		var part_amont_2=$("input[name='part_amont_2']");
		if(part_amont_2.length>0 ){
			$(part_amont_2).each(
				function(){
					count_part+=parseFloat($(this).val());
				});
		}
		
		var count_labour=0.0;
		var labour_fix_2=$("input[name='labour_fix_2']");
		if(labour_fix_2.length>0 ){
			$(labour_fix_2).each(
				function(){
					count_labour+=parseFloat($(this).val());
				});
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#count_span").text(count_all.toFixed(2));
	}
	
	function backKeepFitData(labours2,parts2,ro_no,vin){
		$("#vin").val(vin);
		sendAjax('<%=contextPath%>/ClaimAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
		$("#ro_no").val(ro_no);
		$(".deleteTr").remove();
		var tab=$("#tab_2");
		var str="";
		for(var i = 0;i < parts2.length;i++){
			str+='<tr class="deleteTr">';
			str+='<td nowrap="true" width="10%" >配件>></td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_id_2" type="hidden" class="middle_txt" value="'+parts2[i].REAL_PART_ID+'"/>';
			str+='<input name="part_code_2" readonly="readonly"  value="'+parts2[i].PART_NO+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="15%" >';
			str+='<input name="part_name_2" readonly="readonly"value="'+parts2[i].PART_NAME+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="part_quotiety_2"  size="10"  value="'+parts2[i].PART_QUANTITY+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input name="claim_price_param_2" readonly="readonly" size="10" value="'+parts2[i].PART_COST_PRICE+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" >';
			str+='<input readonly="readonly" name="part_amont_2"  size="10" value="'+parts2[i].PART_COST_AMOUNT+'"/>';
			str+='</td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="pay_type_2">';
			if("11801002"==parts2[i].PAY_TYPE){
				str+='<option value="11801002">索赔</option>';
			}else{
				str+='<option value="11801001">自费</option>';
			}
			str+='</select></td>';
			str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type_2">';
			if("95431002"==parts2[i].PART_USE_TYPE){
				str+='<option value="95431002">更换</option>';
			}else{
				str+='<option value="95431001">维修</option>';
			}
			str+='</select></td>';
			str+='</tr>';
		}
		tab.append(str);
		var tab1=$("#tab_2_1");
		var str1="";
		for(var j = 0;j < labours2.length;j++){
			str1+='<tr class="deleteTr">';
			str1+='<td nowrap="true" width="10%" >工时>></td>';
			str1+='<td nowrap="true" width="15%" >';
			str1+='<input readonly="readonly" name="labour_code_2"  value="'+labours2[j].LABOUR_CODE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="25%" >';
			str1+='<input name="cn_des_2" readonly="readonly" size="35" value="'+labours2[j].LABOUR_NAME+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_quotiety_2" readonly="readonly" size="10" value="'+labours2[j].STD_LABOUR_HOUR+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="parameter_value_2" size="10" readonly="readonly" value="'+labours2[j].LABOUR_PRICE+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%" >';
			str1+='<input name="labour_fix_2" readonly="readonly" size="10" value="'+labours2[j].LABOUR_AMOUNT+'"/>';
			str1+='</td>';
			str1+='<td nowrap="true" width="10%"  >';
			str1+='<select class="min_sel" name="pay_type_labour_2">';
			if("11801002"==labours2[j].PAY_TYPE){
				str1+='  <option value="11801002">索赔</option>';
			}else{
				str1+=' <option value="11801001">自费</option>';
			}
			str1+='</select>';
			str1+='</td>';
			str1+='</tr>';
		}
		tab1.append(str1);
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;保养索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />

<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="ro_no"  value="${t.RO_NO }" readonly="readonly" name="ro_no" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >保养单号：</td>
    	<td  width="15%" >
    		<input class="middle_txt" id="claim_no"   value="${t.CLAIM_NO }" readonly="readonly" name="claim_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" readonly="readonly" id="vin" value="${t.VIN }"  name="vin" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="model_name" value="${t.MODEL_NAME }" readonly="readonly" name="model_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="package_name" value="${t.APP_PACKAGE_NAME  }" readonly="readonly" name="package_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="color" value="${t.APP_COLOR  }" readonly="readonly" name="color" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="guarantee_date" value="<fmt:formatDate value="${t.GUARANTEE_DATE}" pattern="yyyy-MM-dd HH:mm"/>"readonly="readonly" name="guarantee_date" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="engine_no" value="${ t.ENGINE_NO }" readonly="readonly" name="engine_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ></td>
    	<td nowrap="true" width="15%" >
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br>
			<div style="text-align: center;">
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.BALANCE_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]
    		</div>
    		<br>
    		<div id="new_add">
	    		<table border="1" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />免费保养 &nbsp;&nbsp;
					</th>
					<tr>
					<td nowrap="true" width="10%" >维修配件</td>
					<td nowrap="true" width="15%" >新件代码</td>
					<td nowrap="true" width="15%" >新件名称</td>
					<td nowrap="true" width="10%" >新件数量</td>
					<td nowrap="true" width="10%" >单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					<td nowrap="true" width="10%" >维修方式</td>
					</tr>
	    			 <c:forEach items="${parts2 }" var="p2" varStatus="status">
						<tr class="deleteTr">
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="15%" >
						<input name="part_id_2" type="hidden"   value="${p2.REAL_PART_ID }"/>
						<input name="part_code_2" readonly="readonly"  value="${p2.PART_CODE }"/>
						</td>
						<td nowrap="true" width="15%" >
						<input name="part_name_2" readonly="readonly"  value="${p2.PART_NAME }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quotiety_2"  size="10" value="${p2.QUANTITY }"/>
						</td><td nowrap="true" width="10%" >
						<input name="claim_price_param_2" readonly="readonly" size="10" value="${p2.PRICE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input readonly="readonly" name="part_amont_2"  size="10" value="${p2.AMOUNT }"/>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" name="pay_type_2">
						<c:if test="${p2.PAY_TYPE==11801002}">
							<option value="11801002" selected="selected">索赔</option>
						</c:if>
						<c:if test="${p2.PAY_TYPE==11801001}">
							<option value="11801001" selected="selected">自费</option>
						</c:if>
						</select>
						</td>
						<td  width="10%" nowrap="true">
						<select class="min_sel" name="part_use_type_2">
						<c:if test="${p2.PART_USE_TYPE==1}">
							<option value="95431002" selected="selected">更换</option>
						</c:if>
						<c:if test="${p2.PART_USE_TYPE==0}">
							<option value="95431001" selected="selected">维修</option>
						</c:if>
						</select>
						</td>
	    			 </c:forEach>
	    		</table>
    			<table border="1" id="tab_2_1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="15%" >作业代码</td>
					<td nowrap="true" width="25%" >作业名称</td>
					<td nowrap="true" width="10%" >工时定额</td>
					<td nowrap="true" width="10%" >工时单价</td>
					<td nowrap="true" width="10%" >金额（元）</td>
					<td nowrap="true" width="10%" >付费方式</td>
					</tr>
	    			 <c:forEach items="${labours2 }" var="l2" varStatus="status">
			           <tr class="deleteTr">
			           <td nowrap="true" width="10%" >${status.index+1}</td>
			           <td nowrap="true" width="15%" >
			           <input readonly="readonly" name="labour_code_2"  value="${l2.LABOUR_CODE}"/>
			           </td>
			           <td nowrap="true" width="25%" >
			           <input name="cn_des_2" readonly="readonly"  size="35" value="${l2.LABOUR_NAME }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="labour_quotiety_2" readonly="readonly"  size="10" value="${l2.LABOUR_HOURS }"/>
			           </td>
			           <td nowrap="true" width="10%" >
			           <input name="parameter_value_2"  size="10" readonly="readonly" value="${l2.LABOUR_PRICE }"/>
			           </td><td nowrap="true" width="10%" >
			           <input name="labour_fix_2" readonly="readonly"  size="10" value="${l2.LABOUR_AMOUNT }"/>
			           <td nowrap="true" width="10%"  >
			           <select class="min_sel" name="pay_type_labour_2" >
			           <c:if test="${l2.PAY_TYPE==11801002}">
							<option value="11801002" selected="selected">索赔</option>
						</c:if>
						<c:if test="${l2.PAY_TYPE==11801001}">
							<option value="11801001" selected="selected">自费</option>
						</c:if>
			           </select>
			           </td>
			           </tr>
	    			 </c:forEach>
	    		</table>
	    		<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
       <tr>
        <th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>审核备注
		</th>
	  </tr>	
		<tr>
			<td height="12" align=left width="33%" colspan='2'>
				<textarea name='AUDIT_REMARK' readonly="readonly"	 id='AUDIT_REMARK'	 maxlength="100" rows='2' cols='120'>${t.AUDIT_REMARK }</textarea>
			</td>
		</tr>
</table>
    		</div>
<br>
<br/>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
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
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="上报" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="_hide();" class="normal_btn"  style="width=8%" value="关闭"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>