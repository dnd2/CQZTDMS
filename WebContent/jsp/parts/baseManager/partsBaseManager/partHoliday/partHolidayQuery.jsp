<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>配件节假日维护</title>
    
<script type="text/javascript">
// 初始化
$(function(){
	__extQuery__(1);
	if("${impExcelErrorInfo}" != ""){
		MyAlert("${impExcelErrorInfo}");
	}
});

var myPage;

var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartHolidayManager/partHolidayQuery.json";

var title = null;

var columns = [
    {header: "序号", style: "text-align: center", renderer: getIndex},
    {
        id: 'action',
        header: "操作",
        sortable: false,
        dataIndex: 'HOLIDAY_ID',
        renderer: myLink,
        style: "text-align: center"
    },
    	
    {header: "节假日日期", dataIndex: 'HOLIDAY_DATE', style: "text-align: center",},
    {header: "状态", dataIndex: 'STATUS', style: "text-align: center", renderer: getItemValue},
];

//设置超链接
function myLink(value, meta, record) {
    if (record.data.STATUS == <%=Constant.STATUS_ENABLE%>) {
        return String.format('<a href="#" onclick=\'disOrEnAction("'+ value +'","<%=Constant.STATUS_DISABLE%>","失效")\'>[失效]</a>');
    } else {
        return String.format('<a href="#" onclick=\'disOrEnAction("'+ value +'","<%=Constant.STATUS_ENABLE%>","有效")\'>[有效]</a>');
    }
}

// 失效/有效
function disOrEnAction(id, value, text) {
    MyConfirm("确定要"+text+"?", disOrEn, [id, value]);
}
function disOrEn(id,actionStatus) {
    disableAllClEl();
    var abateUrl = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartHolidayManager/disOrEnHoilday.json';
    abateUrl +=	'?holidayId='+id+'&actionStatus=' + actionStatus + '&curPage=' + myPage.page;
    makeNomalFormCall(abateUrl, veiwParts, 'fm');
}

function veiwParts(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        MyAlert(success);
        __extQuery__(jsonObj.curPage);
    }
}

// 导出excel
function expHolidayExcel(flag) {
    document.getElementById("flag").value = flag;
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartHolidayManager/expHolidayExcel.json";
    fm.submit();
}

// 导入Excel模板
function exportExcelTemplate() {
    document.fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartHolidayManager/exportExcelTemplate.do";
	document.fm.target = "_self";
	document.fm.submit();

}

// 导入节假日Excel
function uploadEx() {
    var fileValue = document.getElementById("uploadExcelFile").value;

    if (fileValue == '') {
        layer.msg('导入文件不能空!', {icon: 15});
        return;
    }
    var fi = fileValue.substring(fileValue.length - 3, fileValue.length);
    if (fi != 'xls') {
        layer.msg('导入文件格式不对,请导入xls文件格式', {icon: 15});
        return;
    }
    MyConfirm("确定导入?", uploadExConfirm, []);
}

// 确认导入节假日Excel
function uploadExConfirm(){
    btnDisable();
    fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartHolidayManager/impHolidayExcel.do";
    fm.submit();
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
function showUpload() {
	$("#BtnuploadTable")[0].style.cssText = "background-color:red";
	if ($("#uploadTable")[0].style.display == "none") {
		$("#uploadTable")[0].style.display = "block";
	} else {
		$("#uploadTable")[0].style.display = "none";
		$("#BtnuploadTable")[0].style.cssText = "background-color:none";
	}
	$("#uploadTable2")[0].style.display = "none";
	$("#BtnuploadTable2")[0].style.cssText = "background-color:none";
}
function showlogic() {
	$("#uploadTable")[0].style.display = "none"
	if ($("#showLogic")[0].style.display == "none") {
		$("#showLogic")[0].style.display = "block";
	} else {
		$("#showLogic")[0].style.display = "none";
	}
}
function showUpload2() {
	$("#BtnuploadTable2")[0].style.cssText = "background-color:red";
	if ($("#uploadTable2")[0].style.display == "none") {
		$("#uploadTable2")[0].style.display = "block";
	} else {
		$("#uploadTable2")[0].style.display = "none";
		$("#BtnuploadTable2")[0].style.cssText = "background-color:none";
	}
	$("#uploadTable")[0].style.display = "none";
	$("#BtnuploadTable")[0].style.cssText = "background-color:none";
}
function succAlert() {
	if ('${flag}' != '') {
		MyAlert('${flag}');
	}
}
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理> 基础信息管理 > 配件基础信息维护 > 配件节假日维护</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="flag" id="flag"/>
        <div class="form-panel">
			<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td class="right">节假日开始日期：</td>
                        <td>
                            <input class="middle_txt" readonly="readonly"  type="text" id="HOLIDAY_START_DATE" name="HOLIDAY_START_DATE" group="HOLIDAY_START_DATE,HOLIDAY_END_DATE" datatype="1,is_date,10"/>
                            <input class="time_ico" type="button" value="&nbsp;" />
                        </td>
                        <td  class="right">节假日结束日期：</td>
                        <td>
                            <input class="middle_txt" readonly="readonly"  type="text" id="HOLIDAY_END_DATE" name="HOLIDAY_END_DATE" group="HOLIDAY_START_DATE,HOLIDAY_END_DATE" datatype="1,is_date,10"/>
                            <input class="time_ico" type="button" value="&nbsp;" />
                        </td>
                        <td  class="right">状态：</td>
                        <td>
                            <script type="text/javascript">
                                genSelBox("STATUS", <%=Constant.STATUS%>, "<%=Constant.STATUS_ENABLE%>", true, "", "");
                            </script>
                        </td>
                    </tr>
                    <tr>
                        <td class="formbtn-aln" align="center" colspan="6">
                            <input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                            <input class="u-button" type="reset" name="reset" value="重 置" onclick="reset()"/>
                            <input class="u-button" type="button" name="BtnExportPartBaseExcel" value="导 出" onclick="expHolidayExcel()"/>
                            <input class="u-button" type="button" id="BtnuploadTable" value="批量导入" onclick="showUpload();"/>
                        </td>
                    </tr>
                </table>
                <table id="uploadTable" class="form-button-extra" style="display: none">
                    <tr>
                        <td>
                            <font color="red">
                                <input type="button" class="u-button" value="下载模版" onclick="exportExcelTemplate()"/>
                                	文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;
                            </font>
                            <input type="file" name="uploadExcelFile" style="width: 250px" id="uploadExcelFile" value=""/>
                            &nbsp;
                            <input type="button" id="upbtn" class="u-button" value="确定" onclick="uploadEx()"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>    

</body>
</html>
