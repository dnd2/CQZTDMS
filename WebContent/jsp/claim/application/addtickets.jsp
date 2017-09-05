<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
<%String contextPath = request.getContextPath();%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();//初始化时间控件
	}
</script>
<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
p {
     page-break-after: always;
    }
   </style>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收票新增
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" id="balance_yieldly" name="balance_yieldly"/>
<input type="hidden"  id="dealerid" name="dealerid"/>
<input type="hidden"  id="jude" name="jude"/>
<table align="center" id="otherTableId"  class="table_query" border='1'>
	<tr>
		<td align="center" nowrap="true">运费</td>
		<td align="center"  nowrap="true">
			<input class="middle_txt" size="200px" id="sumcarriage" datatype="0,is_double" decimal="2"    name="sumcarriage"  type="text"/>
			
		</td>
		<td align="center" style="display: none;" nowrap="true">昌河运费</td>
		<td lign="center" style="display: none;" class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="carriage" readonly="readonly" name="carriage" datatype="0,is_double" decimal="2" type="text"/>
		</td>

		<td align="center" style="display: none;" nowrap="true">东安运费</td>
		<td lign="center" style="display: none;" class="table_query_4Col_input" nowrap="nowrap">
			<input class="middle_txt" id="dacarriage"  name="dacarriage" readonly="readonly" datatype="0,is_double" decimal="2" type="text"/>
		</td>
	
		
		<td align="center" nowrap="true">服务站简称</td>
		<td align="center"  nowrap="true">
			<input class="middle_txt" size="200px" id="dealerName" datatype="0,is_null,100" readonly="readonly"   name="dealerName"  type="text"/>
			
		</td>
	</tr>
	<tr>
		<td align="center" nowrap="true">信函总类</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
		<script type="text/javascript">
			genSelBoxExp("letter",9406,"94061001",true,"short_sel","","true",'');
		</script>
			
		</td>
		<td align="center" nowrap="true">发出日期</td>
        <td align="center" class="table_query_4Col_input" nowrap="nowrap">
      	  <input class="short_txt" id="startDate" name="startDate" datatype="0,is_date,10"
            maxlength="10" group="startDate,endDate"/>
         <input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" type="button"/>
		</td>
		<td align="center" nowrap="true">收到日期</td>
		<td align="center" align="left" nowrap="true">
		<input class="short_txt" id="endDate" name="endDate" datatype="0,is_date,10"
            maxlength="10" group="startDate,endDate"/>
         <input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" type="button"/>
		</td>
		<td align="center" nowrap="true">信函情况</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
        <script type="text/javascript">
			genSelBoxExp("lettersf",9407,"94071001",true,"short_sel","","true",'');
		</script>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2" nowrap="true">单据批号</td>
		<td align="center"  colspan="2" nowrap="true">收单数</td>
		<td align="center"  colspan="2" nowrap="true">索赔类型</td>
		<td align="center" colspan="2" nowrap="true">编号</td>
	</tr>
	<tr>
		<td align="center" colspan="2" nowrap="true">
		 <input class="middle_txt"  id="goodsnum" onblur="numapplent(this,1);"    name="goodsnum" type="text"/>
		</td>
		<td align="center"  colspan="2" nowrap="true">&nbsp<input class="middle_txt" id="aplcount1"  name="aplcount" datatype="0,is_digit,6"  readonly="readonly" type="text"/></td>
		<td align="center"  colspan="2" nowrap="true">&nbsp<input class="middle_txt" id="claim_type"  name="claim_type" datatype="1,is_null,100"   type="text"/></td>
		<td align="center" colspan="2" nowrap="true"><input class="middle_txt" id="number1"  name="number"  readonly="readonly" type="text"/></td>
	</tr>
</table>
<table align="center"  class="table_query" border='1'>
    <tr>
		<td align="left" colspan="8" nowrap="true"><span style="color: red;">请先选择经销商再输入单据批号</span></td>
	</tr>
	<tr>
		<td colspan="8" align="center">
			   <input class="normal_btn" type="button"  name="button1"  value="新增" onclick="addRow()"/>	
			  <input class="normal_btn" type="button" disabled="disabled" name="button1" id="queryBtn" value="收单" onclick="addtickets()"/>
			  <INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back/>
		</td>
	</tr>

</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript">
var i = 2;
function addRow()
{
	var ffTable = document.getElementById('otherTableId');
	var tr = ffTable.insertRow(-1);
	var td1 = tr.insertCell(-1);
	var td2 = tr.insertCell(-1);
	var td3 = tr.insertCell(-1);
	var td4 = tr.insertCell(-1);
	td1.colSpan = 2;
	td1.align = "center";
	td1.innerHTML = '<input class="middle_txt"  id="goodsnum" onblur="numapplent(this,'+i+');"  datatype="0,is_null,100"  name="goodsnum" type="text"/>';
	td2.colSpan = 2;
	td2.align = "center";
	td2.innerHTML = '<input class="middle_txt" id="aplcount'+i+'"  name="aplcount" datatype="0,is_digit,6"  readonly="readonly" type="text"/>';
	
	td3.colSpan = 2;
	td3.align = "center";
	td3.innerHTML = '<input class="middle_txt" id="claim_type"  name="claim_type" datatype="0,is_null,100"   type="text"/>';
	
	td4.colSpan = 2;
	td4.align = "center";
	td4.innerHTML = '<input class="middle_txt" id="number'+i+'"  name="number"  readonly="readonly" type="text"/><input class="normal_btn" name="delete" type="button" value ="删除" onclick="javascript:deleteRow02(this)"/>';
	i++;
}
function deleteRow02(obj)
{
	var tabl=document.all['otherTableId'];
	var index = obj.parentElement.parentElement.rowIndex;
	tabl.deleteRow(index); 
	var url='<%=contextPath%>/claim/application/ClaimManualAuditing/delnumapplent.json';
	sendAjax(url,returndel,"fm");
	 
}
function returndel(json)
{
	document.getElementById('carriage').value = json.carriage;
	document.getElementById('dacarriage').value = json.dacarriage;
}

function dodealer(val)
{
   document.getElementById('goodsnum').value = val;
   document.getElementById('goodsnum').disabled = '';
   document.getElementById('queryBtn').disabled = true;
}

var num  = 1;
function numapplent(val,n)
{
		if(val.value == '')
		{
			MyAlert('请输入单据批号 ！');
			return;
		}
		
		var sumcarriage = document.getElementById('sumcarriage').value ;
		if(sumcarriage.length == 0)
		{
			MyAlert('请输入总运费 ！');
			return;
		}
		var k = 0;	
        var goodsnum = document.getElementsByName('goodsnum') ;
        var name = val.value;
        var fal = true;
        for(var j = 0 ;j < goodsnum.length;j++ )
        {
           if(goodsnum[j].value != '')
           {
           		if(name == goodsnum[j].value)
	        	{
	        		k = k + 1;
	        	}
	        	if(name.substring(0,name.length-6) != goodsnum[j].value.substring(0,name.length-6))
	        	{
	        		fal = false;
	        	}
           }
        	
        	
        }
        if(k >= 2)
        {
        	val.value = '';
        	MyAlert('收单号不能重复 ！');
        }else if(! fal)
        {	val.value = '';
        	MyAlert('请输入统一经销商 ！');
        }
        else
        {
        	num = n;
	 		var url='<%=contextPath%>/claim/application/ClaimManualAuditing/numapplent.json?goolname='+val.value;
			sendAjax(url,returnBack,"fm");
        }
		
}

function returnBack(json)
{
	if(json.jude == '0')
	{
	    if(num == 1)
	    {
	      MyAlert('请认真填写年月格式 或者 没有这个月上报的索赔单');
		  document.getElementById('jude').value = '0';
		  document.getElementById('aplcount'+num).value = '';
		  document.getElementById('number'+num).value = '';
	    }else
	    {
	    	 MyAlert('请认真填写年月格式 或者 没有这个月上报的索赔单');
	       document.getElementById('aplcount'+num).value = '';
		  document.getElementById('number'+num).value = '';
	    }
		
	}else
	{
	    if(num == 1)
	    {
	    	document.getElementById('jude').value = '1';
			document.getElementById('dealerName').value = json.dealerName;
			document.getElementById('dealerid').value = json.dealerid;
			document.getElementById('aplcount'+num).value = json.aplcount;
			document.getElementById('number'+num).value = json.number;
			document.getElementById('carriage').value = json.carriage;
			document.getElementById('dacarriage').value = json.dacarriage;
			
			document.getElementById('queryBtn').disabled = '';
	    }else
	    {
	    	document.getElementById('aplcount'+num).value = json.aplcount;
			document.getElementById('number'+num).value = json.number;
			document.getElementById('carriage').value = json.carriage;
			document.getElementById('dacarriage').value = json.dacarriage;
			document.getElementById('queryBtn').disabled = '';
	    }
	    
	}
	
}

function addtickets()
{
	if(!submitForm('fm')) 
	{
	  return false;
	}
	var goodsnum = document.getElementById('goodsnum').value;
	if(goodsnum.length == 0)
	{
		MyAlert('请输入单据批号');
		 return false;
	}
	if(document.getElementById('jude').value == '0')
	{
		MyAlert('请认真输入单据批号 或者没有开票的索赔单');
		return false;
	}
	document.getElementById('queryBtn').disabled = 'disabled';
	var url='<%=contextPath%>/claim/application/ClaimManualAuditing/addwrtickets.json';
    sendAjax(url,returnaddBack,"fm");
}
function  returnaddBack(json)
{
	if(json.ret == 'false'){
		MyAlert('单据批号以存在 ！ ');
		document.getElementById('queryBtn').disabled = false;
	}else 
	{
		MyAlert('收票成功 !');
		if(document.getElementById('balance_yieldly').value == '95411001')
		{
			fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/ticketIntChanghe.do";
			fm.submit();
		}else
		{
			fm.action = "<%=contextPath%>/claim/application/ClaimManualAuditing/ticketIntanglia.do";
			fm.submit();
		}
		
	}
}
</script>
</form>
</body>
</html>