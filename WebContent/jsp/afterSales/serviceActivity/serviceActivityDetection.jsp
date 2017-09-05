<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/common.js"></script>
<title>服务活动</title>
<script type="text/javascript">
	//选择车型组
	function showLabor(){
		var url = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListInit2.do' ;
		OpenHtmlWindow(url,600,400);
	}
	
	function setLaborList(codes,Wcodes){
		 var scode="";
		 var wcode="";
			 for(var i=0;i<codes.length;i++){
				 scode+=codes[i]+",";
				 wcode += Wcodes[i]+",";
			 }
		 	$('#WRGROUP_ID')[0].value = scode.substring(0,scode.length-1);
		 	$('#WRGROUP_CODE')[0].value = wcode.substring(0,wcode.length-1);
		}

	function cleanInput(){
		$('#WRGROUP_ID')[0].value="";
		$('#WRGROUP_CODE')[0].value='';
	}
	
	function setDealerValue(inputId,inputName,id,name){
		document.getElementById(inputId).value=id;
	    document.getElementById(inputName).value=name;
	}

	function setMoreDealerValue(inputId,inputName,userIdsNames,type){
	    //定义一数组
	    var strsTemp = userIdsNames.split("@@"); //字符分割
	    var id = strsTemp[1];
	    var name = strsTemp[0];
	    var code = strsTemp[2];
		if(type == 1){
			var values = document.getElementById(inputId).value;
			values += id + ",";
			document.getElementById(inputId).value = values;//重新赋值
			$("#tr").after('<tr id="tr'+id+'" style="BACKGROUND-COLOR: #fdfdfd" class="table_list_row1">'
					        +'<td align="center" colspan="1" nowrap="nowrap" class = "dealerNum"></td>'
					        +'<td align="center" colspan="2" nowrap="nowrap">'+ code + '</td>'
					        +'<td align="center" colspan="2" nowrap="nowrap">'+ name + '</td>'
					        +'<td align="center" colspan="1" nowrap="nowrap"><input onclick="deleteRow('+id+',\''+inputId+'\');" value="删除" type="button" class="normal_btn"/></td></tr>');
			//设置行号
			setDealerNum();
		}else{
			deleteRow(id,inputId);
		}
	}
		//设置行号
		function setDealerNum(){
			var dealerNum = document.getElementsByClassName("dealerNum");//模板行号
			for (var i = 0 ;i < dealerNum.length ; i++) {
				dealerNum[i].innerHTML = i+1;
			}
		}

		//经销商删除行并且重新赋值
		function deleteRow(id,inputId){
			var values = document.getElementById(inputId).value;
			values = values.replace(id+",","");
			document.getElementById(inputId).value = values;//重新赋值
			//删除行
			$("#tr"+id).remove();
			//设置行号
			setDealerNum();
		}
		//新增经销商
		function addDealer(inputId,inputCode,isMulti){
			var  idVal=document.getElementById(inputId).value;
			var url="<%=contextPath%>/jsp/afterSales/serviceActivity/showDealer.jsp?idVal="+idVal+"&INPUTCODE="+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti;
			OpenHtmlWindow(url,800,600);
		}
		
		var menuUrl = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/serviceActivityPageInit.do";
		var saveUrl = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/saveData.json";
		// 保存信息反馈类型
		function saveData(status) {
			if($("#activity_strate_date").val()!=null && $("#activity_strate_date").val()!='' && $("#activity_end_date").val()!=''  && $("#activity_end_date").val()!=null ){
				var beginDate=$("#activity_strate_date").val();  
				var endDate=$("#activity_end_date").val();  
				var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
				var d2 = new Date(endDate.replace(/\-/g, "\/"));  
				var dd=new Date();
				if(d1 > d2){
					MyAlert("活动开始日期不能大于活动结束日期！");
					return;
				}
				if(d2<dd){
					MyAlert("活动结束日期不能小于当前日期！");
					return;
				}
			}
			
			if($("#activity_strate_mileage").val()!=null && $("#activity_strate_mileage").val()!="" && $("#activity_end_mileage").val()!=null && $("#activity_end_mileage").val()!=""){
				
				if(parseFloat($("#activity_strate_mileage").val()) > parseFloat($("#activity_end_mileage").val())){
					MyAlert("开始里程数不能大于结束里程数！");
					return;
				}
			}
			if($("#activity_sales_strate_date").val()!=null && $("#activity_sales_strate_date").val()!='' && ($("#activity_sales_end_date").val()==''  || $("#activity_sales_end_date").val()==null) ){
				MyAlert("结束实销日期不能为空！");
				return;
			}
			if(($("#activity_sales_strate_date").val()==null || $("#activity_sales_strate_date").val()=='' )&& $("#activity_sales_end_date").val()!=''  && $("#activity_sales_end_date").val()!=null ){
				MyAlert("开始实销日期不能为空！");
				return;
			}
			
			if($("#activity_sales_strate_date").val()!=null && $("#activity_sales_strate_date").val()!='' && $("#activity_sales_end_date").val()!=''  && $("#activity_sales_end_date").val()!=null ){
				var beginDate=$("#activity_sales_strate_date").val();  
				var endDate=$("#activity_sales_end_date").val();  
				var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
				var d2 = new Date(endDate.replace(/\-/g, "\/"));  
				if(d1 > d2){
					MyAlert("开始实销日期不能大于结束实销日期！");
					return;
				}
			}
			if($("#activity_name").val()=='' || $("#activity_name").val()==null){
				MyAlert("活动名称不能为空！");
				return;
			}
			if($("#activity_code").val()=='' || $("#activity_code").val()==null){
				MyAlert("活动编号不能为空！");
				return;
			}
			if($("#activity_strate_date").val()=='' || $("#activity_strate_date").val()==null){
				MyAlert("活动开始日期不能为空！");
				return;
			}
			if($("#activity_end_date").val()=='' || $("#activity_end_date").val()==null){
				MyAlert("活动结束日期不能为空！");
				return;
			}
			if($("#activity_discount").val()=='' || $("#activity_discount").val()==null){
				MyAlert("折扣率不能为空！");
				return;
			}
			var reg=/^(?:\d?\d|100)$/;
			if($("#activity_discount").val()=='' || $("#activity_discount").val()==null){
				MyAlert("折扣率不能为空！");
				return;
			}else if(!reg.test($("#activity_discount").val())||$("#activity_discount").val()==0){
				MyAlert("请输入正确的折扣率!");
				document.getElementById("activity_discount").value="";
				return;
			}
			var reg2 = /(^[1-9]{1,4}?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;  
			if($("#detection_money").val()=='' || $("#detection_money").val()==null){
				MyAlert("检测金额不能为空！");
				return;
			}else if(!reg2.test($("#detection_money").val())){
				MyAlert("请输入正确的检测金额!");
				document.getElementById("detection_money").value="";
				return;
			}
			
			if(status==1){
				$("#activity_status").val("96291001");//尚未发布
				 MyConfirm("确定保存吗？",EditAsDo);
			}else{
				$("#activity_status").val("96291002");//已经发布
				 MyConfirm("确定发布吗？",EditAsDo);
			}
			
		}
		function EditAsDo(){
			var tUrl = saveUrl;
	 		document.getElementById("saveBtn1").disabled=true;
	 		document.getElementById("saveBtn2").disabled=true;
			sendAjax(tUrl, showResult, 'fm');
		}
		function showResult(json){
			if(json.message == "1"){
				MyAlert("保存成功！");
				backInit();
			}else if(json.message == "2"){
				MyAlert("发布成功！");
				backInit();
			}else {
				document.getElementById("saveBtn1").disabled=false;
		 		document.getElementById("saveBtn2").disabled=false;
				MyAlert(json.message);
			}
		}

		function backInit(){
			window.location.href = menuUrl;
		}
		//导入VIN
		function checkVin(status){
			var  activityId=document.getElementById("activityId").value;
			var url="<%=contextPath%>/jsp/afterSales/serviceActivity/checkVin.jsp?activityId="+activityId+"&status="+status;
			OpenHtmlWindow(url,730,390);
		}
	</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：售后服务&gt;维修管理&gt;服务活动管理送检测（<span style="color:red;">如果导入了VIN和里程、车型、实销日期同时做了录入，以导入VIN为准</span>）</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
	<input type="hidden" name="activityId" id="activityId" value="${info.ACTIVITY_ID }"/>
		<input type="hidden" name="updateId" value="${update}"/>
		<input type="hidden" name="activity_type" id="activity_type" value="<%=Constant.SERVICEACTIVITY_TYPE_NEW_03%>"/>
		<input type="hidden" name="activity_status" id="activity_status"/>
		<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>服务活动信息</h2>
			<div class="form-body">
<table  class="table_query">
	<tr >
		<td class="right">活动名称：</td>
		<td  nowrap="nowrap">
			<input class="middle_txt" id="activity_name"  name="activity_name" type="text" value="${info.ACTIVITY_NAME }"  />
		  <span style="color:red;">*</span>
		</td>
		
		<td class="right">活动编号：</td>
		<td  nowrap="nowrap">
			<input class="middle_txt" id="activity_code"  name="activity_code" type="text"  value="${info.ACTIVITY_CODE }" />
		  <span style="color:red;">*</span>
		  </td>
		<td class="right">活动日期：</td>
		<td  nowrap="nowrap">
			<input id="activity_strate_date" name="activity_strate_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_STRATE_DATE }"/> 至
			<input id="activity_end_date" name="activity_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_END_DATE }"/>
		  <span style="color:red;">*</span>
		</td>
	</tr>
	<tr >
		<td class="right">里程设置：</td>
		<td  nowrap="nowrap">
			<input class="middle_txt" style="width: 64px" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" maxlength="8" id="activity_strate_mileage"  name="activity_strate_mileage" type="text" value="${info.ACTIVITY_STRATE_MILEAGE }"  />至
		  	<input class="middle_txt" style="width: 64px" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" maxlength="8" id="activity_end_mileage"  name="activity_end_mileage" type="text" value="${info.ACTIVITY_END_MILEAGE }"  />
		</td>
		 <td class="table_query_2Col_label_6Letter">车型组选择：</td>
            <td >
            <input type="text" name="WRGROUP_CODE"  id ="WRGROUP_CODE" class="middle_txt" readonly="readonly" onmouseover="this.title=this.value" value="${modelCode}" onclick="showLabor();"/>
            <input type="hidden"  name="MODEL_ID11"  id ="WRGROUP_ID" class="middle_txt" value="${modelId }"/>
         	<!-- <input type="button" value="..." class="normal_btn" onclick="showLabor();"/> -->
         	<input type="button" value="清空" class="normal_btn" onclick="cleanInput();"/>
            </td>
		<td class="right">实销日期：</td>
		<td nowrap="nowrap">
			<input id="activity_sales_strate_date" name="activity_sales_strate_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_SALES_STRATE_DATE }"/>
			至
		  	<input id="activity_sales_end_date" name="activity_sales_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.ACTIVITY_SALES_END_DATE }"/>
			<span style="color:red;">*</span>
		</td>
	</tr>
	<tr >
		<td class="right">折扣率：</td>
		<td  >
			<input class="middle_txt" style="width: 40px" id="activity_discount"  name="activity_discount" type="text"  value="${info.ACTIVITY_DISCOUNT }" maxlength="3" />%
		<span style="color:red;">*</span>
		</td>
		<td class="right">检测金额：</td>
		<td  >
			<input class="middle_txt" style="width: 64px" id="detection_money"  name="detection_money" type="text"  value="${info.DETECTION_MONEY }" maxlength="8" />
		<span style="color:red;">*</span>
		</td>
	</tr>
	</table>
	</div>
	</div>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/img/nav.gif"/>附件信息&nbsp;&nbsp;&nbsp;<a class="u-anchor" href="#" onclick="showUpload('<%=contextPath%>','PNG;PDF;JPG;JPEG;BMP;RAR;ZIP;TXT;XLS;XLSX;DOC;DOCX',10)" />添加附件</a> </h2>
			<div class="form-body">
<table class="table_query"  id="file">
			<tr>
				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
			</tr>
			<c:choose>
				<c:when test="${empty select}">
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
	    			</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach items="${fsList}" var="flist">
			    		<script type="text/javascript">
			    			addUploadRowByDb('${flist.filename}','${flist.fjid}','${flist.fileurl}','${flist.fjid}','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
						</script>
			    	</c:forEach>
				</c:otherwise>
			</c:choose>
	    	
	 	</table>
  </div>
  </div>
  <div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>下发经销商&nbsp;&nbsp;&nbsp;
		<input type="hidden" name="dealer_id" id="dealer_id" value="${dealerId }"/>
		<a class="u-anchor" href="#" name="button_add"  id="button_add" onclick="addDealer('dealer_id','dealer_name','true');"/>新增</a>
		</h2>
			<div class="form-body">
	<table class="table_list" id="REMARKS_ID" > 
     <tr id="tr" >
        <td class="center" colspan="1" nowrap="nowrap">行号</td>
        <td class="center" colspan="2" nowrap="nowrap">经销商代码</td>
        <td class="center" colspan="2"  nowrap="nowrap">经销商名称</td>
        <td class="center" colspan="1"  nowrap="nowrap">操作</td>
      </tr>
   
      <c:forEach items="${activityDealer }" var="list" varStatus="st">
	      <tr id="tr${list.DEALER_ID }"  class="right">
      		<td class="center" colspan="1" nowrap="nowrap" class = "dealerNum">${st.index + 1}</td>
        	<td class="center" colspan="2" nowrap="nowrap">${list.DEALER_CODE }</td>
        	<td class="center" colspan="2" nowrap="nowrap">${list.DEALER_NAME }</td>
        	<td class="center" colspan="1" nowrap="nowrap">
        		<input onclick="deleteRow(${list.DEALER_ID },'dealer_id');" value="删除" type="button" class="normal_btn"/>
        	</td>
	      </tr>
      </c:forEach>
      </table>
      </div>
      </div>
      <div class="form-panel">
		<h2>
		<img src="<%=contextPath%>/img/nav.gif"/>导入及查看VIN&nbsp;&nbsp;<a class="u-anchor" href="#"  onclick="checkVin(1);">导入</a>
		</h2>
			<div class="form-body">
      <table class="table_query" >
     <tr>
        <td>查看已导入车辆信息，请点击"导入"按钮查询。</td>
     </tr>
     </table>
    </div>
     </div>
	<div style="text-align: center;">
		<input class="u-button u-submit" type="button" name="button1"  id="saveBtn1" value="保存" onclick="saveData(1);"/>
		<input class="u-button u-cancel" type="button" name="button1"  id="saveBtn2" value="发布" onclick="saveData();"/>
		<input class="u-button u-query" type="button" name="button1"  id="backBtn" value="返回" onclick="backInit();"/>
	</div>
	</form>
	</div>
</body>
</html>