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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：客户关系管理&gt;基础设定&gt;坐席排班(新增)</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" id="SS_ID" name="SS_ID" value="" jset="maindata"/>
<table id="tb1" class="table_query">
    <tr>
        <td width="15%" class="table_query_2Col_label_6Letter" >值班日期:</td>
        <td width="15%" align="left" bgcolor="#FFFFFF">&nbsp;
            <input id="DUTY_DATE" class="short_txt" name="DUTY_DATE" datatype="1,is_date,10" maxlength="10" readonly group="DUTY_DATE" jset="maindata"/>
            <input class="time_ico" onclick="showcalendar(event, 'DUTY_DATE', false);" value=" " type="button" />
        </td>
        <td width="15%" class="table_query_2Col_label_6Letter">值班员工:</td>
        <td width="20%" align="left" bgcolor="#FFFFFF">&nbsp;
            <input name="NAME" class="SearchInput" id="NAME" value="" type="text" size="20" readonly jset="maindata"/>
            <input name="ACNT" id="ACNT" value="" type="hidden" jset="maindata"/>
            <input name="USER_ID" id="USER_ID" value="" type="hidden" jset="maindata"/>
            <input name='dlbtn1' id='dlbtn1' class='mini_btn' type='button' value='...' onclick="popSelect()"/>
        </td>
        <td width="15%" class="table_query_2Col_label_6Letter" >班次类型:</td>
        <td width="20%" bgcolor="#ffffff">
            <select name="WT_TYPE" id="WT_TYPE" jset="maindata" style='width:160px;' >
            <option selected value=''>-请选择-</option>
            <c:forEach items="${wtTypeList}" var="varType">
                <option value="${varType.WT_TYPE_ID}">${varType.WT_TYPE}</option>
            </c:forEach>
            </select>
            <font color="RED">*</font>
        </td>
    </tr>
    <tr>  
        <td width="15%" class="table_query_2Col_label_6Letter">坐席业务:</td>
        <td width="20%" align="left" bgcolor="#FFFFFF">&nbsp;
            <input name="SHIFT_KIND_DESC" class="SearchInput" id="SHIFT_KIND_DESC" value="" type="text" size="20" readonly jset="maindata"/>
            <input name="SHIFT_KIND" id="SHIFT_KIND" value="" type="hidden" jset="maindata"/>
            <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...' onclick="popSelect1()"/>
        </td>                    
    </tr>
</table>
<TABLE align=center width="100%" class=csstable >
    <TR class="tblopt">
      <TD width="100%" class="tblopt">
      <div align="center">
        <input type="button" class="normal_btn" onClick="saveCRMSort();" value="保存">&nbsp;
        <input type="button" class="normal_btn" onclick="back();" value="返回" />
      </div>
      </TD>
    </TR>
  </TABLE>
</form>
</div>

<script type=text/javascript>
loadcalendar();
Date.prototype.format =function(format){
var o = {
"M+" : this.getMonth()+1, //month
"d+" : this.getDate(), //day
"h+" : this.getHours(), //hour
"m+" : this.getMinutes(), //minute
"s+" : this.getSeconds(), //second
"q+" : Math.floor((this.getMonth()+3)/3), //quarter
"S" : this.getMilliseconds() //millisecond
}
if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
(this.getFullYear()+"").substr(4- RegExp.$1.length));
for(var k in o)if(new RegExp("("+ k +")").test(format))
format = format.replace(RegExp.$1,
RegExp.$1.length==1? o[k] :
("00"+ o[k]).substr((""+ o[k]).length));
return format;
}


function popSelect(){
	var url=g_webAppName+"/jsp/customerRelationships/baseSetting/CRMSort/popselect.jsp";
	OpenHtmlWindow(url,500,300);
}

function showUser(data){
	fm.USER_ID.value=data["USER_ID"];
	fm.ACNT.value=data["ACNT"];
	fm.NAME.value=data["NAME"];
}

function popSelect1(){
	var url=g_webAppName+"/jsp/customerRelationships/baseSetting/CRMSort/popselect1.jsp";
	OpenHtmlWindow(url,500,300);
}

function showSelect1(arrayJSON){
	var SHIFT_KIND="";
	var SHIFT_KIND_DESC="";
	for(var i=0;i<arrayJSON.length;i++){
		SHIFT_KIND=SHIFT_KIND+arrayJSON[i].SHIFT_KIND+",";
		SHIFT_KIND_DESC=SHIFT_KIND_DESC+arrayJSON[i].SHIFT_KIND_DESC+",";
	}
	SHIFT_KIND=SHIFT_KIND.substr(0,SHIFT_KIND.length-1);
	SHIFT_KIND_DESC=SHIFT_KIND_DESC.substr(0,SHIFT_KIND_DESC.length-1);
	fm.SHIFT_KIND.value=SHIFT_KIND;
	fm.SHIFT_KIND_DESC.value=SHIFT_KIND_DESC;
}

function saveCRMSort(){
	var dutyDate = document.fm.DUTY_DATE.value;
	var dutyDateFmt=new Date(dutyDate.replace(/\-/g,"/"));
	var nowDate=new Date();
	var nowDateFmt=new Date(nowDate.getFullYear()+"/"+(nowDate.getMonth()+1)+"/"+nowDate.getDate());
	var userId = document.fm.USER_ID.value;
	var wtType = document.fm.WT_TYPE.value;
	var shiftKind = document.fm.SHIFT_KIND.value;
    if(null==dutyDate||"".indexOf(dutyDate)==0){
        MyAlert("请输入值班日期！");
        return false;
    }
    if(dutyDateFmt<nowDateFmt){
        MyAlert("值班日期应大于当前日期！");
        return false;
    }    
    if(null==userId||"".indexOf(userId)==0){
       MyAlert("请输入值班人员！");
       return false;
    }	
    if(null==wtType||"".indexOf(wtType)==0||wtType=="0"){
       MyAlert("请输入班次类型！");
       return false;
    }
    if(null==shiftKind||"".indexOf(shiftKind)==0||shiftKind=="0"){
        MyAlert("请输入坐席业务！");
        return false;
     }    
    var json=getElementsByJSet("maindata");   
    var url= "<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortSave.json?json="+json;
	sendAjax(url,saveResult,'fm');
}

function saveResult(json) {
    if (json != null) {
        var success = json.success;
        var alreadySort = json.alreadySort;
        var error = json.error;
        var exceptions = json.Exception;
        if (success) {
        	MyAlertForFun(success,back);
        }else if (alreadySort) {
            MyAlert(alreadySort);
        }else if (error) {
            MyAlert(error);
        } else if (exceptions) {
        	MyAlert(exceptions.message);
        }
    }
}

//返回
function back() {
	fm.action="<%=contextPath%>/customerRelationships/baseSetting/CRMSortManager/CRMSortMainInit.do";
	fm.submit();
}

</script>

</body>
</html>
