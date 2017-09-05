<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<title>配件转换</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 仓储相关信息维护
    配件形态转换 &gt;转换历史查询
</div>
<form method="post" name ="fm" id="fm" method="post" enctype="multipart/form-data">
	<table class="table_query">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	    <tr>
            <td align="right">库房：</td>
            <td align="left">
                <select name="WH_ID" id="WH_ID" class="short_sel" onchange="changeSub();">
                    <c:forEach items="${list}" var="wareHouse">
                        <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                    </c:forEach>
                </select>
            </td>
	      <td align="right">配件编码：</td><td><input class="middle_txt" type="text" name="PART_CODE" /></td>
          <td align="right">配件名称：</td><td><input class="middle_txt" type="text" name="PART_NAME" /></td>
          <%--<td align="right">货位编码：</td>
          <td><input name="LOC_CODE" id="LOC_CODE" class="middle_txt" type="text" value="">
	          <input class='mini_btn' type='button' value='...' onclick="codeChoice('query');"/>
          </td>--%>
      </tr>
        <tr>
            <td align="right">操作日期：</td>
            <td>
                <input name="TstartDate" type="text" class="short_time_txt" id="TstartDate" value="${old}"
                       style="width:65px"/>
                <input name="button2" type="button" value=" " class="time_ico"
                       onclick="showcalendar(event, 'TstartDate', false);"/>
                至
                <input name="TendDate" type="text" class="short_time_txt" id="TendDate" value="${now}"
                       style="width:65px"/>
                <input name="button2" type="button" value=" " class="time_ico"
                       onclick="showcalendar(event, 'TendDate', false);"/>
        </tr>
      <tr>
		 <td align="center" colspan="6">
		  <input class="normal_btn" type="button" value="查询" name="BtnQuery" id="queryBtn" onClick="__extQuery__(1);"/>
		  <input class="normal_btn" type="button" value="关闭" name="BtnClose" id="BtnClose" onClick="_hide();"/>
		 </td>
	  </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartFormConvert/query.json";
	var title = null;
	var columns = [
					{header: "序号", style: 'text-align:center', renderer:getIndex},
                    {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:center'},
					{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:center'},
					{header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:center'},
//					{header: "库存数量", dataIndex: 'ITEM_QTY', style: 'text-align:left'/*,renderer:numItemQty*/},
//					{header: "可用数量", dataIndex: 'NORMAL_QTY', style: 'text-align:left'},
					{header: "转出货位", dataIndex: 'LOC_CODE', style: 'text-align:center'},
					{header: "转出数量", dataIndex: 'PART_NUM', style: 'text-align:center'/*,renderer:moveNum*/},
					{header: "转入编码", dataIndex: 'TOPART_OLDCODE', style: 'text-align:center'/*,renderer:convertCode*/},
					{header: "转入货位", dataIndex: 'TOLOC_CODE', style: 'text-align:center'/*,renderer:choice*/},
					{header: "转入数量", dataIndex: 'TOPART_NUM', style: 'text-align:center'/*,renderer:choice*/},
					{header: "操作日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'/*,renderer:choice*/}
//					{header: "操作", dataIndex: 'PART_ID', style: 'text-align:center'/*,renderer:myLink*/}
		      ];
    function numItemQty(value,meta,record){
        var id = record.data.PART_ID +","+ record.data.LOC_ID;
		var text = "<input type='hidden' value='"+value+"' name='qty_"+id+"' id='qty_"+id+"' />";
		return String.format(text+value);
    }
    function convertCode(value,meta,record){
    	var id = record.data.PART_ID +","+ record.data.LOC_ID;
		var text = "<input class='middle_txt' type='text' name='convertCode_"+id+"' id='convertCode_"+id+"' readonly='readonly'/>";
		text = text + '<input name="BUTTON" class="mini_btn" onclick="sel1(\''+id+'\')" type="button" value="..."/>';
		return String.format(text);
    }
    function moveNum(value,meta,record){
        var id = record.data.PART_ID +","+ record.data.LOC_ID;
		var text = "<input class='short_txt' type='text' name='num_"+id+"' id='num_"+id+"' />";
		return String.format(text);
    }
    function choice(value,meta,record){
    	var id = record.data.PART_ID +","+ record.data.LOC_ID;
    	var whId = record.data.WH_ID;
    	var whName = record.data.WH_NAME;
    	var text = "<input name='LOC_CODE_"+id+"' id='LOC_CODE_"+id+"' class='middle_txt' type='text' value='' onchange='checkCode(this,\""+id+"\",\""+whId+"\",\""+whName+"\");'/>";
    		text = text	+ "<input name='LOC_ID_"+id+"' id='LOC_ID_"+id+"' type='hidden' value='123' />";
    		text = text + "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\""+id+"\",\""+whId+"\");'/>";
		return String.format(text);
    }
  //选择
    function sel1(id) {
        OpenHtmlWindow(g_webAppName+'/parts/baseManager/partsBaseManager/PartFormConvert/partExceptionQuerySelect.do?ID='+id, 700, 500);
    }
	//设置超链接
	function myLink(value,meta,record)
	{
		var id = record.data.PART_ID +","+ record.data.LOC_ID;
		var whId = record.data.WH_ID;
		var text = "<input type='hidden' value='"+id+"' />";
		text = text + "<input name='WH_ID_"+id+"' id='WH_ID_"+id+"' type='hidden' value='"+whId+"'>";
  		var yiwei = "<input class='normal_btn' type='button' value='[保存]' onClick='moveSeat(\""+id+"\");'/>";
  		return String.format(text+yiwei);
	}
	jQuery.noConflict();
	function codeChoice(id,whId){
		OpenHtmlWindow(g_webAppName +"/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id="+id+"&whId="+whId,700,400);
	}
	var LOC_ID = null;
	function codeSet(i,c,n){
		var v = i+","+c+","+n;
		if("query"==LOC_ID){
			jQuery("#LOC_CODE").val(c);
		}else{
			document.getElementById("LOC_CODE_"+LOC_ID).value = c;
			document.getElementById("LOC_ID_"+LOC_ID).value = v;
		}
	}
	function setPartCode(partId,partOldCode,partCode,partCname){
		document.getElementById("convertCode_"+LOC_ID).value = partOldCode;
	}
	function checkCode(th,partId,whId,whName){
		var loc_code = th.value;
		var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
		var para = "LOC_CODE="+loc_code+"&PART_ID="+partId+"&whId="+whId+"&whName="+whName;
		makeCall(url2,forBack2,para);
	}
	function forBack2(json){
		if(json.returnValue != 1){
			var partId = json.PART_ID;
			if(partId!=""){document.getElementById("LOC_CODE_" + partId).value = "";}
			parent.MyAlert("该货位编码在仓库【"+json.whName+"】中不存在,请先维护在操作！");
		}else{
            codeSet(json.LOC_ID,json.LOC_CODE,json.LOC_CODE);
        }
	}
	function moveSeat(id){
		if(validate(id)){
			var partId = id.split(",")[0];
			var locId = id.split(",")[1];
			var mt = document.getElementById("myTable");
			for (var i = 1; i < mt.rows.length; i++) {
		        var pl = mt.rows[i].cells[10].firstChild.value;
		        if (pl==id) {
	                var partCode = mt.rows[i].cells[2].innerText;  //件号
	                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
	                var itemQty = mt.rows[i].cells[4].innerText;  //配件库存
	                var locCode = mt.rows[i].cells[6].innerText;  //货位编码
	                var number = document.getElementById("num_" + id).value;  //转换数量
	                var convertCode = document.getElementById("convertCode_" + id).value;  //转换编码
	                var loc = document.getElementById("LOC_CODE_" + id).value;  //转换货位
	                var whId = document.getElementById("WH_ID_" + id).value;  //转换仓库信息
	                if(partCode.trim() == convertCode.trim()){
	                	MyAlert("转换配件编码和原配件编码一致！");
	                	return;
		            }
                    if(confirm("确定要形态转换?")){
					var url2 = g_webAppName+"/parts/baseManager/partsBaseManager/PartFormConvert/moveSeat.json";
					var paramas = "PART_ID="+partId+"&LOC_ID="+locId+"&PART_CODE="+partCode+"&PART_NAME="+partCname+"&MOVE_NUM="+number+"&LOC_CODE="+loc+"&WH_ID="+whId+"&convertCode="+convertCode;
					makeCall(url2,moveCallBack,paramas);
                    }
		        }
		    }
		}
	}
	function moveCallBack(jsonObj){
		if (jsonObj != null) {
	        var success = jsonObj.success;
	        var error = jsonObj.error;
	        var exceptions = jsonObj.Exception;
	        if (null != error && error.length > 0) {
	            MyAlert(error);
	        }else if (null != success && success.length > 0) {
	        	MyAlert(success);
	        	__extQuery__(1);
	        }else {
	        	MyAlert(exceptions.message);
	        }
	    }
	}
	function validate(id){
		var loc = document.getElementById("LOC_CODE_"+id).value;
		var num = document.getElementById("num_"+id).value;
		var qty = document.getElementById("qty_"+id).value;
		var convertCode = document.getElementById("convertCode_"+id).value;
		if(convertCode==""){
			MyAlert("请输入转换配件编码！");
			return false;
		}
		if(loc==""){
			MyAlert("请先选择移入货位！");
			return false;
		}
		if(Number(qty) < Number(num)){
			MyAlert("转换数量不能大于库存数量！");
			return false;
		}
		var re = /^[1-9]+[0-9]*]*$/;
	    if (!re.test(num)) {
	        MyAlert("转换数量必须是正整数！");
	        return;
	    }
		if(num==""){
			MyAlert("请输入转换数量！");
			return false;
		}
		return true;
	}
</script>
</html>