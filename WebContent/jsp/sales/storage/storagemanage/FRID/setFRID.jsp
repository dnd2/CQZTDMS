<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> RFID维护设置 </title>
<OBJECT style="display:none;" id=TUReader codebase="<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/printUtilCab/UReaderProj.CAB#version=1,0,0,2" classid="clsid:220AC733-63C2-4B1E-AC07-EF0B5147BE17">
</OBJECT>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> RFID维护设置
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
		 <tr class="table_list_row2">
		   <td class="right">产地：</td> 
	  	   <td align="left">${valueMap.AREA_NAME }</td> 
	  	   <td class="right">VIN：</td> 
	  	   <td align="left">${valueMap.VIN }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td class="right">车型名称：</td> 
	  	   <td align="left">${valueMap.MODEL_NAME }</td> 
	  	   <td class="right">车型：</td> 
	  	   <td align="left">${valueMap.MODEL_CODE }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td class="right">配置名称：</td> 
	  	   <td align="left">${valueMap.PACKAGE_NAME }</td> 
	  	   <td class="right">配置代码：</td> 
	  	   <td align="left">${valueMap.PACKAGE_CODE }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td class="right">物料名称：</td> 
	  	   <td align="left">${valueMap.MATERIAL_NAME }</td> 
	  	   <td class="right">物料代码：</td> 
	  	   <td align="left">${valueMap.MATERIAL_CODE }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td class="right">发动机号：</td> 
	  	   <td align="left">${valueMap.ENGINE_NO }</td> 
	  	   <td class="right">生产日期：</td> 
	  	   <td align="left">${valueMap.PRODUCT_DATE }</td> 
	    </tr>
	     <tr class="table_list_row2">
		   <td class="right">入库日期：</td> 
	  	   <td align="left">${valueMap.ORG_STORAGE_DATE }</td> 
	  	   <td class="right">原FRID：</td> 
	  	   <td align="left">${valueMap.HGZ_NO }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td class="right">RFID扫描：</td> 
	  	   <td align="left" colspan="3">
	  	   <input type="text" maxlength="20"  id="hgzNo" name="hgzNo" class="middle_txt" readonly="readonly"/>
	  	   <input type="hidden" id="vehicle_id" name="vehicle_id" class="middle_txt" value="${valueMap.VEHICLE_ID}"/>
	  	   <input type="button" class="normal_btn" onclick="onHgz()" value=" 扫描  " /> 
	  	   </td> 
	    </tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
function openCOM(){
	//if(closeCOM()=="00" || closeCOM()==00){
		closeCOM();
	//}
	return TUReader.Open(6);//打开6,代表COM接口
}
function closeCOM(){
	return TUReader.Close(); //关闭
}
//合格证扫描调用方法
function onHgz(){
	var openReturn=openCOM();
	if(openReturn=='00' || openReturn==00){
		if(typeof(TUReader.Inventory())=='undefined'){
			MyAlert("合格证扫描控件未设置或设置错误！");
			return;
		}
		if(TUReader.Inventory()!="" && TUReader.Inventory()!=null){
			hgzProcess();
		}else{
			MyAlert("扫描件放置错误或该扫描件已破损或连接读取设备有误,读取失败！");
		}
	}else{
		MyAlert("打开接口失败！请检查COM端口是否为COM6或者驱动是否正常安装");
	}
}
//合格证获取值
function hgzProcess() {
	document.all("hgzNo").value= TUReader.Inventory();
	if(TUReader.Inventory()!=""){
		reHgzNo();
	}
	closeCOM();
}
function saveFRID(){
	makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/RFIDManage/updateFRID.json",saveFRIDBack,'fm','queryBtn'); 
}
function saveFRIDBack(json)
{
	if(json.returnValue == 1)
	{
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/RFIDManage/FRIDInit.do";
		fm.submit();
	}
	else
	{
		MyAlert("操作失败！请联系系统管理员！");
	}
}
//判断合格证号
function reHgzNo(){
	var hgz_no = document.getElementById("hgzNo").value;//合格证码
	var url = "<%=request.getContextPath()%>/sales/storage/storagemanage/RFIDManage/FRIDWQuery.json";
    makeCall(url,reHgzNoBack,{hgz_no:hgz_no});	
}
//合格证回调函数
function reHgzNoBack(json){
	if(json.returnValue==0){
		MyConfirm("确认修改FRID号！",saveFRID);
	}else if(json.returnValue==1){
		MyAlert("数据出错,该RFID已有对应的车辆信息");
		document.getElementById("hgzNo").value="";
	}else{
		MyAlert("数据查询失败！");
		document.getElementById("hgzNo").value="";
	}
}
</script>
</body>
</html>
