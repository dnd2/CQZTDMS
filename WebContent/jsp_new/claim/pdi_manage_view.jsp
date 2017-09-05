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
		$("#vin").live("blur",function(){
			var vin=$("#vin").val();
			if(vin!=""){
				sendAjax('<%=contextPath%>/ClaimAction/checkshowInfoByVin.json?vin='+vin,backByVin,'fm');
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
		$("textarea").attr("readonly","readonly");
		$("input[type='text']").attr("readonly","readonly");
		$("input[type='radio']").attr("disabled","disabled");
		$("#report,#sure,#sureUndo,#surePass,#sureUnpass").hide();
		$(".normal_btn").each(function(){
			 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
				 $(this).hide();
			} 
		});
	}
});
function backByVin(json){
	if(""!=json.res){
		MyAlert(json.res);
		return;
	}
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
	function checkYes(){
		$("#remark").val("整备完好");
		$("#remark").attr("readonly","readonly");
	} 
	function checkNo(){
		$("#remark").val("");
		$("#remark").removeAttr("readonly","");
	}
	function enableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=false;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=false;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=false;
	}
	function disableBtns(){
		if(null!=document.getElementById("surePass") &&
				"undefined"!= typeof (document.getElementById("surePass").disabled))
			document.getElementById("surePass").disabled=true;
		if(null!=document.getElementById("sureUnpass") &&
				"undefined"!= typeof (document.getElementById("sureUnpass").disabled))
			document.getElementById("sureUnpass").disabled=true;
		if(null!=document.getElementById("sureUndo") &&
				"undefined"!= typeof (document.getElementById("sureUndo").disabled))
			document.getElementById("sureUndo").disabled=true;
	}
	function sureInsert(identify){
		try{
		disableBtns();
		var money = parseFloat(document.getElementById("amount").value);
		var old_money = parseFloat(document.getElementById("amount_old").value);
		var audit_remark = document.getElementById("AUDIT_REMARK").value;
		var status = document.getElementById("status").value;
		var id = document.getElementById("id").value;
		if(isNaN(money)){
			MyAlert("PDI金额请输入正确的数字 ");
			document.getElementById("amount").value = document.getElementById("amount_old").value;
			return;
		}
		if(isNaN(old_money)){
			MyAlert("PDI申请上报金额请输入正确的数字 ");
			return;
		}
		
		if(money<old_money){
			if(""==audit_remark){
				MyAlert("修改了PDI审核金额必须要填写备注! ");
				return;
			}
		}
		if(audit_remark.length>150){
			MyAlert("审核备注长度请保持在150字以内! ");
			return;
		}
		var url="<%=contextPath%>/ClaimBalanceAction/pdiAudit.json";
		if(0==identify){
			//MyConfirm("是否确认审核通过？",sureInsertCommit,[0]);
			MyUnCloseConfirm("确定要审核通过?", function(identify){
				sendRequest(id,url,status,audit_remark,identify,money);
			}, [identify], enableBtns);
		}
		if(1==identify){
			//MyConfirm("是否确认审核退回？",sureInsertCommit,[1]);
			if(""==audit_remark){
               enableBtns();
               MyAlert("提示：审核退回必须填写备注！");
               return;
			}
			MyUnCloseConfirm("确定要审核退回?", function(identify){
				sendRequest(id,url,status,audit_remark,identify,money);
			}, [identify], enableBtns);
		}if(2==identify){
			//MyConfirm("是否确认审核退回？",sureInsertCommit,[1]);
			MyUnCloseConfirm("确定要撤销审核?", function(identify){
				sendRequest(id,url,status,audit_remark,identify,money);
			}, [identify], enableBtns);
		}
		}catch(e){
			MyAlert(e);
		}
	}

	function sendRequest(id,url,status,auditRemark,identify,amount) {
		sendAjax(url+'?id='+id+"&status="+status+"&AUDIT_REMARK="+auditRemark+"&identify="+identify+"&amount="+amount,backInfo,'fm');
	}

	function backInfo(json){
		if (json.Exception) {
			MyUnCloseAlert(json.Exception.message, enableBtns);
		} else {
			MyUnCloseAlert(json.info.message, function(){
				window.location.href = g_webAppName + '/ClaimBalanceAction/claimBalanceList.do';
			});
		}
	}
	
	function goBack(){
		var goBackType = $("#goBackType").val();
		if("2" == goBackType ){
			_hide();
		}else{
			history.back();
		}
		
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;PDI索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="netitemId" value="${t.NETITEM_ID }" name="netitemId" type="hidden"  />
<input class="middle_txt" id="IS_AGREE" value="${t.IS_AGREE }"  type="hidden"  />
<input class="middle_txt" id="timeOutAmount" value="${t.TIMEOUT_AMOUNT }"  type="hidden"  />
<input class="middle_txt" id="status"  name="status" value="${t.STATUS }"  type="hidden"  />
<input class="middle_txt" id="goBackType" value="${goBackType }" name="goBackType" type="hidden"  />


<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<tr>
		<td colspan="8"><span font color='red'>${timeout}</span></td>
	</tr>
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" align="right">PDI单号：</td>
    	<td nowrap="true" width="15%" align="left">
    		${t.CLAIM_NO}
    		<input class="middle_txt" id="claim_no"  value="${t.CLAIM_NO }" readonly="readonly" name="claim_no" type="hidden" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%"  align="right">VIN：</td>
    	<td nowrap="true" width="15%" align="left">
    		${t.VIN }
    		<input class="middle_txt" id="vin" value="${t.VIN }"  name="vin" type="hidden" maxlength="30" />
		<td nowrap="true" width="10%" align="right">车型：</td>
    	<td nowrap="true" width="15%" align="left">
    		${t.MODEL_NAME }
    		<input class="middle_txt" id="model_name" value="${t.MODEL_NAME }" readonly="readonly" name="model_name" type="hidden" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
    	</td>
		<td nowrap="true" width="10%" align="right">配置：</td>
    	<td nowrap="true" width="15%" align="left">
    		${t.APP_PACKAGE_NAME  }
    		<input class="middle_txt" id="package_name" value="${t.APP_PACKAGE_NAME  }" readonly="readonly" name="package_name" type="hidden" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">发动机号：</td>
    	<td nowrap="true" width="15%" align="left">
    		${ t.ENGINE_NO }
    		<input class="middle_txt" id="engine_no" value="${ t.ENGINE_NO }" readonly="readonly" name="engine_no" type="hidden" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">颜色：</td>
    	<td nowrap="true" width="15%" align="left">
    		${t.APP_COLOR  }
    		<input class="middle_txt" id="color" value="${t.APP_COLOR  }" readonly="readonly" name="color" type="hidden" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<br/>
 <div id="new_acc_add"  style="text-align: center; width: 100%;">
 			<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
				<th colspan="8">
					<img class="nav" src="../jsp_new/img/subNav.gif" />PDI
				</th>
				<tr>
					<td nowrap="true" width="20%" >项目名称</td>
					<td nowrap="true" width="20%" >金额(元)</td>
					<td nowrap="true" width="20%" >整备是否完好</td>
					<td nowrap="true" width="40%" ><span style="color: red;">备注</span></td>
				</tr>
   				<tr>
   				  <td nowrap="true" width="20%" >
   				  		QT011
						<input readonly="readonly" name="pdi" class="middle_txt" style="display:none" value="QT011"/>
					</td><td nowrap="true" width="20%" >
						<input  id="amount_old" name="amount_old" type="hidden" class="middle_txt" value="30.0"/>
						<input  id="amount" name="amount" class="middle_txt" value="30.0"/>
					</td>
					<td nowrap="true" width="20%" >
						<div id="showOrHide" style='display:none'>
								<input type="radio" name="is_agree" onclick="checkYes();" checked="checked" value="1"/>是
								&nbsp;&nbsp;&nbsp;
								<input type="radio" name="is_agree" onclick="checkNo();" value="0"/>否
							</div>
						<c:if test="${t.IS_AGREE==1 }">
							<span>是</span>
							<input type="radio" name="is_agree" onclick="checkYes();" style='display:none' checked="checked" value="1"/>
							&nbsp;&nbsp;&nbsp;
							<input type="radio" name="is_agree" onclick="checkNo();" style='display:none' value="0"/>
						</c:if>
						<c:if test="${t.IS_AGREE==0}">
							<span>否</span>
							<input type="radio" name="is_agree" onclick="checkYes();" style='display:none' value="1"/>
							&nbsp;&nbsp;&nbsp;
							<input type="radio" name="is_agree" onclick="checkNo();" style='display:none' checked="checked" value="0"/>
						</c:if>
					</td>
					<td nowrap="true" width="40%" >
						${t.REMARK }
						<input  name="remark" id="remark" value="${t.REMARK }" readonly="readonly" type="hidden"  maxlength="100" size="65"/>
					</td>
				</tr>
    	</table>
</div>
<br/>
<!-- 添加附件 开始  -->
        <table width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
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
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
<th colspan="8">
			<img class="nav" src="../jsp_new/img/subNav.gif"/>审核备注
		</th>
		<tr>
			<td height="12" align=left width="33%" colspan='2'>
				<textarea name='ADUIT_REMARK' id='AUDIT_REMARK'	datatype="1,is_textarea,100" maxlength="100" rows='2' cols='120'></textarea>
			</td>
		</tr>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
            	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="surePass" onclick="sureInsert(0);"  style="width=8%" value="审核通过" />
               	</c:if>
               	<c:if test="${t.STATUS==10791008}">
               		&nbsp;&nbsp;
               		<input type="button" class="normal_btn" id="sureUndo" onclick="sureInsert(2);"  style="width=8%" value="撤销审核" />
               	</c:if>
               	<c:if test="${t.STATUS==10791003 || t.STATUS==10791002}">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sureUnpass" onclick="sureInsert(1);"  style="width=8%" value="审核退回" />
               	</c:if>
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="goBack();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>