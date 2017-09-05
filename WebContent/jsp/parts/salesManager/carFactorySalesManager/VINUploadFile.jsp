<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/jquery-1.7.2.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
jQuery.noConflict();
var partArr = [];
function uploadExcel(code){
	    $('describe').value = parent.document.getElementById("describe").value;
	    $('checkSDate').value = parent.document.getElementById("checkSDate").value;
	    $('checkEDate').value = parent.document.getElementById("checkEDate").value;
	    $('actCode').value = parent.document.getElementById("actCode").value;
	    $('actPartType').value = parent.document.getElementById("actPartType").value;
// 	    $('state').value = parent.document.getElementById("state").value;
	    
	var state = parent.document.getElementById("state").value;
    
    if ("" == state || null == state) {
        MyAlert('请选择活动类型!');
        return false;
    }
    if(confirm("确定上传订单?，点击确定后请耐心等待.........!")){
	var importFileName = $("uploadFile1").value;
	if(importFileName==""){
	    MyAlert("请选择上传文件!");
        btnEable();
		return false;
	}
	var index = importFileName.lastIndexOf(".");
	var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
	if(suffix != "xls" && suffix != "xlsx"){
	MyAlert("请选择Excel格式文件!");
       btnEable();
		return false;
	}
	    var describe = parent.document.getElementById("describe").value;
	    var startDate = parent.document.getElementById("checkSDate").value;
	    var endDate = parent.document.getElementById("checkEDate").value;
	    var actCode = parent.document.getElementById("actCode").value;

	    if ("" == actCode || null == actCode) {
	        MyAlert('请填写活动编码!');
	        return false;
	    }
	    
	    if ("" == describe || null == describe) {
	        MyAlert('请先填写活动描述信息!');
	        return false;
	    }

	    if ("" == startDate || null == startDate) {
	        MyAlert('请先设置活动开始日期!');
	        return false;
	    }

	    if ("" == endDate || null == endDate) {
	        MyAlert('请先设置活动结束日期!');
	        return false;
	    }

	    var startDateFormat = new Date(startDate.replace("-", "/"));
	    var endDateFormat = new Date(endDate.replace("-", "/"));

	    if ((endDateFormat - startDateFormat) < 0) {
	        MyAlert('活动结束日期要晚于开始日期!');
	        return false;
	    }

	    var partIds = parent.document.getElementsByName("partIds");
	    if (null == partIds || partIds.length <= 0) {
	        MyAlert('请至少选择一个新增的配件!');
	        return false;
	    }else{
// 	    	var PartIds = document.getElementsByName("part");
// 	        if (null != PartIds && PartIds.length > 0) {
	            var idsSize = partIds.length;
	            for (var i = 0; i < idsSize; i++) {
	            	var isneedFlag = parent.document.getElementsByName("isneedFlag_"+partIds[i].value);
	            	if(isneedFlag[0].checked==false&&isneedFlag[1].checked==false){
	            	 MyAlert('请选择是否需要回运!');
	                 return false;
	            	}
	            }
// 	        }
	    }
    var obj = parent.document.getElementsByName("partIds");
    var parts = [];
    var k = 0;
    var input ="";
    var uploadDiv = null;
    for (var i = 0; i < obj.length; i++) {
        parts[k] = obj[i].value;
        uploadDiv = document.getElementById("uploadDiv");
        input = document.createElement('input');
        input.type = "hidden";
        input.name = "parts";
        input.value = parts[k];
        uploadDiv.appendChild(input);
        
        var input1 = document.createElement('input');
        var isdeendFlag = parent.document.getElementsByName("isneedFlag_"+parts[k]);
        for (var int = 0; int < isdeendFlag.length; int++) {
			if(isdeendFlag[int].checked==true){
		        input1.type = "hidden";
		        input1.name = "isneedFlag_"+parts[k];
		        input1.value = isdeendFlag[int].value;
			}
		}

        uploadDiv.appendChild(input1);

        k++;
    }
	fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityReplacedPartSetForImport.do?";
	fm.submit();
    }
}
function exportExcelTemplate(){
	parent.exportExcelTemplate();
}

function doInit(){
	if($('flag').value=='true'){
// 		parent.$('wbox').style.display = "none";
// 		MyAlert("新增成11功");
		parent.window.location.href="<%=contextPath%>/jsp/parts/baseManager/activityPartSet/activityPartSetMain.jsp";
	}
}

</script>
</head>

<body onload = "doInit();autoAlertException();" >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="flag" name="flag" type="hidden" value="${flag}">
<input id="error" name="error" type="hidden" value="${error}">
<input id="SELLER_ID" name="SELLER_ID" type="hidden" value="">
<input id="dealerId" name="dealerId" type="hidden" value="">
<input id="ORDER_TYPE" name="ORDER_TYPE" type="hidden" value="">
<input id="produceFac" name="produceFac" type="hidden" value="">
<input id="brand" name="brand" type="hidden" value="">
<input id="partData" name="partData" type="hidden" value="${partData}">
<input id="saveFlag" name="saveFlag" type="hidden" value="${saveFlag}" />
<input id="seq" name="seq" type="hidden" value="${seq}" />

<input id="describe" name="describe" type="hidden" value="">
<input id="checkSDate" name="checkSDate" type="hidden" value="">
<input id="checkEDate" name="checkEDate" type="hidden" value="">
<input id="actCode" name="actCode" type="hidden" value="">
<input id="actPartType" name="actPartType" type="hidden" value="">


<div style="display:none; heigeht: 5px" id="uploadDiv">

    <tr>
        <td><font color="red">
            <input type="button" class="normal_btn" value="下载模版" onclick="exportExcelTemplate()"/>
            文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
            <input type="file" name="uploadFile1" id="uploadFile1" style="width: 250px" datatype="0,is_null,2000"
                   value=""/>
            &nbsp;
            <input type="button" id="upbtn" class="normal_btn" value="保存" onclick="uploadExcel('')"/></td>
<!--             <input type="button" id="upbtn" class="normal_btn" value="直接生成订单" style="width:80px" onclick="uploadExcel('save')"/></td> -->
    </tr>

</div>
</form>
</body>
</html>
