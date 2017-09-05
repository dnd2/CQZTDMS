<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//String code = "SC1022SAN.FAA.MY1";//条形码内容   
	String contextPath = request.getContextPath();
%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<!--media=print 这个属性在打印时有效 有些不想打印出来的分页打印的都可以应用这类样式进行控制 在非打印时是无效的(可从打印预览中看到效果)-->
<title></title>
<style>
td {
	word-wrap: break-word;
	word-break: normal;
}
#checkMsgDiv0{
	display: none;
}
</style>
<style media=print>
.Noprint {
	display: none;
}
p
{
   page-break-after: always;
}
</style>
<script type="text/javascript">
	function PageSetup_Null()   
	{   
	   try{    
	       var Wsh=new ActiveXObject("WScript.Shell");    
	       HKEY_Key="header";    
	       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
	       HKEY_Key="footer";    
	       Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");    
	   }catch(e){}    
	}
	var date =document.getElementById('createD').value;
	var d = date.substr(0,16);
	document.getElementById('createDate').innerHTML=d;
	function printsetup(){    
		// 打印页面设置    
		wb.execwb(8,1);    
	}    
	function printpreview(){    
		// 打印页面预览      
		wb.execwb(7,1);    
	}      
	function printit()    
	{    
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
	var settings = {
	        output:"css",
	        bgColor: "#FFFFFF",
	        color: "#000000",
	
	        barWidth: "1",
	        barHeight: "40",
	        moduleSize: "1",
	        posX: "1",
	        posY: "1",
	        addQuietZone: true,
	        marginHRI: "1",
	        showHRI: true
	   };
</script>
</head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-barcode-2.0.2.min.js"></script>

<%
	List<Map<String, Object>> list_all = (List<Map<String, Object>>) request
			.getAttribute("ls");
%>

<body  topmargin="0px" leftmargin="0px" rightmargin="0px" bottommargin="0px">
<OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height=0 id=wb name=wb width=3></OBJECT>
<div id="kpr" align="center" class="Noprint">     
	<input class="ipt" type=button name= button _print value="打印" onclick ="javascript :printit();">    
	<input class="ipt" type=button name=button _setup value="打印页面设置" onclick =" javascript : printsetup();">    
	<input class="ipt" type=button name=button_show value="打印预览" onclick="javascript:printpreview();">    
	<input class="ipt" type=button name= button _fh value="关闭" onclick =" javascript:window.close();">
</div>
<center>
<%
int k = 1;
	for (int i = 0; i < list_all.size(); i ++) {
		if(i  == 0 || i % 4 == 0)
		{%>
		<div style="width:297mm;height:209mm;padding:2mm 2mm 2mm 2mm;">
		 <table border="0" style="width:100%;height:100%; page-break-after: always;">
	<%	}%>
	 
	   <%  if(i == 0   ||  i % 2 == 0 ){%>
	         <tr>
	    	<%}%>
		   <td style="border:0px solid blue;padding:1px 1px 1px 1px;" >
		<table   
		<%if(((i+1)==list_all.size()&&list_all.size()%4!=0)||((i+2)==list_all.size()&&list_all.size()%4!=0)){ 
			%>
			style="height: 50%;width: 50%"
		<%}else{%>
			style="height: 100%;width: 100%"
		<%} %>
		  class="tab_printss" >
		<tr>
				<td align="center" width="76px" rowspan="3" nowrap="true" ><img src="../../../img/chana/img.jpg"/> </td>
				<td align="center" width="201px;" colspan="3" rowspan="2" nowrap="true"> 三包索赔件标签</td>
				<td  style="border: 0"> &nbsp; </td>
				<td align="center" width="76px" colspan="1"nowrap="true"> 供应商代码</td>
				<td align="center" width="201px;" colspan="3"nowrap="true"> <%=list_all.get(i).get("PRODUCER_CODE")%></td>
			</tr>
			<tr>
				<td  style="border: 0"> &nbsp; </td>
				<td align="center" width="15%" colspan="1"nowrap="true"> 配件编码</td>
				<td align="center" width="35%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;"><%=list_all.get(i).get("DOWN_PART_CODE")%></td>
				<td  style="border: 0"> &nbsp; </td>
			</tr>
			<tr>
				<td align="center" width="35%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;" > 索赔单号:&nbsp;<%=list_all.get(i).get("CLAIM_NO")%></td>
				<td  style="border: 0"> &nbsp; </td>
				<td align="center" width="15%" colspan="1"nowrap="true"> 配件名称</td>
				<td align="center" colspan="3" nowrap="true"> <%=list_all.get(i).get("DOWN_PART_NAME")%></td>
			</tr>
			
			<tr>
				<td align="center" width="15%" colspan="1"  nowrap="true" > 服务站名称</td>
				<td align="center" width="35%" colspan="3" nowrap="true" style="padding:1px 1px 1px 1px;"><%=list_all.get(i).get("DEALER_NAME")%></td>
				<td  style="border: 0"> &nbsp; </td>
				<td align="center" width="15%" colspan="1"nowrap="true"> 配件数量</td>
				<td align="center" width="35%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;">1</td>
				<td  style="border: 0"> &nbsp; </td>
			</tr>
			<tr>
				<td align="center" width="15%" colspan="1"nowrap="true"> 服务站代码</td>
				<td align="center" width="35%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;"><%=list_all.get(i).get("DEALER_CODE")%></td>
				<td  style="border: 0"> &nbsp; </td>
				<td align="center" width="15%" colspan="1" rowspan="2"nowrap="true"> 故障描述</td>
				<td align="center" width="35%" colspan="3" rowspan="2"><%=list_all.get(i).get("F_REMARK") == null ? " "
							: list_all.get(i).get("F_REMARK")%></td>
			</tr>
			
			<tr>
				<td align="center" width="15%" colspan="1" nowrap="true"> 购车日期</td>
				<td align="center" width="15%" colspan="3" nowrap="true" style="padding:1px 1px 1px 1px;"> &nbsp;<%=list_all.get(i).get("PURCHASED_DATE") == null ? " "
									: list_all.get(i).get("PURCHASED_DATE")%></td>
									<td width="2%" style="border: 0"> &nbsp; </td>
			</tr>
			<tr>
				<td align="center" width="15%" colspan="1"nowrap="true"> 维修日期</td>
				<td align="center" width="15%" colspan="1"nowrap="true"><%=list_all.get(i).get("RO_CREATE_DATE")%></td>
				<td align="center" width="15%" colspan="1" nowrap="true"> 行驶里程</td>
				<td align="center" width="15%" colspan="1" nowrap="true" style="padding:1px 1px 1px 1px;"> &nbsp;<%=list_all.get(i).get("IN_MILEAGE")%> </td>
				<td  style="border: 0"> &nbsp </td>
				
				<td align="center" width="15%" colspan="1" rowspan="4"nowrap="true"> 故障原因</td>
				<td align="center" width="35%" colspan="3" rowspan="4"><%=list_all.get(i).get("F_REASON") == null ? " "
							: list_all.get(i).get("F_REASON")%></td>
			</tr>
			<tr>
				<td align="center" width="15%" colspan="1"nowrap="true"> 车型</td>
				<td align="center" width="15%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;">&nbsp;<%=list_all.get(i).get("MODEL_CODE") == null ? ""
										: list_all.get(i).get("MODEL_CODE")%></td>
										<td width="2%" style="border: 0"> &nbsp </td>
			</tr>
			<tr>
			<td align="center" width="15%" colspan="1"nowrap="true"> VIN码</td>
				<td align="center" width="15%" colspan="3"nowrap="true" style="padding:1px 1px 1px 1px;">&nbsp;<%=list_all.get(i).get("VIN") == null ? ""
							: list_all.get(i).get("VIN")%></td>
							<td width="2%" style="border: 0"> &nbsp </td>
			</tr>
			<tr>
				<td align="center" width="15%" colspan="1" nowrap="true"> 发动机号</td>
				<td align="center" width="15%" colspan="3" nowrap="true" style="padding:1px 1px 1px 1px;"> &nbsp;<%=list_all.get(i).get("ENGINE_NO") == null ? ""
							: list_all.get(i).get("ENGINE_NO")%> </td>
							<td  style="border: 0"> &nbsp </td>
			</tr>
		</table>
		</td>
		<%	if( k % 2 == 0 ){%></tr><%}%>
 <% if(k%4 == 0  || k == list_all.size() )
  {%>
    </table>
  </div>
	<%
  }
%>

<%	
   k++;
	}
%>
</center>
</body>
</html>
