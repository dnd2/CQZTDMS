<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>广宣品物流信息查询</title>

</head>
<body onload="doInit();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath %>/img/nav.gif" alt=""/>&nbsp;当前位置： 配件管理 &gt; 本部销售管理  &gt; 广宣品物流信息查询 </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>查询条件</th>
            <!--  <tr >
                <td   align="right">服务商代码：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_CODE" name="DEALER_CODE"/></td>
                <td   align="right">服务商名称：</td>
                <td width="24%"><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
               <td   align="right">装箱号：</td>
                <td width="24%"><input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO"/></td>
            </tr>
            <tr>
                <td   align="right">发运方式：</td>
                <td width="24%">
                    <script type="text/javascript">
                        genSelBox("TRANS_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
	           </td>
	           <td   align="right">要求最终发运日期：</td>
                <td width="24%">
                <input name="CstartDate" type="text" class="short_time_txt" id="CstartDate" value=""
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CstartDate', false);"/>
                至
                <input name="CendDate" type="text" class="short_time_txt" id="CendDate" value=""
                       style="width:65px"/>
                <input name="button2" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'CendDate', false);"/>
                </td>
               
               <td   align="right">是否已发运：</td>
                <td width="24%">
                <script type="text/javascript">
                        genSelBox("ISTRANS", <%=Constant.IF_TYPE%>, <%=Constant.IF_TYPE_NO%>, true, "short_sel", "", "false", '');
                    </script>
                </td>
                
            </tr>
           
           <tr>
                <td   align="right">承运物流：</td>
                <td width="24%">
                    <select name="transportOrg" id="transportOrg" class="short_sel">
                    <option value="">--请选择--</option>
                        <c:forEach items="${listc}" var="obj">
                            <option value="${obj.fixValue}" ${po['TRANS_ORG'] eq obj.fixValue?"selected":"" }>${obj.fixName}</option>
                        </c:forEach>
                   </select>
	           </td>
            </tr>
            
            <tr>
                <td colspan="6" align="center">
                <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button"
                                                      value="查 询" onclick="__extQuery__(1);"/>
                &nbsp;
                <input class="normal_btn" type="button" value="修改" onclick="modifyTransType();"/>
                &nbsp;
                <input class="normal_btn" type="button" value="导 出" onclick="expExcel();"/>
                </td>

            </tr> -->
        </table>
        <div id="layer">
            <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
            <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        </div>
    </div>
</form>
</body>

<script language="javascript">
autoAlertException();
//初始化查询TABLE
var myPage;

var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/PartGxTransTypeLogMsgCheckQuery.json";

var title = null;

var columns = [
    /*{header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PLAN_ID', align:'center',width: '33px' ,renderer:seled},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_CODE', renderer: myLink, align: 'center'},*/
    {header: "服务站代码", dataIndex: 'DEALER_CODE', align: 'center'},
    {header: "服务站名称", dataIndex: 'DEALER_NAME', align: 'center'},
    {header: "发运计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center'},
    {header: "装箱号", dataIndex: 'PKG_NO', style:'text-align:left;'},
    {header: "随车物流名称", dataIndex: 'LOGI_NAME', align: 'center'},
    {header: "随车发运单号", dataIndex: 'ASS_NO', align: 'center', renderer: getItemValue},
    {header: "随车发运时间", dataIndex: 'ASS_DATE', align: 'center'},
    {header: "其它物流名称", dataIndex: 'TRANS_ORG', align: 'center'},
    {header: "物流单号", dataIndex: 'WL_NO', align: 'center'},
    {header: "物流发运时间", dataIndex: 'WL_DATE', align: 'center'}

];

function selAll(obj){
	var cks = document.getElementsByName('ck');
	for(var i =0 ;i<cks.length;i++){
		if(obj.checked){
			cks[i].checked = true;
		}else{
			cks[i].checked = false;
		}
	}
 }

function seled(value,meta,record) 
{
	 var pkgNo = record.data.PKG_NO;
	 var assNo = record.data.ASS_NO;
	 if(assNo){
		 return String.format('<img src="<%=contextPath%>/img/close.gif" />');
	 }else{
		 return "<input type='checkbox' value='"+value+","+pkgNo+"' name='ck' id='ck' onclick='chkPart()'/>";
	 }
}

function chkPart(){
	var cks = document.getElementsByName('ck');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("ckbAll").checked = flag;
}

//function myLink(value, meta, record) {
	//var isTrans = $("ISTRANS").value;
	//var pkgNo = record.data.PKG_NO;
	///*if(isTrans==<%=Constant.IF_TYPE_YES%>){
		//return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + pkgNo + "\")'>[查看]</a>");
	//}else{
		//return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + pkgNo + "\")'>[查看]</a>" + "<a href=\"#\" onclick='modify(\"" + value + "\",\"" + pkgNo + "\")'>[修改]</a>");
	//}*/
	//return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + pkgNo + "\")'>[查看]</a>");
//}

function modifyTransType(){
	var ck = document.getElementsByName('ck');
	var cn=0;
	
	var transType = $("TRANS_TYPE").value;
	var transportOrg = $("transportOrg").value;
	
	if(!transType||!transportOrg){
		MyAlert("请选择发运方式和承运物流!");
		return;
	}
	
	for(var i = 0 ;i<ck.length; i ++){
		if(ck[i].checked){
			cn++;
		}
	}
	if(cn==0){
		MyAlert("请选择要修改的记录!");
		return;
	}

    if(confirm("确定修改?")){
    	btnDisable();
    	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/updatePartGxPlan.json";
        sendAjax(url, getResult, 'fm');
    }
}

//查看
function detailOrder(value, pkgNo) {
    var buttonFalg="disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/detailPlanOrderInit.do?planCode=" + value + "&pkgNo=" + pkgNo+"&buttonFalg="+buttonFalg,900,400);
}

function modify(value, pkgNo) {
    disableAllClEl();
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/modifyPlanOrderInit.do?planCode=" + value+"&pkgNo="+pkgNo;
}

function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}

function enableAllA() {

    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
    }
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}

function doInit() {
    loadcalendar();
    //initCondition();
    __extQuery__(1);
    enableAllClEl();
}

function getResult(jsonObj) {
	btnEable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//导出
function expExcel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/expExcel.do";
    fm.target = "_self";
    fm.submit();
}
</script>

</html>