<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.bean.TtAsWrMainPartClaimBean"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
	List<TtAsWrMainPartClaimBean> list= (List<TtAsWrMainPartClaimBean>)request.getAttribute("list");
	TtAsWrMainPartClaimBean beans= (TtAsWrMainPartClaimBean)request.getAttribute("bean");
%>
<script type="text/javascript">    
function init(){

	var type = document.getElementById("claimType").value;
	var comType = document.getElementsByName("hasPart");
	var flag = true;
	for(var i=0;i<comType.length;i++){
		if(type==comType[i].value){
		document.getElementById(comType[i].value).setAttribute("checked","checked"); 
		flag = false;
		}
	}
	if(flag){
		document.getElementById("other").setAttribute("checked","checked"); 
	}
}

function  ffTableId()
	{
	   	var ffTable = document.getElementById('ffTableIds');
	}
	
function printsetup(){    
	// 打印页面设置    
	wb.execwb(8,1);    
} 

function printpreview(){    
	// 打印页面预览      
	wb.execwb(7,1);    
}      
function printit(){    
	if (confirm('确定打印吗？')){
	wb.execwb(6,6)    
	}    
}    

function nowprint() {   
    window.print();   
}   
function window.onbeforeprint() {   
    eval(visble_property_printview + " = \"" + visble_property_false + "\"");   
}   
function window.onafterprint() {   
    eval(visble_property_printview + " = \"" + visble_property_true + "\"");   
}   

function sxsw(){
	WebBrowser.ExecWB(7,1); 
	window.opener=null; 
	window.close();
}

</script>

<style media=print>
    /* 应用这个样式的在打印时隐藏 */
    .Noprint {
     display: none;
    }
   
    /* 应用这个样式的，从那个标签结束开始另算一页，之后在遇到再起一页，以此类推 */
    .PageNext {
     page-break-after: always;
    }
   </style>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>主因件索赔打印</title>
	</head>
<body onload="init();" >
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">昌河汽车主因件索赔单(${yieldly })</font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">
<input type="hidden" name="claimType" id="claimType" value="${bean.claimType }"/>
<center>
  <table class="tab_print" width="700px">
    <tr >
      <td width="12%" nowrap> 编号 </td>
      <td width="32%"  colspan="3" > ${bean.claimNo } </td>
      <td width="10%" nowrap> 上报日期 </td>
      <td width="15%"> ${bean.reportDate } </td>
      <td width="12%"> 批准日期 </td>
      <td width="15%"> ${bean.auditDate} </td>
    </tr>
    <tr >
      <td nowrap> 经销商代码 </td>
      <td width="10%"> ${bean.dealerCode } </td>
      <td width="10%"> 经销商电话 </td>
      <td width="12%"> ${bean.dealerTel } </td>
      <td > 经销商名称 </td>
      <td colspan="3"> ${bean.dealerName } </td>
    </tr>
    <tr >
      <td nowrap> 车型 </td>
      <td > ${bean.modelCode } </td>
      <td > 购买日期 </td>
      <td > ${bean.buyDate } </td>
      <td > 出厂日期 </td>
      <td > ${bean.outDate } </td>
      <td > 行使公里数 </td>
      <td > ${bean.mileage } </td>
    </tr>
    <tr >
      <td > 车主名称 </td>
      <td colspan="3"> ${bean.ownerName } </td>
      <td nowrap> 车主电话 </td>
      <td colspan="3"> ${bean.ownerTel } </td>
    </tr>
    <tr >
      <td > VIN代码 </td>
      <td colspan="3"> ${bean.vin } </td>
      <td nowrap > 发动机号 </td>
      <td colspan="3"> ${bean.engineNo } </td>
    </tr>
    <tr >
      <td nowrap> 主因件名称 </td>
      <td colspan="3"> ${bean.partName } </td>
      <td nowrap> 主因件厂 </td>
      <td colspan="3"> ${bean.supplyName } </td>
    </tr>
    <tr >
      <td nowrap> 有无旧件 </td>
      <td colspan="3"><input type="checkbox" name="hasPart" <%if(list!=null&&list.size()>0)out.print("checked"); %> disabled="disabled" />
        有&nbsp;
        <input type="checkbox" name="hasPart" <%if(list==null || list.size()==0)out.print("checked"); %> disabled="disabled" />
        无 </td>
      <td nowrap> 单据类型 </td>
      <td colspan="3" ><input type="checkbox" name="hasPart" id="10661001" value="10661001" disabled="disabled" />正常
        <input type="checkbox" name="hasPart" id="10661007" value="10661007" disabled="disabled" />售前
        <input type="checkbox" name="hasPart" id="10661009" value="10661009" disabled="disabled" />外派
        <input type="checkbox" name="hasPart" id="10661010" value="10661010" disabled="disabled" />特殊
        <input type="checkbox" name="hasPart" id="10661006" value="10661006" disabled="disabled" />活动
        <input type="checkbox" name="hasPart" id="other" value="10661012" disabled="disabled" />其他 </td>
    </tr>
    <tr>
      <td colspan="8"><table width="100%" style="border-collapse: collapse;">
          <tr >
            <td width="5%" style="border-left-color: white; border-top-color:white; border-bottom-color: white;">具<br>
              体<br>
              问<br>
              题<br>
              及<br>
              状<br>
              态</td>
            <td colspan="7" style="border-top-color:white; border-right-color: white; border-bottom-color: white;"><textarea rows="6" style="width: 95%;"> ${bean.question } </textarea></td>
          </tr>
        </table></td>
    </tr>
    <tr>
    <td colspan="6" width="80%"><table width="100%" style="border-collapse: collapse;">
        <tr>
          <td width="7%" rowspan="16" style="border-left-color: white; border-top-color: white; ">处<br>
            理<br>
            方<br>
            法</td>
          <td width="20%" style="border-top-color: white;">故障件代码</td>
          <td width="40%" style="border-top-color: white;">故障件名称</td>
          <td width="16%" style="border-top-color: white;">故障件数量</td>
          <td width="17%" style="border-top-color: white; border-right-color: white;">处理方式</td>
        </tr>
        <c:set var="pageSize"  value="10000" />
        <c:set var="price"  value="0.0" />
        <c:forEach var="dList" items="${list}" varStatus="status">
          <tr style='${(status.count%pageSize==0) ? "page-break-after:always;":""}'   height="20px">
            <td >${dList.partCode}</td>
            <td >${dList.partName}</td>
            <td >${dList.partNum}</td>
            <td style="border-right-color: white;">${dList.partType}</td>
            <c:set var="price"  value="${dList.partPrice + price}" />
          </tr>
        </c:forEach>
        <% int size = 0;
        if(list!=null&&list.size()>0){
        	size = list.size();
        }else{
        	size=0;
        }
        if(size<15){
			for(int i =0;i<15-size;i++){%>
        <tr    height="20px">
          <td >&nbsp;</td>
          <td >&nbsp;</td>
          <td >&nbsp;</td>
          <td style="border-right-color: white;">&nbsp;</td>
        </tr>
        <%  }
	} %>
        <tr >
          <td rowspan="2" style="border-left-color: white; border-bottom-color: white;"> 费用预算 </td>
          <td > 材料费 </td>
          <td nowrap> <fmt:formatNumber type="number" value="${price }" maxFractionDigits="2"/>  </td>
          <td > 其他费用 </td>
          <td style="border-right-color: white;"></td>
        </tr>
        <tr >
          <td style="border-bottom-color: white;">工时费</td>
          <td nowrap style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;">合计(元) </td>
          <td nowrap style="border-bottom-color: white; border-right-color: white;"> <fmt:formatNumber type="number" value="${price }" maxFractionDigits="2"/></td>
        </tr>
      </table></td>
      <td colspan="2" width="28%" valign="top"><table width="100%" style="border-collapse: collapse;" frame="void">
        <tr>
          <td rowspan="7" width="10%" style="border-left-color: white; border-top-color: white;">&nbsp;技&nbsp;<br/>
            术<br/>
            支<br/>
            持<br/>
            室<br/>
            意<br/>
            见 </td>
          <td width="90%" colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        </tr>
        
        <td colspan="2" style="border-right-color: white; text-align: left;">&nbsp;经办人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室主任：</td>
        </tr>
        <tr>
          <td rowspan="7" width="6%" style="border-left-color: white; border-top-color: white;">&nbsp;技&nbsp;<br/>
            &nbsp;术&nbsp;<br/>
            &nbsp;服&nbsp;<br/>
            &nbsp;务&nbsp;<br/>
            &nbsp;处&nbsp;<br/>
            &nbsp;意&nbsp;<br/>
            &nbsp;见&nbsp;</td>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        </tr>
        
        <td colspan="2" style="border-right-color: white; text-align: left;">&nbsp;主管处长：</td>
        </tr>
        <tr>
          <td rowspan="4" width="6%" style="border-left-color: white; border-top-color: white; border-bottom-color: white;">&nbsp;批&nbsp;<br/>
            &nbsp;准&nbsp;</td>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="border-color: white;">&nbsp;</td>
        </tr>
        <td colspan="2" style="border-right-color: white; border-bottom-color: white; text-align: left;"> &nbsp;批准人：</td>
        </tr>
      </table></td>
    </tr>
  </table>
  <br>
    <p class="Noprint">
		<input type=button name= button_print class="normal_btn" value="打印" onclick ="printit();">
		<input type=button name= button_print class="long_btn" value="打印页面设置" onclick ="printsetup();">
		<input type=button name= button_print class="normal_btn" value="打印预览" onclick ="printpreview();">
    </p>
</center>
</form>
<script language="javascript">    
  
	function printsetup()
	{       
		wb.execwb(8,1);    // 打印页面设置 
	}    
	function printpreview()
	{    
		wb.execwb(7,1);   // 打印页面预览       
	}      
	function printit()    
	{    
		if(confirm('确定打印吗？'))
		{    
			wb.execwb(6,6)    
		}    
	} 
	  $$('.partCode').each(function(e){
		    var partCode = e.value ;
		    var firstPart = e.next().value;
		    var labourAmount = e.next(1).value
		    var labourHour = e.next(2).value
			if(partCode!=firstPart){
				if($('codeId').value==80081001){
					e.up().next(10).innerText = 0;
					e.up().next(9).innerText = 0;
				}else{
					e.up().next(9).innerText = 0;
					e.up().next(8).innerText = 0;
				}
				
			}
			else{
				if($('codeId').value==80081001){
					e.up().next(10).innerText = labourAmount;
					e.up().next(9).innerText = labourHour;
				}else{
					e.up().next(9).innerText = labourAmount;
					e.up().next(8).innerText = labourHour;
				}
			}

		  });
</script> 
</div>
<script type="text/javascript">
	$('topContainer').style.height=document.viewport.getHeight();
</script>
</body>

</html>

