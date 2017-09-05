<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.po.TmPtPartBasePO"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
TmPtPartBasePO map = (TmPtPartBasePO)request.getAttribute("SELMAP");
if(map.getPartWarType() == Integer.parseInt( Constant.PART_WR_TYPE_1))
{
	map.setWrMonths(3);
	map.setWrMileage(Double.parseDouble("5000"));
}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>关注件预授权维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body onload="doCusChange('<%=map.getPartWarType()%>')">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
售后服务管理&gt;索赔预授权&gt;关注件预授权修改</div>
<form name='fm' id='fm'><input type="hidden" name="ID" id="ID"
	value='<%=map.getPartId()==null?"":map.getPartId()%>' />
	<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
	<input type="hidden" name="partCode" id="partCode" value='<%=map.getPartCode()==null?"":map.getPartCode()%>'/>
	<input type="hidden" name="partWarType" id="partWarType" value='<%=map.getPartWarType()==null?"":map.getPartWarType()%>'/>
<table class="table_query">
	<tr>
		<td style="text-align: right">配件代码：
		</td>
		<td><%=map.getPartCode()==null?"":map.getPartCode()%></td>
		<td style="text-align: right">配件名称：
		</td>
		<td><%=map.getPartName()==null?"":map.getPartName()%></td>
		<td style="text-align: right"">配件三包类型：
		</td>

		<td><script type="text/javascript">
				genSelBoxExp("PART_WAR_TYPE",<%=Constant.PART_WR_TYPE%>,"<%=map.getPartWarType()%>",false,"","","false",'');
	       		</script>
	   </td>
	   <td></td>
	   <td></td>
	</tr>
	<tr id="trdis1" style="display: none">
		<td style="text-align: right">三包月份：
		</td>
		<td>24<input type="hidden" name="wr_months1" value="24"/></td>
		<td style="text-align: right">三包里程：
		</td>
		<td>50000<input type="hidden" name="wr_mileage1" value="50000"/></td>
	   <td></td>
	   <td></td>
	   <td></td>
	   <td></td>
	</tr>
	
	<tr id="trdis2" style="display: none">
		<td style="text-align: right">三包月份：
		</td>
		<td><input type="text" id="wr_months2" name="wr_months2" class="middle_txt" datatype="0,is_digit,4" value="<%= map.getWrMonths()==null?"3":map.getWrMonths()%>"/>
		</td>
		<td style="text-align: right">三包里程：
		</td>
		<td><input type="text" id="wr_mileage2" name="wr_mileage2" class="middle_txt" datatype="0,is_double" decimal="2" value="<%=map.getWrMileage()==null?"5000":map.getWrMileage()%>"/>
		</td>
		<td><input type="hidden" name="typename" id="typename" value=""/></td>
	    <td></td>
	    <td></td>
	    <td></td>
	</tr>
	
	
	<tr id="trdis3" style="display: none">
		<td style="text-align: right">三包月份：
		</td>
		<td>24<input type="hidden" name="wr_months3" value="24"/></td>
		<td style="text-align: right">三包里程：
		</td>
		<td>50000<input type="hidden" name="wr_mileage3" value="50000"/></td>
		
		<td style="text-align: right">关注件天数：
		</td>
		<td>60<input type="hidden" name="att_days" value="60"/></td>
		<td style="text-align: right">关注件里程：
		</td>
		<td>3000<input type="hidden" name="att_miliage" value="3000"/></td>
	</tr>
	
	
	
	<tr>
		<td colspan="6" style="text-align:center"><input name="ok" type="button"
			class="normal_btn" id="commitBtn" value="确定"
			onclick="checkFormUpdate();" /> <input name="back" type="button"
			class="normal_btn" value="返回" onclick="_hide();" /></td>
	</tr>
</table>
</form>
</div>
<script>
  function doCusChange(value)
  {
  	if(value == '<%=Constant.PART_WR_TYPE_1 %>')
  	{
  		document.getElementById('typename').value = '1';
  		document.getElementById('trdis1').style.display = '';
  		document.getElementById('trdis2').style.display = 'none';
  		document.getElementById('trdis3').style.display = 'none';
  	}else if(value == '<%=Constant.PART_WR_TYPE_2 %>')
  	{
  		document.getElementById('typename').value = '2';
  		document.getElementById('trdis2').style.display = '';
  		document.getElementById('trdis1').style.display = 'none';
  		document.getElementById('trdis3').style.display = 'none';
  	}
  }
	
	//表单提交前的验证：
	function checkFormUpdate(){
		MyConfirm("是否确认修改？",checkForm);
	}
	//表单提交方法：
	function checkForm(){
	
			document.getElementById("commitBtn").disabled = true ;
			makeNomalFormCall('<%=contextPath%>/repairOrder/RoMaintainMain/foucsPartUpdate.json',showResult,'fm');	
	}
	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("修改成功！");
			__parent().__extQuery__(1) ;
			_hide() ;
		}else{
			MyAlert("修改失败，请联系管理员！");
		}
	}
	//页面跳转：
	function sendPage(){
		window.location.href = "<%=contextPath%>/repairOrder/RoMaintainMain/foucsLabourSet.do";
	}	
</script>
</body>
</html>