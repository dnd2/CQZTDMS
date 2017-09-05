<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<!-- created by lishuai103@yahoo.com.cn 20100603 修改配件订单规则 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件订单业务参数修改</title>
<META HTTP-EQUIV="Expires" CONTENT="0">

</head>
<body >
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件订单业务参数</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="dealerId" value="<c:out value="${id}"/>"/>
 <table class="table_query"> 
 	<tr>
 		<th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />经销商列表</th>
 	</tr>
    <tr>
    	<td align="center">经销商代码</td>
    	<td align="center">经销商名称</td>
    </tr>
     <c:forEach items="${list}" var="ak">
      	<tr>
           	<td align="center"><c:out value="${ak.DEALER_CODE}"/></td>
           	<td align="center"><c:out value="${ak.DEALER_NAME}"/></td>
       	</tr>
     </c:forEach>
   </table>
   <br>
  <table class="table_query">
  <tr>
  	  <th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />参数维护</th>
  </tr>
    <tr>
       <td class="table_query_3Col_label_7Letter">订单最大行数：</td>
	   <td>
	   		<input type="text"  name="orderMaxLines" id="orderMaxLines" datatype="0,is_digit,4" class="short_txt"/>
	   </td>
	   <td class="table_query_3Col_label_10Letter">周期内允许上报次数：</td>
	   <td>
			<input type="text"  name="allowSubmitTimes" id="allowSubmitTimes" datatype="0,is_digit,4" class="short_txt" value="" />
	   </td>
       <td class="table_query_3Col_label_36Letter">折扣：</td>
       <td>
       		<input type="text"  name="discountRate" id="discountRate" datatype="0,is_double,6" class="short_txt" value="" /> (0.XX)
       </td>
      </tr>
   </table>  
  <table class="table_query">
  <tbody id="editRule">
    <tr>
      <td class="table_query_3Col_label_6Letter">选择周期类型：</td>
	  <td class="table_query_3Col_input">
	  	<script type="text/javascript">     
 			 genSelBoxExp("cycleType",<%=Constant.CYCLE_TYPE%>,"",false,"short_sel","onchange='showHint(this.value)'","false",'');
		</script>
		<div  style="position:absolute;" id="hint">有效天数：-7 至 -1,1 至 7</div>
	  </td>
    </tr>
    <tr>
      <td align="center">开始日期</td>
      <td align="center">结束日期</td>
      <td align="center">处理日期</td>
      <td align="center">
      	<input class="normal_btn" type="BUTTON" name="add" value="增加一条"  onClick="javascript:addItem();">
      </td>
    </tr>
  </tbody>
  </table>  
  <br>
  <table class="table_edit">
  	  <tr>
  	  	<td align="center">
    		<input class="normal_btn" type="BUTTON" name="ok" value="确定"  onClick="javascript:SubmitForm();">
        	<input class="normal_btn" name="back" type="button" onClick="JavaScript:history.back();" value="取消">
       </td>
  	  </tr>
   </table>
</form>
<script type="text/javascript">
  function showHint(v)
  {
     if(v=='<%=Constant.CYCLE_TYPE_WEEK %>'){
     	hint.innerText="有效天数：-7 至 -1,1 至 7";
     }else if(v=='<%=Constant.CYCLE_TYPE_MONTH %>'){
     	hint.innerText="有效天数：-31 至 -1,1 至 31";
     }else{
     	hint.innerText="有效天数：-92 至 -1,1 至 92";
     }
  }
  function addItem()
	{
		isChanged = true;
		var reason = "";
		var td1 = '<input name=\"START_DATE\" type=\"text\" class=\"short_txt\" size=\"5\">';
		var td2 = '<input name=\"END_DATE\" type=\"text\" class=\"short_txt\" size=\"5\">';
		var td3 = '<input name=\"HANDLE_DATE\" type=\"text\" class=\"short_txt\" size=\"5\">';
		var td4 = '<input type=button value=\"删除\" class=\"normal_btn\" name=\"remain\" onclick=\"javascript:delItem(this)\">';
		
		var aTr = document.createElement("TR");	
		editRule.appendChild(aTr);
	
		var aTD1 = document.createElement("<TD class='zi' bgcolor='EAEAEA'></TD>");
		var aTD2 = document.createElement("<TD class='zi' bgcolor='EAEAEA'></TD>");
		var aTD3 = document.createElement("<TD class='zi' bgcolor='EAEAEA'></TD>");
		var aTD4 = document.createElement("<TD class='zi' bgcolor='EAEAEA'></TD>");
		aTr.appendChild(aTD1);
		aTr.appendChild(aTD2);
		aTr.appendChild(aTD3);
		aTr.appendChild(aTD4);

		aTD1.innerHTML=td1;
		aTD1.align = "center";
		aTD2.innerHTML=td2;
		aTD2.align = "center";
		aTD3.innerHTML=td3;
		aTD3.align = "center";
		aTD4.innerHTML=td4;
		aTD4.align = "center";
	}
	
	function delItem(obj)
	{
		var i = obj.parentElement.parentElement.rowIndex;
		editRule.deleteRow(i);
	}	
	
	function SubmitForm()
	{	
		submitForm(fm);
		if(!uncheckDate())
		{
		     return false;
		}
		if(!confirm("确定修改配件订单业务参数信息?"))
		{
		  return false;
		}						
		fm.action = "<%=contextPath%>/partsmanage/infoSearch/PartsmanageOrderParamInfo/udateOrderParamDo.do";
		fm.submit();
	}
	
	function checkDate(){
		var startDate = document.fm.START_DATE;
		var endDate = document.fm.END_DATE;
		var handleDate = document.fm.HANDLE_DATE;
		//如果是数组
		if(!startDate){
		  MyAlert("没有指定日期！");
		  return false;
		}
		//MyAlert("=======================>"+startDate.length);
		if(startDate.length){
		  for(var i=0;i<startDate.length;i++){
		      //MyAlert(startDate[i].value+"    "+endDate[i].value+"   "+handleDate[i].value);
			  if(!dateValidation(startDate[i],endDate[i],handleDate[i])){
			     return false;
			  }
			  //判断下一行的开始日期要晚于上一行的结束日期
		     if(i>0){
		        if(startDate[i].value-0<endDate[i-1].value-0){
		             //MyAlert(startDate[i].value-0+"       "+endDate[i-1].value-0);
				     MyMyAlert(startDate[i],"第"+(i+1)+"行开始日期不能小于第"+i+"行结束日期");
				     return false;
		        }
		     }
		   }
		 }else{
		//周期类型选择-周度
		  //MyAlert(startDate.value+"    "+endDate.value+"   "+handleDate.value);
		  if(!dateValidation(startDate,endDate,handleDate)){
		     return false;
		  }
		}
		return true;
    }
  //判断日期是否合法，交叉重复
	function uncheckDate(){
	var startDate = document.fm.START_DATE;
	var endDate = document.fm.END_DATE;
	var handleDate = document.fm.HANDLE_DATE;
	//如果是数组
	if(startDate){
	  
	//MyAlert("=======================>"+startDate.length);
	if(startDate.length){
	  for(var i=0;i<startDate.length;i++){
	      //MyAlert(startDate[i].value+"    "+endDate[i].value+"   "+handleDate[i].value);
		  if(!dateValidation(startDate[i],endDate[i],handleDate[i])){
		     return false;
		  }
		  //判断下一行的开始日期要晚于上一行的结束日期
	     if(i>0){
	        if(startDate[i].value-0<=endDate[i-1].value-0){
	             //MyAlert(startDate[i].value-0+"       "+endDate[i-1].value-0);
			     MyAlert(startDate[i],"第"+(i+1)+"行开始日期必须大于第"+i+"行结束日期");
			     return false;
	        }
	     }
	   }
	 }else{
	//周期类型选择-周度
	  //MyAlert(startDate.value+"    "+endDate.value+"   "+handleDate.value);
	  if(!dateValidation(startDate,endDate,handleDate)){
	     return false;
	  }
	}
	}
	return true;
  }
  
  function dateValidation(start,end,handle){
	var cycle = document.fm.cycleType;
	var week = 7;
	var month = 31;
	var quarter = 92;
	//判断选择周期类型
	  if(cycle.value=='<%=Constant.CYCLE_TYPE_WEEK %>'){
	    cycle = week;
	  }else if(cycle.value=='<%=Constant.CYCLE_TYPE_MONTH %>'){
	  	cycle = month;
	  }else{
	  	cycle = quarter;
	  }
	  //开始判断	
	//MyAlert(startDate.value+"    "+endDate.value+"  "+handleDate.value);
	//MyAlert(start.value);
	  if(!isNumeric(start)){
			MyAlert(start,"所填写值必须为整数");
			return false;
		}else{
		  if(start.value==0){
			MyAlert(start,"所填写值[0]不是一个有效的天数");
			return false;
		  }
		}
	  if(!isNumeric(end)){
			MyAlert(end,"所填写值必须为整数");
			return false;
		}else{
		  if(end.value==0){
			MyAlert(end,"所填写值[0]不是一个有效的天数");
			return false;
		  }
		}
	  if(!isNumeric(handle)){
			MyAlert(handle,"所填写值必须为整数");
			return false;
		}else{
		  if(handle.value==0){
			MyAlert(handle,"所填写值[0]不是一个有效的天数");
			return false;
		  }
		}
	  if(start.value-0<-1*cycle){
	     MyAlert(start,"开始日期不能小于周期最小日期["+-1*cycle+"]");
	     return false;
	  }
	  if(end.value-0>cycle-0){
	     MyAlert(end,"结束日期不能大于周期最大日期["+cycle+"]");
	     return false;
	  }
	  if(start.value-0>end.value-0){
	    //MyAlert(start.value+"==="+end.value);
	     MyAlert(end,"开始日期不能大于结束日期");
	     return false;
	  }
	  if(handle.value-0>cycle-0){
	     MyAlert(handle,"处理日期不能大于周期最大日期["+cycle+"]");
	     return false;
	  }
	  if(handle.value-0<-1*cycle-0){
	     MyAlert(handle,"处理日期不能小于周期最小日期["+-1*cycle+"]");
	     return false;
	  }
	  if(handle.value-0>start.value-0&&handle.value-0<end.value-0){
	     MyAlert(handle,"处理日期不能在开始和结束日期内");
	     return false;
	  }
	  return true;
  }
  
  function isNumeric(obj) {
	var reg=/^(-|\+)?\d+(\.\d+)?$/
	if(reg.test(obj.value)){
		return true;
	}else{
		return false;
	}
}
  function MyAlert(theText,notice){
   MyAlert(notice);
   theText.focus();
   theText.select();
}
  </script>
</body>
</html>