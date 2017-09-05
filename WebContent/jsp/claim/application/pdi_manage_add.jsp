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

<title>PDI索赔单管理</title>
<script type="text/javascript">
$(function(){
	var type=$("#type").val();
	$("#noBalance,#balance").hide();
	if("add"==type){
		$("#claim_no").val(" 新增保存后自动生成 ");
		$("#remark").val("整备完好");
		$("#vin").live("keyup",function(){
			var vin=$("#vin").val();
			if($.trim(vin).length>=8){
				sendAjax('<%=contextPath%>/OrderAction/findVinListByVin.json?vin='+vin,backVinDataByVin,'fm');
			}
		});
	}
	if("update"==type){
		$("#showOrHide").remove();
		$("#vin").attr("readonly","readonly");
		if($("#IS_AGREE").val()=="0"){
			$("#remark").removeAttr("readonly","");
		}
	}
	if("view"==type){
		$("#showOrHide").remove();
		$("#vin").attr("readonly","readonly");
		$("input[type='radio']").attr("disabled","disabled");
		$("#report,#sure").hide();
		$(".normal_btn").each(function(){
			 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
				 $(this).hide();
			} 
		});
	}
});
function backByVin(json){
	if(json.flag==true){
		var t=json.info;
		if(null==t){
			$("#color").val("");
			$("#engine_no").val("");
			$("#package_name").val("");
			$("#model_name").val("");
			$("#model_id").val("");
		}else{
			$("#color").val(t.COLOR);
			$("#engine_no").val(t.ENGINE_NO);
			$("#package_name").val(t.PACKAGE_NAME);
			$("#model_name").val(t.MODEL_NAME);
			$("#model_id").val(t.MODEL_ID);
		}
	}
}
	function checkYes(){
		$("#remark").val("整备完好");
		$("#remark").attr("readonly","readonly");
	} 
	function checkNo(){
		$("#remark").val("");
		$("#remark").removeAttr("readonly","");
	}
	function sureInsert(identify){
		var temp=0;
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
		var is_agree=$("input[name='is_agree']");
		if(is_agree.length>0){
			$(is_agree).each(function(){
				if($(this).val()!="0" && ""==$("#remark").val()){
					MyAlert("提示：PDI选择为否时必须输入备注！");
					temp++;
					return;
				}
			});
		}else{
			MyAlert("提示：请选择整配完好状态！");
			temp++;
			return;
		}
		if(temp==0){
			var vin=$("#vin").val();
			var url="<%=contextPath%>/ClaimAction/pdiAddCheck.json?vin="+vin;
			sendAjax(url,function(json){
				if(json.res==""){
					if(0==identify){
						MyConfirm("是否确认保存？",sureInsertCommit,[0]);
					}
					if(1==identify){
						MyConfirm("是否确认上报？",sureInsertCommit,[1]);
					}
				}else{
					MyAlert(json.res);
					return;
				}
			},'fm');
			
		}
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		$("#report").attr("disabled",true);
		var url="<%=contextPath%>/ClaimAction/pdiAddSure.json?identify="+identify;
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
			var url='<%=contextPath%>/ClaimAction/pdiManageList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#sure").attr("disabled",false);
			$("#report").attr("disabled",false);
		}
	}
	function backVinDataByVin(json){
		$("#divTemp").css('display','block'); 
		$("#vinDiv").empty();
		if(json.vinData!=null){
			for(var i=0;i<=json.vinData.length;i++){
				var vinDivInner="<tr><td onclick='checkThisVin(this);'>"+json.vinData[i].VIN+"</td></tr>";
				$("#vinDiv").append(vinDivInner);
			}
		}
	}
	function checkThisVin(obj){
		$("#vin").val($(obj).text());
		var vin=$("#vin").val();
		if($.trim(vin).length>=12){
			sendAjax('<%=contextPath%>/OrderAction/showInfoByVin.json?vin='+vin,backByVin,'fm');
		}
		$("#divTemp").css('display','none'); 
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;PDI索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="netitemId" value="${t.NETITEM_ID }" name="netitemId" type="hidden"  />
<input class="middle_txt" id="IS_AGREE" value="${t.IS_AGREE }"  type="hidden"  />

<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >PDI单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="claim_no"  value="${t.CLAIM_NO }" readonly="readonly" name="claim_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="vin" value="${t.VIN }"  name="vin" type="text" maxlength="30" />
    		<div id="divTemp" style="position:absolute; left:500px; top:80px; width:155px; height:200px;display: none">
    			<table id='vinDiv'  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;color:green;">
    		</table>
    		</div>
		<td nowrap="true" width="10%" >车型：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="model_name" value="${t.MODEL_NAME }" readonly="readonly" name="model_name" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="package_name" value="${t.APP_PACKAGE_NAME  }" readonly="readonly" name="package_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="engine_no" value="${ t.ENGINE_NO }" readonly="readonly" name="engine_no" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="color" value="${t.APP_COLOR  }" readonly="readonly" name="color" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br/>
 <div id="new_acc_add"  style="text-align: center; width: 100%;">
 			<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
				<th colspan="8">
					<img class="nav" src="<%=contextPath%>/img/subNav.gif" />PDI
				</th>
				<tr>
					<td nowrap="true" width="20%" >项目名称</td>
					<td nowrap="true" width="20%" >金额(元)</td>
					<td nowrap="true" width="20%" >整备是否完好</td>
					<td nowrap="true" width="40%" ><span style="color: red;">备注</span></td>
				</tr>
   				<tr>
   				  <td nowrap="true" width="20%" >
						<input readonly="readonly" name="pdi" class="middle_txt" value="QT011"/>
					</td><td nowrap="true" width="20%" >
						<input readonly="readonly" name="amount" class="middle_txt" value="30.0"/>
					</td>
					<td nowrap="true" width="20%" >
						<div id="showOrHide">
								<input type="radio" name="is_agree" onclick="checkYes();" checked="checked" value="1"/>是
								&nbsp;&nbsp;&nbsp;
								<input type="radio" name="is_agree" onclick="checkNo();" value="0"/>否
							</div>
						<c:if test="${t.IS_AGREE==1 }">
							<input type="radio" name="is_agree" onclick="checkYes();" checked="checked" value="1"/>是
							&nbsp;&nbsp;&nbsp;
							<input type="radio" name="is_agree" onclick="checkNo();" value="0"/>否
						</c:if>
						<c:if test="${t.IS_AGREE==0}">
							<input type="radio" name="is_agree" onclick="checkYes();"  value="1"/>是
							&nbsp;&nbsp;&nbsp;
							<input type="radio" name="is_agree" onclick="checkNo();" checked="checked" value="0"/>否
						</c:if>
							
					</td>
					<td nowrap="true" width="40%" >
						<input  name="remark" id="remark" value="${t.REMARK }" readonly="readonly"   maxlength="100" size="65"/>
					</td>
				</tr>
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
				    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
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