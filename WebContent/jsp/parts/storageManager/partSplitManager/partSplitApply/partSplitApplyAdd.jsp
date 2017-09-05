<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件拆合申请</title>
<style type="text/css">
.table_list_row0 td {
	background-color:#FFFFCC;
	border: 1px solid #DAE0EE;
	white-space:    nowrap;
}
</style>
</head>
<body onload="autoAlertException();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理&gt;配件拆合件管理&gt;配件拆合件申请&gt;新增申请
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
    <input type="hidden" name="PART_ID" id="PART_ID"/>
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />信息</th>
	     <tr>
	      <td width="10%"   align="right">拆合单号：</td>
	      <td width="20%">${spcpdCode }
	      <input type="hidden" name="spcpdCode" id="spcpdCode" value="${spcpdCode }"/>
	      </td>
	      <td width="10%"   align="right">制单单位：</td>
	      <td width="20%">${createOrgName }
		      <input type="hidden" name="orgId" id="orgId" value="${orgId}"/>
		      <input type="hidden" name="orgCode" id="orgCode" value="${createOrgCode }"/>
      		  <input type="hidden" name="orgName" id="createOrgName" value="${createOrgName }"/>
	      </td>
	      <td  width="10%"  align="right">制单人：</td>
	      <td  width="20%">${createName }
	      <input type="hidden" name="createName" id="createName" value="${createName }"/>
	      </td>
	    </tr>
	    <tr>
	        <td width="10%"   align="right" >仓库：</td>
      		<td width="20%">
      		<select id="WH_ID" name="WH_ID" onchange="querySubPartInfo();">
		      <c:forEach items="${wareHouses}" var="wareHouse">
	            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
	          </c:forEach>
	      </select>
	      <input type="hidden" name="whName" id="whName" value=""/>
        	</td>
        <td  width="10%"   align="right">总成件编码：</td>
	    <td  width="20%">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" />
	      <font color="red">*</font>
	      <input class="mark_btn" type="button" value="&hellip;" onclick="showPartInfoByWhId();"/>
	    </td>
        <td   width="10%"   align="right">总成件件号：</td>
	    <td  width="20%">
	      <input class="middle_txt" type="text"  name="PART_CODE" id="PART_CODE" readonly="readonly"/>
	    </td>
      </tr>
	    <tr>
	    <td  width="10%"   align="right">总成件名称：</td>
	    <td   width="20%">
	      <input class="middle_txt" type="text"  name="PART_CNAME" id="PART_CNAME" readonly="readonly"/>
	    </td>
	    <td width="10%"   align="right">拆合类型：</td>
      <td width="20%">
      <script type="text/javascript">
		       genSelBoxExp("SPCPD_TYPE",<%=Constant.PART_SPCPD_TYPE%>,"",false,"short_sel","","false",'');
	  </script>
      </td>
      <td  width="10%"   align="right">拆合数量：</td>
	    <td  width="20%">
	      <input class="middle_txt" type="text"  name="QTY" id="QTY" onblur="changeQty(this);"/>
	      <font color="red">*</font>
	    </td>
      </tr>
      <tr>
        <td  width="10%"   align="right">库存数量：</td>
	    <td  width="20%">
	      <input class="middle_txt" type="text"  name="NORMAL_QTY" id="NORMAL_QTY" readonly="readonly"/>
	    </td>
        <td  width="10%"   align="right">包装规格：</td>
	    <td  width="20%">
	      <input class="middle_txt" type="text"  name="UNIT" id="UNIT" readonly="readonly"/>
	    </td>
        <td   width="10%"   align="right">货位：</td>
	    <td   width="20%">
	      <input type="hidden"  name="LOC_ID" id="LOC_ID"/>
	      <input type="hidden"  name="LOC_CODE" id="LOC_CODE"/>
	      <input class="middle_txt" type="text"  name="LOC_NAME" id="LOC_NAME" readonly="readonly"/>
	    </td>
      </tr>
       <tr>
	      <td  width="10%"   align="right">备注：</td>
	      <td colspan="6"><textarea name="REMARK"  id="REMARK" style="width:90%" rows="4"></textarea></td>
	    </tr>    
	</table>
	 <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />分总成件信息
	  </th>
    </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_edit">
	<tr>
    	<td align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="提交" disabled="disabled" onclick="setCheckModel();"  class="normal_btn"/>
            <input type="button" name="returnBtn" id="returnBtn" value="返 回" 

onclick="javascript:goback();"  class="normal_btn"/>
        </td>
    </tr>
  </table>
</form>
<script type="text/javascript" >

autoAlertException();
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/querySubPartInfo.json";
				
var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "分总成件编码", dataIndex: 'SUBPART_OLDCODE', style: 'text-align:left',renderer:insertSubPartOldCodeInput},
				{header: "分总成件名称", dataIndex: 'SUBPART_CNAME', style: 'text-align:left',renderer:insertSubPartCnameInput},
				{header: "分总成件件号", dataIndex: 'SUBPART_CODE', style: 'text-align:left',renderer:insertSubPartCodeInput},
				{header: "规格", dataIndex: 'UNIT', align:'center',renderer:insertUnitInput},
				{header: "拆分数量", dataIndex: 'SPLIT_NUM', align:'center',renderer:insertSplitNumInput},
				//{header: "成本比例", dataIndex: 'COST_RATE', align:'center',renderer:insertCostRateInput},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center',renderer:insertLocnameInput},
				{header: "数量", dataIndex: 'QTY', align:'center',renderer:insertQtyInput},
				{header: "分总成库存数量", dataIndex: 'NORMAL_QTY', align:'center',renderer:insertNormalQtyInput},
				{header: "备注", dataIndex: 'REMARK',align:'center',renderer:insertRemarkInput}
			  ];

function insertSubPartCodeInput(value,meta,record){
    var output = '<input type="hidden"  id="SUBPART_CODE'+record.data.SUBPART_ID+'" name="SUBPART_CODE'+record.data.SUBPART_ID+'" value="'+value+'"/>'
    +'<input type="hidden"  id="SUBPART_ID" name="SUBPART_ID" value="'+record.data.SUBPART_ID+'"/>'
    +value;
    return output;
}
function insertSubPartOldCodeInput(value,meta,record){
	
    var output = '<input type="hidden"  id="SUBPART_OLDCODE'+record.data.SUBPART_ID+'" name="SUBPART_OLDCODE'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}

function insertSubPartCnameInput(value,meta,record){
	
    var output = '<input type="hidden"  id="SUBPART_CNAME'+record.data.SUBPART_ID+'" name="SUBPART_CNAME'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}

function insertUnitInput(value,meta,record){
	
    var output ='<input type="hidden"  id="SUBUNIT'+record.data.SUBPART_ID+'" name="SUBUNIT'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}
function insertSplitNumInput(value,meta,record){
	var output="";
	output = '<input type="hidden"  id="SPLIT_NUM'+record.data.SUBPART_ID+'" name="SPLIT_NUM'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}

function insertCostRateInput(value,meta,record){
	var output="";
	output = '<input type="hidden"  id="COST_RATE'+record.data.SUBPART_ID+'" name="COST_RATE'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}

function insertLocnameInput(value,meta,record){
	var output="";
	output = '<input type="hidden"  id="SUBLOC_ID'+record.data.SUBPART_ID+'" name="SUBLOC_ID'+record.data.SUBPART_ID+'" value="'+record.data.LOC_ID+'"/>'
	+'<input type="hidden"  id="SUBLOC_CODE'+record.data.SUBPART_ID+'" name="SUBLOC_CODE'+record.data.SUBPART_ID+'" value="'+record.data.LOC_CODE+'"/>'
	+value;
    return output;
}

function insertQtyInput(value,meta,record){
	var output="";
	output = '<input type="text" readonly="readonly" style="border:0;background:transparent;" class="short_txt" id="SUBQTY'+record.data.SUBPART_ID+'" name="SUBQTY'+record.data.SUBPART_ID+'" value="'+value+'"/>';
    return output;
}

function insertNormalQtyInput(value,meta,record){
	var output="";
	output = '<input type="hidden"  id="SUBNORMAL_QTY'+record.data.SUBPART_ID+'" name="SUBNORMAL_QTY'+record.data.SUBPART_ID+'" value="'+value+'"/>'+value;
    return output;
}

function insertRemarkInput(value,meta,record){
	var output="";
	output = '<input type="text" class="long_txt" id="SUBREMARK'+record.data.SUBPART_ID+'" name="SUBREMARK'+record.data.SUBPART_ID+'" value="'+value+'"/>';
    return output;
}

document.getElementById('PART_OLDCODE').attachEvent('onpropertychange',function(o){
	if(o.propertyName=='value'){//如果是value属性才执行
		var partId = document.getElementById('PART_ID').value;
		sendAjax(url,callBack,'fm');
	}
});
//查询分总成件信息
function querySubPartInfo(){
	//改变仓库之后首先清空总成件信息
	$("PART_ID").value = "";
	$("PART_OLDCODE").value = "";
	$("PART_CODE").value = "";
	$("PART_CNAME").value = "";
	$("QTY").value = "";
	$("NORMAL_QTY").value = "";
	$("UNIT").value = "";
	$("LOC_NAME").value = "";
	//var partOldCode = $("PART_OLDCODE").value;
	//if(partOldCode!=null&&partOldCode!=""){
	sendAjax(url,callBack,'fm');
	//}
}

function changeQty(qtyObj){
	var subPartId = document.getElementsByName("SUBPART_ID");
	var spcpdType = document.getElementById("SPCPD_TYPE").value;
	var l = subPartId.length;
	var qty = qtyObj.value;
	var normalQty = $("NORMAL_QTY").value;
	var pattern1 = /^[1-9][0-9]*$/; 
    if (!pattern1.exec(qty)) {
        MyAlert("拆合数量只能输入非零的正整数!");
        $("QTY").value="";
        return;
    }
    if(parseInt(spcpdType)==<%=Constant.PART_SPCPD_TYPE_01%>){//如果拆合类型是拆件
    	if(parseInt(qty)>parseInt(normalQty)){
        	MyAlert("拆合数量不能大于库存数量!");
        	return;
        }
	}
    
	for(var i=0;i<l;i++)
	{       
		var splitNum = document.getElementById("SPLIT_NUM"+subPartId[i].value).value;//拆分数量
		document.getElementById("SUBQTY"+subPartId[i].value).value=qty*splitNum;//分总成件数量
	}
}
	function setCheckModel()
	{
		//changeQty($("QTY"));
		var qty = document.getElementById("QTY").value;
		var pattern1 = /^[1-9][0-9]*$/; 
	    if (!pattern1.exec(qty)) {
	        MyAlert("拆合数量只能输入非零的正整数!");
	        $("QTY").value="";
	        return;
	   }
	    var subPartId = document.getElementsByName("SUBPART_ID");
		var l = subPartId.length;
		for(var i=0;i<l;i++)//如果改变了仓库,那么分总成件就可能改变,那么分总成件的数量也要重新计算
		{       
			var splitNum = document.getElementById("SPLIT_NUM"+subPartId[i].value).value;//拆分数量
			document.getElementById("SUBQTY"+subPartId[i].value).value=qty*splitNum;//分总成件数量
		}
		var spcpdType = document.getElementById("SPCPD_TYPE").value;
		if(parseInt(spcpdType)==<%=Constant.PART_SPCPD_TYPE_02%>){//如果拆合类型是合件,就要验证分总成件库存数量
			for(var i=0;i<l;i++)
			{        
					var subPartCname = document.getElementById("SUBPART_CNAME"+subPartId[i].value).value;//分总成件名称
					var subnormalQty = document.getElementById("SUBNORMAL_QTY"+subPartId[i].value).value;//分总成库存数量
					var subqty = document.getElementById("SUBQTY"+subPartId[i].value).value;//合成总成件时所需的分总成数量
					if(parseInt(subnormalQty)<parseInt(subqty)){
						MyAlert("分总成件["+subPartCname+"]的库存数量小于"+subqty+",不能进行合件!");
						return;
					}
			}
		}
		if(parseInt(spcpdType)==<%=Constant.PART_SPCPD_TYPE_01%>){//如果拆合类型是拆件
			var normalQty = document.getElementById("NORMAL_QTY").value;
			if(parseInt(qty)>parseInt(normalQty)){
		    	MyAlert("拆合数量不能大于库存数量!");
		    	$("QTY").value="";
		    	$("QTY").focus();
		    	return;
		    }
		}
      MyConfirm("确认提交？",saveApply);
	}
	
	function saveApply(){
	   var whIdobj = document.getElementById("WH_ID");
	   var whNameObj = document.getElementById("whName");
	   var txt = whIdobj.options[whIdobj.selectedIndex].text;
	   whNameObj.value=txt;
	   btnDisable();
	   var url1 = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/saveApply.json";	
	   sendAjax(url1,getResult,'fm');
}

function getResult(jsonObj){
	btnEnable();
	if(jsonObj){
		var success = jsonObj.success;
		var exceptions = jsonObj.Exception;
		if(success){
			MyAlert(success);
			window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/queryPartSplitApplyInit.do';
		}else if(exceptions){
	    	MyAlert(exceptions.message);
	    }
	}
}
	
function showPartInfoByWhId(){
	var spcpdType = document.getElementById("SPCPD_TYPE").value;//拆合类型
	var whId = document.getElementById("WH_ID").value;
	OpenHtmlWindow('<%=contextPath%>/jsp/parts/storageManager/partSplitManager/partSplitApply/partSelectByWhId.jsp?whId='+whId+'&spcpdType='+spcpdType,730,390);
}

function callBack(json){
	var ps;
	//设置对应数据
	if(Object.keys(json).length>0){
		keys = Object.keys(json);
		for(var i=0;i<keys.length;i++){
		   if(keys[i] =="ps"){
			   ps = json[keys[i]];
			   break;
		   }
		}
	}
	
	//生成数据集
	if(ps.records != null){
		$("_page").hide();
		$('myGrid').show();
		new createGrid(title,columns, $("myGrid"),ps).load();			
		//分页
		myPage = new showPages("myPage",ps,url);
		myPage.printHtml();
		hiddenDocObject(2);
		document.getElementById("saveBtn").disabled="";
	}else{
		$("_page").show();
		$("_page").innerHTML = "<div class='pageTips'>"+json.errorMsg+"</div>";
		$("myPage").innerHTML = "";
		removeGird('myGrid');
		$('myGrid').hide();
		hiddenDocObject(1);
		document.getElementById("saveBtn").disabled="disabled";
	}
}
//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSpiltApplyManager/queryPartSplitApplyInit.do';
}
</script>
</div>
</body>
</html>
