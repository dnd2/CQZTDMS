<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>售后经销商绑定</title>
<script type="text/javascript">	
	var myPage;
	var url = "<%=contextPath%>/sysmng/dealer/ServiceDealerBind/queryBindServiceDealerInfo.json";
	var title= null;
	var columns = [{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
					{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
					{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
					{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
					{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
					{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
					{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
					{header: "验收形象等级", width:'10%', dataIndex: 'IMAGE_COMFIRM_LEVEL',renderer:getItemValue},
					{id:'action',header: "绑定详情", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
				  ];
   
	//查询
	function doQuery() {
	    //执行查询
	    __extQuery__(1);

	    // 绑定成功信息
	    var successMessage = document.getElementById("successMessage").value;
	    if (successMessage) {
			MyAlert(successMessage);
			document.getElementById("successMessage").value="";
		}
	}
	function myLink(dealer_id){	    
		var CUR_DEALER_ID = document.getElementById("CUR_DEALER_ID").value;
		var link = "<a href=\"<%=contextPath%>/sysmng/dealer/ServiceDealerBind/relieveBindServiceDealer.do?DEALER_ID="+dealer_id+"&CUR_DEALER_ID="+CUR_DEALER_ID+"\">[解除绑定]</a>"; 
	    return String.format(link);
	} 
	function showCompanyA(path){ 
		OpenHtmlWindow(path+'/common/OrgMng/queryCompanyA.do',800,450);
	}
	function clrTxt(){
    	document.getElementById("dealerCode").value = "";
    	document.getElementById("DEALER_NAME").value="";
    }
    function getModel(id, code, name) {
		document.getElementById("DEALER_NAME").value=name;
		document.getElementById("DEALER_ID").value=id;
    }
    // 绑定经销商前CHECK
    function bindServiceDealerCheck() {
        document.getElementById("bindBtn").disabled=true;
        var dealerCode = document.getElementById("dealerCode").value;
        if (!dealerCode) {
        	MyAlert("请选择被绑定的经销商!");
        	document.getElementById("bindBtn").disabled=false;
        	return;
        }
        MyConfirm("是否确认进行绑定操作？",function(){
        	sendAjax('<%=contextPath%>/sysmng/dealer/ServiceDealerBind/bindServiceDealerCheck.json',bindServiceDealer,'fm');
       },"");
    }
    // 确定进行绑定操作
    function bindServiceDealer(json) {
        if (json.Exception) {
            MyAlert(json.Exception.message);
            document.getElementById("bindBtn").disabled=false;
        } else {
            if (json.warningMessage) {
            	MyConfirm(json.warningMessage, function(){
            		fm.action="<%=contextPath%>/sysmng/dealer/ServiceDealerBind/bindServiceDealer.do";
                    fm.submit();
                }, "");
            } else {
            	fm.action="<%=contextPath%>/sysmng/dealer/ServiceDealerBind/bindServiceDealer.do";
                fm.submit();
            }
        }
    }
</script>
</head>
<body onload="doQuery()">
<form method="post" id="fm" name="fm">
	<table class="table_query" border="0">
		<tr>
			<td colspan="6">
				当前经销商：${dealerName }
			</td>
		</tr>
		<tr>
			<td align="right">选择经销商：</td>  
			<td align="left">
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button3" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','','false','','','','10771002','');" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt()"/>
			</td>
			<td align="right" width="15%">经销商名称：</td>
			<td align="left">
				<input type='text' class="middle_txt" name="DEALER_NAME" readonly="readonly" id="DEALER_NAME" value="" />
			</td>
		</tr>
		<input type="hidden" name="DEALER_ID" value="" id="DEALER_ID" />
		<input type="hidden" name="CUR_DEALER_ID" value="${curDealerId }" id="CUR_DEALER_ID" />
		<input type="hidden" name="successMessage" value="${successMessage }" id="successMessage" />
	</table>
	<table class=table_query>
		<tr>
			<td align="center">
				<input type="button" value="绑 定" name="bindBtn" class="normal_btn" onclick="bindServiceDealerCheck()" id="bindBtn"/> 
				<input type="button" value="查 询" name="queryBtn" class="normal_btn" onclick="doQuery()" id="queryBtn"/> 
				<input type="button" value="返 回" name="cancelBtn" class="normal_btn" onclick="history.back();" id="cancelBtn"/>
			</td>
		</tr>
	</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</body>
</html>

