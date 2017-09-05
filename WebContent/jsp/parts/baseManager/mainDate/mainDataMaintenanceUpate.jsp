<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />

<title>配件赠送设置修改</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		loadcalendar();  //初始化时间控件
		pageSearch();
	}
</script>
</head>
<BODY >
<form id="fm" name="fm" method="post">
<input type="hidden" id="prvDefid" name="prvDefid" value="${defid }">
<div class="wbox">
  <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件基础信息管理&gt; 配件基础信息维护 &gt;配件赠送设置修改</div>
  <table width="100%" class="table_list" style="background-color: #F0F7F2;">
    <tr style="height: 30px; line-height: 30px;">
      <td width="10%"  align="right">赠品方式：</td>
	  <td width="20%" align="left" >
	  <input type="hidden" id="giftWay" name="giftWay" value="${way }">
	  ${list.GIFT_WAY }
	  </td>
	  <td width="10%" align="right">活动日期：</td>
	  <td width="25%" align="left">
	    <input id="checkSDate" class="short_txt" name="checkSDate" value="${list.START_DATE_FM }" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
	    <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
	    至&nbsp;
	    <input id="checkEDate" class="short_txt" name="checkEDate" value="${list.END_DATE_FM }" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" readonly="readonly" />
		<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
		<font color="red">*</font>
	  </td>
	  <td width="10%" align="right">是否本部发起：</td> 
	  <td width="20%" align="left">
		<script type="text/javascript">
	   	 genSelBoxExp("isOemStart",<%=Constant.IF_TYPE%>, ${list.IS_OEM_START },true,"short_sel","","false",'');
		</script>
	  </td> 
    </tr>
  </table>
  <input type="hidden"  id="GIFT_TYPE" name="GIFT_TYPE" value="${list.GIFT_TYPE }" />
  <c:choose>
    <c:when test="${'PZWay' eq way}">
	  <table class="table_list" id="tableDefault"  style="background-color: #F0F7F2; display: block;" >
	    <tr>
		  <td width="10%" align="right">配件赠品描述：</td> 
		  <td width="20%" align="left">
		  ${list.GIFT_TYPE }
			  <input type="hidden"  id="PER_REMARK" name="PER_REMARK" class="long_txt" value="${list.GIFT_TYPE }" />
		  </td>  
		  <td align="center" width="30%" colspan="2">
	      <input class="cssbutton" type="button" id="saveButton" value="保 存" name="button5" onclick="savepart();"/>
	      <input class="cssbutton" onclick="showPartInfo();" value="新增配件" type="button" name="cancelBtn2" />
	      <input class="cssbutton" type="button" value="返 回" id="backButton" name="button2" onclick="goBack();"/>
	      </td> 
	      <td align="center" width="30%" colspan="2"></td> 
	    </tr>
	  </table>
	</c:when>
	<c:otherwise>
	  <table class="table_list" id="tableBlock"  style="background-color: #F0F7F2; display: block;" >
	    <tr>
		  <td width="10%" align="right">配件赠品描述：</td> 
		  <td width="20%" align="left">
			   ${list.GIFT_TYPE }
			  <input type="hidden"  id="PER_REMARK" name="PER_REMARK" class="long_txt" value="${list.GIFT_TYPE }" />
		  </td>  
		  <td width="10%" align="right">销货金额大于：</td> 
		  <td width="25%" align="left">
			  <input type="text" id="PER_NAME"  name="PER_NAME" class="middle_txt" value="${list.CONDITION}" onchange="dataTypeCheck(this)"/> (单位：<font color="blue">元</font>)
			  <font color="red">*</font>
		  </td> 
		  <td align="center" width="30%" colspan="2">
	      <input class="cssbutton" type="button" id="saveButton" value="保 存" name="button5" onclick="savepart();"/>
	      <input class="cssbutton" onclick="showPartInfo();" value="新增配件" type="button" name="cancelBtn2" />
	      <input class="cssbutton" type="button" value="返 回" id="backButton" name="button2" onclick="goBack();"/>
	      </td> 
	    </tr>
	    <tr>
	    </tr>
	  </table>
	</c:otherwise>
  </c:choose>
  
  <table id="file" class="table_list" style="display:none">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />配件赠送明细</th>
    </tr>
  </table>
</div>
<!-- 查询条件 end -->
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/detailQuery.json";

var title = null;
var columns = null;

function pageSearch()
{
	var giftWay = document.getElementById("giftWay").value;
	if("ZDWay" == giftWay)
	{
		columns = [
					{header: "序号", dataIndex: 'DEF_ID', renderer:getIndex,align:'center'},
					{header: "配件编码",dataIndex: 'PART_OLDCODE',align:'center'},
					{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
					{header: "配件件号", dataIndex: 'PART_CODE', align:'center'},
					{header: "赠送数量", dataIndex: 'GIFT_QTY', align:'center',renderer:qtyText},
					{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
					{header: "操作",sortable: false, dataIndex: 'DEF_ID', align:'center',renderer:myLink}
			      ];
	}
	else
	{
		columns = [
					{header: "序号", dataIndex: 'DEF_ID', renderer:getIndex,align:'center'},
					{header: "配件编码",dataIndex: 'PART_OLDCODE',align:'center'},
					{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
					{header: "配件件号", dataIndex: 'PART_CODE', align:'center'},
					{header: "满足数量", dataIndex: 'COND_NUM', align:'center',renderer:meetText},
					{header: "赠送数量", dataIndex: 'GIFT_QTY', align:'center',renderer:qtyText},
					{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
					{header: "操作",sortable: false, dataIndex: 'DEF_ID', align:'center',renderer:myLink}
			      ];
	}
	__extQuery__(1);
}

     

//设置超链接
function myLink(value,meta,record)
{
	var defineId = record.data.DEF_ID;
	var state = record.data.STATE;
	var disableValue = <%=Constant.STATUS_DISABLE%>;
	if(disableValue == state){
		return String.format("<a href=\"#\" onclick='enableData(\""+defineId+"\")'>[有效]</a>");
    } else {
    	return String.format("<a href=\"#\" onclick='cel(\""+defineId+"\")'>[失效]</a>");
	}
		
}

//返回 text
function qtyText(value,meta,record)
{
	var defineId = record.data.DEF_ID;
	var str = "<input type='hidden'  name='defineIds' value=\""+defineId+"\" /><input type='text' id='qty_"+defineId+"' name='qty_"+defineId+"' onchange='dataTypeCheck1(this)' value=\""+value+"\" />"
	return String.format(str);
		
}
function meetText(value,meta,record)
{
	var defineId = record.data.DEF_ID;
	var str = "<input type='text' id='meetNum_"+defineId+"' name='meetNum_"+defineId+"' onchange='dataTypeCheck1(this)' value=\""+value+"\" />"
	return String.format(str);
		
}


//数据验证
function dataTypeCheck1(obj)
{
	var value = obj.value;
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = "";
        return;
    }
    var re = /^([1-9]+[0-9]*]*)$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
}

function dataTypeCheck(obj)
{
	var value = obj.value;
	value = value + "";
	value = parseFloat(value.replace(new RegExp(",","g"),""));
    if (isNaN(value) || "" == value) {
        MyAlert("请输入数字!");
        obj.value = (0.00).toFixed(2);
        return;
    }
    if(0 > value)
    {
	    MyAlert("销货金额不能小于 0!");
	    obj.value = (0.00).toFixed(2);
	    return;
    }
    obj.value = addKannma(value.toFixed(2));
}

//千分格式
function addKannma(number) {  
    var num = number + "";  
    num = num.replace(new RegExp(",","g"),"");   
    // 正负号处理   
    var symble = "";   
    if(/^([-+]).*$/.test(num)) {   
        symble = num.replace(/^([-+]).*$/,"$1");   
        num = num.replace(/^([-+])(.*)$/,"$2");   
    }   
  
    if(/^[0-9]+(\.[0-9]+)?$/.test(num)) {   
        var num = num.replace(new RegExp("^[0]+","g"),"");   
        if(/^\./.test(num)) {   
        num = "0" + num;   
        }   
  
        var decimal = num.replace(/^[0-9]+(\.[0-9]+)?$/,"$1");   
        var integer= num.replace(/^([0-9]+)(\.[0-9]+)?$/,"$1");   
  
        var re=/(\d+)(\d{3})/;  
  
        while(re.test(integer)){   
            integer = integer.replace(re,"$1,$2");  
        }   
        return symble + integer + decimal;   
  
    } else {   
        return number;   
    }   
}

function savepart(){
	var giftWay = document.getElementById("giftWay").value;
	var defineIds = document.getElementsByName("defineIds");
 	var PER_REMARK =   document.getElementById("PER_REMARK").value;
 	var  PER_NAME = 0;
 	if("ZDWay" == giftWay)
 	{
 		PER_NAME = document.getElementById("PER_NAME").value;
 		if(PER_NAME==null||PER_NAME.length==0){
 			MyAlert("请填写销货金额!");	
 			return false;
 		}
 	 		
 	}
	for(var i=0;i<defineIds.length;i++){
		var meetNumTmp = "";
		var giftQty = "";
		
		if("ZDWay" == giftWay)
	 	{
			giftQty = document.getElementById("qty_" + defineIds[i].value).value;
			if (isNaN(giftQty) || "" == giftQty)
			{
				MyAlert("第"+ (i + 1) +"行赠送数量不合法!");
				return false;
			}
	 	}
		else
		{
			meetNum = document.getElementById("meetNum_" + defineIds[i].value).value;
			if (isNaN(meetNum) || "" == meetNum)
			{
				MyAlert("第"+ (i + 1) +"行满足数量不合法!");
				return false;
			}
			giftQty = document.getElementById("qty_" + defineIds[i].value).value;
			if (isNaN(giftQty) || "" == giftQty)
			{
				MyAlert("第"+ (i + 1) +"行赠送数量不合法!");
				return false;
			}
		}
		
	}
	if (submitForm('fm')) {
		MyConfirm("确认保存设置?", savePart);
	}


}
function savePart()
{
	btnDisable();
	var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/UpdateDateMainDataMaintenance.json?curPage='+myPage.page;
	makeFormCall(url,showResult,'fm');
}

//设置失效：
function cel(defId) {
	if(confirm("确定失效该数据?")){
		btnDisable();
		var optionType = "disable";
     	var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/celOrEnablePart.json?defId='+defId+'&curPage='+myPage.page+'&optionType='+optionType;
  		makeFormCall(url,showResult,'fm');
    }
}

//设置有效：
function enableData(defId) {
	if(confirm("确定有效该数据?")){
		btnDisable();
		var optionType = "enable";
     	var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/celOrEnablePart.json?defId='+defId+'&curPage='+myPage.page+'&optionType='+optionType;
  		makeFormCall(url,showResult,'fm');
    }
}

function showResult(json) {
	btnEnable();
    if (json.errorExist != null && json.errorExist.length > 0) {
        MyAlert(json.errorExist);
    } else if (json.success != null && json.success == "true") {
    	MyAlert("操作成功!");
    	document.getElementById("GIFT_TYPE").value = document.getElementById("PER_REMARK").value;
    	__extQuery__(json.curPage);          
    } else {
        MyAlert("操作失败，请联系管理员!");
    }
}


//新增配件
function showPartInfo(){
	var prvDefid = document.getElementById("prvDefid").value;
	var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/goPartQueryMod.do?prvDefid='+prvDefid;
	OpenHtmlWindow(url,730,500);
}


function goBack(){
	btnDisable();
	window.location.href = "<%=request.getContextPath()%>/parts/baseManager/mainData/mainDataMaintenance/mainDataMaintenanceInit.do";
}

//失效按钮
function btnDisable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = true;
    });

}

//有效按钮
function btnEnable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = "";
    });

}

</script>
</BODY>
</html>
