<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%> 
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<%@page import="java.text.DecimalFormat" %>
<%
Double price1 = 0.0;//材料费
Double price2 = 0.0; //工时费
Double price3 = 0.0; //辅料费
DecimalFormat   df  =   new  DecimalFormat("##0.00");
List<TtAsWrApplicationExtPO> partLs = (LinkedList<TtAsWrApplicationExtPO>) request.getAttribute("list");
Double otherPrice = (Double)request.getAttribute("otherPrice");
%>
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
</script>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>市场质量问题处理反馈单打印</title>
	</head>
<body onload="init();">
  <object classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="3"></object>
<div id="topContainer" style="" >
<br/>
<center>
  <strong><font size="5px">市场质量问题处理反馈单</font></strong>
</center>
<br/>
<form method="post" name="fm" id="fm">

<input type="hidden" name="claimType" id="claimType" value="${bean.claimType }"/>
<center>
  <table class="tab_print" width="700px">
    <tr >
      <td width="12%" nowrap> 编号 </td>
      <td width="32%"  colspan="3" >${bean.fkNo }</td>
      <td width="10%" nowrap>索赔单号</td>
      <td width="46%" colspan="3" style="border-right-color:white"> ${bean.claimNo } </td>
    </tr>
    <tr >

      <td width="12%" nowrap> 上报日期 </td>
      <td width="32%" colspan="3" > ${bean.appDate } </td>
      <td width="10%"> 批准日期 </td>
      <td width="46%" colspan="3" style="border-right-color:white" > ${bean.agreeDate }</td>
    </tr>
    <tr >
      <td nowrap> 服务站代码 </td>
      <td width="10%">${bean.dealerCode } </td>
      <td width="10%"> 电&nbsp;&nbsp;&nbsp;话 </td>
      <td width="12%">${bean.phone }</td>
      <td > 服务站名称 </td>
      <td colspan="3" style="border-right-color:white"> ${bean.dealerShortname }</td>
    </tr>
    <tr >
      <td nowrap> 车型 </td>
      <td >${bean.modelCode } </td>
      <td > 购买日期 </td>
      <td > ${bean.buyDate }</td>
      <td > 出厂日期 </td>
      <td > ${bean.outDate }</td>
      <td > 行驶公里数 </td>
      <td style="border-right-color:white"> ${bean.inMileage } KM</td>
    </tr>
    <tr >
      <td > 用户名称 </td>
      <td colspan="3">${bean.customerName } </td>
      <td nowrap> 用户电话 </td>
      <td colspan="3" style="border-right-color:white">${bean.customerPhone } </td>
    </tr>
    <tr >
      <td > VIN代码 </td>
      <td colspan="3">${bean.vin } </td>
      <td nowrap > 发动机号 </td>
      <td colspan="3" style="border-right-color:white"> ${bean.engineNo }</td>
    </tr>
    <tr>
      <td nowrap> 有无旧件 </td>
    <td colspan="3"><input type="checkbox" name="hasPart" <%if(partLs.size()>0){out.print("checked");} %> readonly="readonly" onclick="return false"/>
       有&nbsp;
   <input type="checkbox" name="hasPart"  name="hasPart" <%if(partLs==null||partLs.size()<1){out.print("checked");}%>  readonly="readonly" onclick="return false"/> 无 </td>
      <td nowrap> 单据类型 </td>
      <td colspan="3" style="border-right-color:white">
        <input type="checkbox" name="hasPart" id="10661001"  value="10661001" readonly="readonly" onclick="return false"/>正常
        <input type="checkbox" name="hasPart" id="10661007" value="10661007" readonly="readonly" onclick="return false"/>售前
        <input type="checkbox" name="hasPart" id="10661009" value="10661009" readonly="readonly" onclick="return false"/>外派
        <input type="checkbox" name="hasPart" id="10661010" value="10661010" readonly="readonly" onclick="return false"/>特殊
        <input type="checkbox" name="hasPart" id="10661006" value="10661006" readonly="readonly" onclick="return false"/>活动
        <input type="checkbox" name="hasPart" id="other" value="10661012" readonly="readonly" onclick="return false"/>其他
    </tr>
    <tr>
      <td colspan="8"><table width="100%" style="border-collapse: collapse;">
          <tr >
            <td width="8%" style="border-left-color: white; border-top-color:white; border-bottom-color: white;">问<br>
              题<br>
              描<br>
              述<br>
              及<br>
              状<br>
              态</td>
            <td colspan="7" style="border-top-color:white; border-right-color: white; border-bottom-color: white;vertical-align: top;"  align="left"><div>${bean.troubleDesc }</div></td>
          </tr>
        </table></td>
    </tr>
        <tr>
      <td colspan="8">
      <table width="100%" style="border-collapse: collapse;" frame="void">
         <tr>
          <td width="5%" rowspan="4" style="border-left-color: white; border-top-color: white;border-bottom-color: white; ">处<br>
            理<br>
            方<br>
            法</td>
          <td width="10%" style="border-top-color: white;">故障件代码</td>
          <td width="20%" style="border-top-color: white;">故障件名称</td>
          <td width="5%" style="border-top-color: white;">数量</td>
          <td width="5%" style="border-top-color: white;">单价</td>
          <td width="12%" style="border-top-color: white;">故障件厂家</td>
          <td width="5%" style="border-top-color: white;">方式</td>
          <td width="20%" style="border-top-color: white;">故障模式</td>
          <td width="5%" style="border-top-color: white;">标准工时</td>
          <td width="8%" style="border-top-color: white;">材料费</td>
          <td width="7%" style="border-top-color: white;border-right-color:white ">工时费</td>
          </tr>
          <%if(partLs!=null&&partLs.size()>0){
        	price1 += partLs.get(0).getAmount() ;
          	price2 += partLs.get(0).getLabourAmount();
          	price3 = partLs.get(0).getAccessoriesPrice();
          	String code = partLs.get(0).getLabourCode(); %>
        <tr>
          <td ><%=partLs.get(0).getPartCode() %></td>
          <td><%=partLs.get(0).getPartName() %></td>
          <td><%=partLs.get(0).getQuantity() %></td>
          <td><%=partLs.get(0).getPrice() %></td>
          <td><%=partLs.get(0).getSupplyName() %></td>
          <td><%=partLs.get(0).getDealType() %></td>
          <td><%=partLs.get(0).getMalName() %></td>
          <td><%=partLs.get(0).getLabourHours() %></td>
          <td><%=partLs.get(0).getAmount() %></td>
          <td style="border-right-color:white"><%=partLs.get(0).getLabourAmount() %></td>
          </tr>
        <%for(int i=1;i<partLs.size();i++){
    	if(code.equalsIgnoreCase(partLs.get(i).getLabourCode())){
    		price1+=partLs.get(i).getAmount();%>
    	<tr>
          <td ><%=partLs.get(i).getPartCode() %></td>
          <td><%=partLs.get(i).getPartName() %></td>
          <td><%=partLs.get(i).getQuantity() %></td>
          <td><%=partLs.get(i).getPrice() %></td>
          <td><%=partLs.get(i).getSupplyName() %></td>
          <td><%=partLs.get(i).getDealType() %></td>
          <td>--</td>
          <td>--</td>
          <td><%=partLs.get(i).getAmount() %></td>
          <td style="border-right-color:white">--</td>
          </tr>
    	
         <% }else{
        		price1 += partLs.get(i).getAmount() ;
            	price2 += partLs.get(i).getLabourAmount();
         %>
        	     	
        <tr>
          <td ><%=partLs.get(i).getPartCode() %></td>
          <td><%=partLs.get(i).getPartName() %></td>
          <td><%=partLs.get(i).getQuantity() %></td>
          <td><%=partLs.get(i).getPrice() %></td>
          <td><%=partLs.get(i).getSupplyName()%></td>
          <td><%=partLs.get(i).getDealType() %></td>
          <td><%=partLs.get(i).getMalName() %></td>
          <td><%=partLs.get(i).getLabourHours() %></td>
          <td><%=partLs.get(i).getAmount() %></td>
          <td style="border-right-color:white"><%=partLs.get(i).getLabourAmount() %></td>
          </tr>
        	 
        	 
        	 
        <% }}} else{%>
        	
        	 <tr>
          <td style="border-bottom-color: white;" ></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;"></td>
          <td style="border-bottom-color: white;border-right-color:white"></td>
          </tr>
        	
      <%  }%>
         
         
            </table>
            </td>
          </tr>
                    <tr>
          
            <td colspan="8" >
            <c:if test="${length!=0}">
      <table width="100%" style="border-collapse: collapse;" frame="void" >
          <tr>
              <td  class="tdp" nowrap style="border-top-color: white;">行号</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时代码</td>
              <td width="30%" class="tdp" nowrap style="border-top-color: white;">工时名称</td>
              <td width="40%" class="tdp" nowrap style="border-top-color: white;">金额</td>
          </tr>
      <c:forEach items="${claimNoAccessoryList}" var="claimNoAccessoryList" varStatus="i">
          <tr style='' >
	            <td class="tdp">${i.index+1}</td>
	            <td class="tdp">${claimNoAccessoryList.WORKHOUR_CODE}</td>
	            <td class="tdp" >${claimNoAccessoryList.WORKHOUR_NAME}</td>
	            <td class="tdp" align="right">${claimNoAccessoryList.PRICE}&nbsp;</td>
          </tr>
      </c:forEach>
                  <tr>
            <td class="tdp"colspan="2">辅料关联主因件</td>
            <td class="tdp" colspan="2">
            ${claimNoAccessoryList[0].MAIN_PART_CODE}
            </td>
            </tr>
      </table></c:if>
          </td>
          </tr>
          <tr>
          
            <td colspan="8" >
      <table width="100%" style="border-collapse: collapse;" frame="void" >
         <tr>
          <td width="8%"  style="border-left-color: white;border-top-color:white; border-bottom-color: white;">费用
          <td width="8%" style="border-top-color: white;border-bottom-color: white;">材料</td>
          <td width="12%" style="border-top-color: white;border-bottom-color: white;"><%=df.format(price1) %></td>
          <td width="8%" style="border-top-color: white;border-bottom-color: white;">工时</td>
          <td width="12%" style="border-top-color: white;border-bottom-color: white;"><%=df.format(price2) %></td>
          <td width="8%" style="border-top-color: white;border-bottom-color: white;">辅料</td>
          <td width="12%" style="border-top-color: white;border-bottom-color: white;"><%=df.format(price3) %></td>
          <td width="8%" style="border-top-color: white;border-bottom-color: white;">其他</td>
          <td width="8%" style="border-top-color: white;border-bottom-color: white;"><%=df.format(otherPrice) %></td>
          <td width="8%" style="border-top-color: white;border-bottom-color: white;">合计</td>
          <td width="14%" style="border-top-color: white;border-bottom-color: white;border-right-color:white"><%=df.format((price1+price2+price3+otherPrice)) %></td>

          </tr>
          </table>
          </td>
          </tr>
          <tr>
                   <td colspan="8" >
      <table width="100%" style="border-collapse: collapse;" frame="void" >
         <tr>
      <td  width="8%" style="border-top-color: white;border-bottom-color: white;border-left-color:white">结算<br/>依据 </td>
      <td  width="92%"  style="border-top-color: white;border-bottom-color: white;border-right:white" colspan="7"  align="left">&nbsp;<input type="checkbox"  readonly="readonly" onclick="return false"/>
      照片&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
       行驶证复印件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
        身份证复印件&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
        协议
        <br/>
        &nbsp;<input type="checkbox" readonly="readonly" onclick="return false"/>
      故障件不返厂&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
       费用明细清单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
        商品车售前处&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="checkbox"   readonly="readonly" onclick="return false"/>
        其他（请说明）:
</td>
</tr>
</table></td></tr>
<tr >
<td rowspan="2" width="5%" nowrap>审核说明</td>
<td rowspan="2" width="70%" colspan="5" ></td>
<td rowspan="2" width="10%">批准人：</td>
<td  rowspan="2"  width="15%" style="border-right-color:white"></td>
</tr>


  </table>
  <div align="left"  style="width:700px">
    &nbsp; &nbsp;说明：此反馈单由服务站打印三份，一份附在《故障件返厂清单》后面返回索赔室，一份附在结算单后返回结算室，一份服务站自己保存。
  </div>

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
	  
</script> 
</div>
</body>

</html>

