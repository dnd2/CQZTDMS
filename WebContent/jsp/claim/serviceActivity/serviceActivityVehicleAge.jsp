<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动设定-车龄定义列表</title>
<%
String contextPath = request.getContextPath();
%>
<%
			//List<TtAsActivityBean> VehicleAgeList = (List<TtAsActivityBean>) request.getAttribute("VehicleAgeList");
%>
<script language="JavaScript" type="text/javascript">
    //日历控件
		function doInit()
		{
		   loadcalendar();
		}
    //增加车龄信息
		function addAges(){
		    var activityId=document.getElementById("activityId").value ;
			var str="";
			var dataType1="";
			var dataType2="";
			var tab = document.getElementsByName("dateType");
			var l = tab.length;	
			if(l>0)
			{
					for(var i=0;i<l;i++){ 
						   if(str)
							{  
									str +=","+ tab[i].value;
							}else{
									str += tab[i].value;
							} 
					}
				 var beforeVehicle='<%=request.getAttribute("beforeVehicle") %>';//如果服务活动车辆范围为售前车，那么车龄类型只能是生产日期
	               dataType1=str.split(",");
	              // MyAlert(dataType1);
	               if(dataType1.length>2)
	               {
		               for(var j=0;j<dataType1.length;j++){
		               if(beforeVehicle=='11321001'&&dataType1[j]=='10801001'){
								MyDivAlert("如果服务活动车辆范围为售前车，那么车龄类型只能是生产日期");
						    	return false;
							}
			                for(var k=j+1;k<dataType1.length;k++){
			                
				                if(dataType1[j]==dataType1[k]){
						              if(!checkDateC(j,k)){//调用方法，判断日期是否交叉
								             return false;
						               	}
				               }
			                }
		               }
	               }if(dataType1.length=2){
		               for(var j=0;j<dataType1.length-1;j++){
		               
			                for(var k=j+1;k<dataType1.length;k++){
			                if(beforeVehicle=='11321001'&&dataType1[k]=='10801001'){
							 MyDivAlert("如果服务活动车辆范围为售前车，那么车龄类型只能是生产日期");
					  		 return false;
							}
				                if(dataType1[j]==dataType1[k]){
						              if(!checkDateC(j,k)){//调用方法，判断日期是否交叉
								             return false;
						               	}
				               }
			                }
		               }
	               }if(dataType1.length=1){
	                if(beforeVehicle=='11321001'&&dataType1[0]=='10801001'){
							 MyDivAlert("如果服务活动车辆范围为售前车，那么车龄类型只能是生产日期");
					  		 return false;
							}
	               }
               }else{
	               MyDivAlert("请填写车龄！");
	               return false;
               }        
			if(!submitForm('FRM')) {//表单校验
				return false;
			}
			 MyDivConfirm("是否确认增加？",sures,[activityId]);
			}
	function sures(activityId){
	         document.FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityManageVehicleAgeOption.do?activityId=" + activityId;
			 document.FRM.submit();
	}
</script>
</head>

<body>
<div class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
</div>
<form name="FRM" id="FRM">
<input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>" />
<table width=95% border=0>
	<tr>
		<td>&nbsp;</td>
		<td align=right>
			<input type=button value='确定' class="normal_btn" name="comfig" onclick="addAges();" />
		    <input type=button value='关闭' class="normal_btn" name="guanbi" onclick="parent._hide();"/>
	    </td>
	</tr>
</table>
<table width=95% border=0 class="table_list" style="border-bottom: 1px solid #DAE0EE" id="myTable">
   <tr>
		<th colspan="4" align="left">
			<img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 车龄定义列表
		</th>
   </tr>
	<tr bgcolor="#55CA9E">
		<th width="25%">起始日期</th>
		<th width="24%">截止日期</th>
		<th width="36%">类型</TH>
		<th width="15%">
			<input name="button3" type="button" class="normal_btn" onclick="javascript:addItem()" value="新增" />
		</th>
	</tr>
	<tbody id="editRule">
	   <%
	      int j=11111111;
		  int k=22222222;
	   %>
		<c:forEach var="VehicleAgeList" items="${VehicleAgeList}">
		   <%
		    int m=j++;
		    int n=k++;
		   %>
			<tr class="table_list_row1">
				<td>
						<input name="startdate" id="<%=m%>" value="${VehicleAgeList.saleDateStart}" type="text" class="short_txt" datatype="0,is_date,10" group="<%=m%>,<%=n%>" hasbtn="true" callFunction="showcalendar(event, '<%=m%>', false);" />
				</td>
				<td>
				  		<input name="enddate" id="<%=n%>" value="${VehicleAgeList.saleDateEnd}" type="text" class="short_txt" datatype="0,is_date,10" group="<%=m%>,<%=n%>" hasbtn="true" callFunction="showcalendar(event, '<%=n%>', false);" />
				</td>
				<td>
					<script type="text/javascript">
	   					genSelBoxExp("dateType",<%=Constant.SERVICEACTIVITY_DATE_TYPE%>,"${VehicleAgeList.dateType}",false,"short_sel","","false",'');
	  				</script>
  		        </td>
				<td>
				        <input type=button value="删除" class="normal_btn" style="" name="remain" onclick="javascript:delItem(this)" />
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script language="JavaScript" type="text/javascript">                      
    var id1 = <%= j %>-1;
	var id2 = <%= k %>-1;		
	var id3 = 33333333-0;	         
	 function addItem(){
			id1 = id1 +1;
			id2 = id2 +1 ;
			id3 = id3 + 1;                         
			var td1 = '<input name=\"startdate\" class=\"short_txt\" id=\"'+id1+'\" type=\"text\" datatype=\"0,is_date,10\" group=\"'+id1+','+id2+'\" hasbtn=\"true\" callFunction=\"showcalendar(event, \''+id1+'\', false);\">';
			var td2 = '<input name=\"enddate\" class=\"short_txt\" id=\"'+id2+'\" type=\"text\" datatype=\"0,is_date,10\" group=\"'+id1+','+id2+'\" hasbtn=\"true\" callFunction=\"showcalendar(event, \''+id2+'\', false);\">';	
			//var td3 = '<input name=\"enddate1\" class=\"short_txt\" size=\"20\" id=\"'+id3+'\" readonly>&nbsp;<img src=\"/uat.service\/images\/cal_date_picker.gif\"  id=\"yy'+id3+'\"  title=\"选择日期\" width=\"15\" height=\"12\" style=\"cursor: pointer;\">';
			var td4 = '<input type=button value=\"删除\" class=\"normal_btn\" style=\"width=48\" name=\"remain\" onclick=\"javascript:delItem(this)\">';	
        
			var aTr = document.createElement("<tr bgcolor=\'#DEFEDC\'><\/tr>");
			editRule.appendChild(aTr);

			var aTD1 = document.createElement("td");
			var aTD2 = document.createElement("td");
			var aTD3 = document.createElement("td");
			var aTD4 = document.createElement("td");
			aTr.appendChild(aTD1);
			aTD1.innerHTML=td1;
			aTr.appendChild(aTD2);
			aTD2.innerHTML=td2;
			aTr.appendChild(aTD3);
			aTD3.innerHTML=genSelBoxStrExp("dateType",<%=Constant.SERVICEACTIVITY_DATE_TYPE%>,"",false,"short_sel","","false",'');
			aTr.appendChild(aTD4);
			aTD4.innerHTML=td4;
			setMustStyle([document.getElementById(id1),document.getElementById(id2)]);//input 增加×号
			setBtnStyle([document.getElementById(id1),document.getElementById(id2)]);//添加日历控件图片
			addListener();//form表达校验
			}
		
	function delItem(obj){
	MyDivConfirm("是否确认删除？",del,[obj]);
	}
	function del(obj){
		var i = obj.parentElement.parentElement.rowIndex;
		editRule.deleteRow(i-2);
		}
	
	function checkDateC(j,k){
		var array1=new Array();
		var array2=new Array();
		var start;
		var end;
		var t1=document.getElementById(parseInt(11111111)+j).value;
		var e1=document.getElementById(parseInt(22222222)+j).value;
		var t2=document.getElementById(parseInt(11111111)+k).value;
		var e2=document.getElementById(parseInt(22222222)+k).value;
    	if(((t1>=t2)&&(t1<=e2))||((e1>=t2)&&(e1<=e2))){
			MyDivAlert("时间不能交叉！");
			return false;
		}
		return true;	
	}	
</script>
</form>
</body>
</html>