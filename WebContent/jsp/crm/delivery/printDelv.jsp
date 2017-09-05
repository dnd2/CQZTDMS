<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	List  executePlans = (List)request.getAttribute("executePlans");
	List  attachList   = (List)request.getAttribute("attachList");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title class="noneprint">交车信息打印</title>
<script type="text/javascript">
function printsetup(){
    //  打印页面设置
    var wb =$('wb');
    wb.execwb(8,1);   
}
function myPrint(){
	window.print();
}
function printView(){
	var wb =$('wb');
	wb.execwb(7,1);
}
</script>
<style media="print">  
  .noprint { display : none; }  ;
</style>
</head>
<body >
<form method="post" name = "fm" >
<!-- 去掉页眉的控件 -->
 <!-- 打印设置的控件-->
 <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0"></OBJECT>
     <div style="height: 20px;">&nbsp;</div>    
     <center>交车确认</center>
	<table width="99%"  align="center"   style="border: 1px  solid #4F4F4F; border-collapse: collapse" cellspacing="0" cellpadding="0"  bordercolor="#4F4F4F"  >
		
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F; width:10%" rowspan="11"  >事前准备</td>
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%" rowspan="5" colspan="2" >联系客户并确认</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认付款情况（经财务部确认）</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认并通知交车日期、时间与场所</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认交车手续</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认保险证（车辆保险、其他保险）</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认是否去掉座位上的透明塑料罩</td>
		</tr>
		<tr height="25px;">
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%" rowspan="3" colspan="2" >参照车验证</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认登记编号</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认车牌号码</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 确认所有者姓名、车检标签</td>
		</tr>
		<tr height="25px;">
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%" rowspan="3" colspan="2" >参照新车订单</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" /> 检查装备、附件的配置情况</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" />确认订购装备、装载附件、车身颜色 </td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"  colspan="2" > <input type="checkbox" />变速器（AT/MT） </td>
		</tr>
		<tr height="25px;">
			<td align="center"  style="border: 1px  solid #4F4F4F; width:10%" rowspan="11"  >车辆确认</td>
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%"  colspan="2" >销售店确认</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    colspan="2"   > <input type="checkbox" /> 确认实施新车交付前检查（PDI）（    月    日）</td>
		</tr>
		<tr height="25px;">
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%" rowspan="12"  >与客户共同确认</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"   rowspan="3" > 外部</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查车身油漆</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查车身及玻璃有无划痕、污渍</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查大灯、前后灯及功能灯</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查轮胎、车轮有无划痕、污渍</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查后视镜有无划痕、污渍</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查雨刮器是否正常工作</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"   rowspan="8" >内部</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 清洁车辆</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 确认门窗锁开关之动作情形及顺滑度</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 安置车厢内脚垫（未订购，用脚垫纸代替）</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 检查内饰颜色、划痕、污渍</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 确认座椅外观及性能和安全带动作功能</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 确认电动装置能否正常工作</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 确认音响系统</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" />  确认订购装备</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" /> 确认中控防盗系统</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" />  确认汽油剩余量</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" />  确认室内灯、阅读灯、门灯、仪表指示灯</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%"    > <input type="checkbox" />  确认加装装饰配件等</td>
		</tr>
		
	</table>
	<table width="99%"  align="center"   style="border: 1px; border-collapse: collapse;"   >
		<tr height="25px;">
			<td align="center"   style="border: 1px  solid #4F4F4F; width:12%" rowspan="6"  >当天活动</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" colspan="2"   > <input type="checkbox" /> 关于安全的说明</td>
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" rowspan="6"  colspan="2"   > 合影</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" colspan="2"   > <input type="checkbox" /> 关于保证制度的说明</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" colspan="2"   > <input type="checkbox" /> 关于车辆维护保养的说明</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" colspan="2"   > <input type="checkbox" /> 关于保险的说明</td>
		</tr>
		<tr height="25px;">
			<td align="left"  style="border: 1px  solid #4F4F4F;width:10%" colspan="2"   > <input type="checkbox" /> 展厅经理/服务经理的出席、介绍、感谢客户</td>
		</tr>
	</table>
	<table width="99%"  align="center"   style="border: 0px  solid #4F4F4F; border-collapse: collapse" cellspacing="0" cellpadding="0"  bordercolor="#4F4F4F" >
		<tr height="25px;">
			<td colspan="2" style="border: 0px  solid #4F4F4F;width:10%">客户签名：</td>
			<td colspan="2" style="border: 0px  solid #4F4F4F;width:10%">销售经理：</td>
			<td rowspan="2" style="border: 0px  solid #4F4F4F;width:10%">交车日期： 
			  <font style="text-decoration: underline;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>年 
			  <font style="text-decoration: underline;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>月
			  <font style="text-decoration: underline;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>日 
			  <font style="text-decoration: underline;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>时</td>
		</tr>
		<tr height="25px;">
			<td colspan="2" style="border: 0px  solid #4F4F4F;width:10%">合同号码：</td>
			<td colspan="2" style="border: 0px  solid #4F4F4F;width:10%">销售顾问：</td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width=99% border="0" align="center" cellpadding="1" cellspacing="1"  style="background-color: white;" class="table_query" >
		<tr align="center">
			<td >
			<input class="cssbutton  noprint " name="button2" type="button" onclick="printsetup();"  value ="打印设置" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="printView();"  value ="打印预览" />
			<input class="cssbutton  noprint" name="button2" type="button" onclick="myPrint();"   value ="打印" />
			<input class="cssbutton   noprint" name="button2" type="button" onclick="window.close();"  value ="关闭" />
			</td>
		</tr>
	</table>
</form>
</body>
</html>