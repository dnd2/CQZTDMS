<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
    List yearList=(List)request.getAttribute("yearList");
    List serlist=(List)request.getAttribute("serlist");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script language="javascript">
		function getPlanVer(){
		    var year=document.getElementById("year").value;
		    var month=document.getElementById("month").value;
		    var arr=document.getElementsByName("chngType");
		   // var areaId=document.getElementById("buss_area").value;
		   // var planType=document.getElementById("planType").value;
		    var chngType;
		    for (var i=0;i<arr.length;i++){
		    	if(arr[i].checked){
		    		chngType=arr[i].value;
		    	}
		    }
			var url = "<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/selectMaxPlanVer.json";
			makeCall(url,showPlanVer,{year:year,month:month,chngType:chngType,planType:null});
		}
		function showPlanVer(json){
			var obj1 = document.getElementById("plan_ver");
			obj1.options.length = 0;
			var maxVer=json.maxVer;
			var j=0;
			for(var i=maxVer;i>0;i--){
				obj1.options[j]=new Option(i, i);
				j++;
			}
	    }
	    function checkOrg(){
	    	document.getElementById("obtn").disabled=false;
	    	document.getElementById("dbtn").disabled=true;
	    	getPlanVer();
	    }
	    function checkDealer(){
	    	document.getElementById("obtn").disabled=true;
	    	document.getElementById("dbtn").disabled=false;
	    	getPlanVer();
	    }
	    function clrTxt(txtId){
	    	document.getElementById(txtId).value="";
	    }
	    
	   //月份下拉框默认选中 
	    function show(){  
	        var m = document.getElementById("month");  
	        var d = new Date(); 
	        m.options[d.getMonth()].selected="selected";
	   } 
	   
	    var myPage;
		//查询路径
		var url ;		
		var title = null;
		var columns;
		
		function search1Submit() {
			document.getElementById("dthid").value=1;
		    url="<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/oemMonthPlanTotalSearch.json";
			var chngType=getRdValue();
			if(chngType==1){
				columns = [
					{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
					{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'}
					//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue}
			      ];
			}else{
				columns = [
					{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'}
					//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue}
			      ];
			}
			getAreaGroup();
			showDbtn();
			__extQuery__(1);
		}
		
		function search2Submit(){
		    document.getElementById("dthid").value=2;
			url="<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/oemMonthPlanDetailSearch.json";
			var chngType=getRdValue();
			if(chngType==1){
			    columns = columns3;
			}else{
				columns = columns4;
			}
			showDbtn();
			__extQuery__(1);
		}
		
		function getAreaGroup(){
	        var areaId=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/selectAreaGroup.json";
			makeCall(url,showColumn1,{buss_area:areaId});
		}
		//getAreaGroup();
		
		function getAreaGroup(){
		    //  var areaId=document.getElementById("buss_area").value;
			var url = "<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/selectAreaGroup.json";
			makeCall(url,showColumn1,{});
		}
			      
		
		function showColumn1(json){
			      for(var i=0;i<json.serList.length;i++){
						columns.push({header: json.serList[i].GROUP_NAME, dataIndex: 'S'+i, align:'center'});
				  }
					    columns.push({header: "合计", dataIndex: 'SALE_AMOUNT', align:'center'});
		}
		
	    
	    var columns3=[
					{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
					{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
					//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue},
					{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "月度任务", dataIndex: 'SALE_AMOUNT', align:'center'}
			      ];
	   var columns4 = [
					{header: "区域代码", dataIndex: 'ORG_CODE', align:'center'},
					{header: "区域名称", dataIndex: 'ORG_NAME', align:'center'},
					{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
					{header: "经销商", dataIndex: 'DEALER_SHORTNAME', align:'center'},
					//{header: "任务类型", dataIndex: 'PLAN_TYPE', align:'center',renderer:getItemValue},
					{header: "车系代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "月度任务", dataIndex: 'SALE_AMOUNT', align:'center'}
			      ];
	   
	   function getRdValue(){
	   		var rd1=document.getElementById("rd1");
	   		// var rd2=document.getElementById("rd2");
	   		if (rd1 ==null || rd1 == 'null' || rd1 == 'undefind') {
	   			return 1;
	   		} else {
	   			if(rd1.checked){
	   	   			return 1;
	   	   		}else{
	   	   			return 2;
	   	   		}
	   		}
	   }
	  function showDbtn(){
	  	  document.getElementById("dbtn").style.display='inline';
	  }
	    //下载
		function doExport(){
	    	var fm = document.getElementById("fm");
			fm.action= "<%=contextPath%>/sales/planmanage/MonthTarget/OemMonthPlanSearch/yearPlanSearchExport.json";
			fm.submit();
		}
	    
	    $(document).ready(function(){
	    	show();
	   		getPlanVer(); 
	    });
	</script>
<title>月度任务查询</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>月度任务>月度任务查询</div>
<form method="post" name="fm"  id="fm">
	<div class="form-panel">
		<h2>月度任务查询</h2>
		<div class="form-body">
		   <table class="table_query" >
		      <tr>
			      <td class="right" >选择月份：</td>
			      <td>
			        <select name="year" id="year" onchange="getPlanVer();" class="u-select" style="width: 20px;">
			        	<%
			        		for(int i=0;i<yearList.size();i++){
			        			String year=(String)yearList.get(i);
			        	%>
			        			<option value="<%=year %>"><%=year %></option>
			        	<%
			        		}
			        	%>
			        </select> 年
			        <select name="month"  id="month" onchange="getPlanVer();" class="u-select" style="width: 20px;">
			        	 <c:forEach var="month" begin="1" end="12" step="1" >  
					        <option value="${month}">${month}</option>  
					     </c:forEach> 
			        </select> 月
			      </td>
			  </tr>    
		      <tr>
		          <td class="right">版本号：</td>
		          <td>
		          	<select name="plan_ver" id="plan_ver" class="u-select" style="width: 20px;"></select>
		          </td>
		     </tr>
		     <tr>
		     <%-- <td align="right"> 业务范围：</td>
		     <td>
		      <select name="buss_area" id="buss_area" onchange="getPlanVer();">
			       <c:forEach items="${areaBusList}" var="areaBusList" >
			       		<option value="${areaBusList.AREA_ID }">${areaBusList.AREA_NAME }</option>
				   </c:forEach>
		      </select>
		    </td> --%>
		  </tr>
		<%--  增加任务类型--%>
		   <tr style="display: none">
		     <td class="right"> 任务类型：</td>
		     <td>
				<script type="text/javascript">
				    genSelBoxExp("planType",<%=Constant.PLAN_TYPE%>,"",false,"","onchange='getPlanVer()'","false",'');
				</script>	
		    </td>
		  </tr>    
		     <!--
		     <tr>
		          <td align="right">版本描述：</td>
		          <td>
		             <input type="text" name="plan_desc" size="20" value="" />
		          </td>
		     </tr>
		     
		     -->
		       <%-- <c:if test="${dutyType==10431001}">
		       <tr id="org" style="display: ${style }">
		         <td align="right">
		         	<input type="radio" id="rd2" name=chngType value="2"  onclick="checkOrg();"/>选择区域：
		         </td>
		         <td>
		            <input type="text" id="orgCode" name="orgCode" value="" size="15" />
		           
						<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','' ,'true','${orgId}');" disabled="disabled"/>
					
					<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
				 </td>
		     </tr>
		      </c:if> --%>
		     <c:if test="${userType eq '10021001' }">
			     <tr id="dealer">
			         <td class="right">
				          <input type="radio" id="rd1"  name="chngType" value="1"  checked="checked"  onclick="checkDealer();"  />选择经销商：
			         </td>
			         <td>
			            <input type="text"  name="dealerCode" size="15"  id="dealerCode" readonly="readonly"  class="middle_txt" style="cursor: pointer;"
			            	onclick="showOrgDealer('dealerCode', 'dealerId', 'false', '', '<%= Constant.DEALER_LEVEL_01 %>', 'true', '<%=Constant.DEALER_TYPE_DVS %>', 'dealerName');"/>
			            <input type="hidden"  name="dealerId" size="15"  id="dealerId" readonly="readonly"/>
			            <input type="hidden"  name="dealerName" size="15"  id="dealerName" readonly="readonly"/>
			 <%--	<input name="dbtn" id="dbtn" class="mini_btn" type="button" value="&hellip;" onclick="showOrgDealer3('dealerCode', '', 'true', '${orgId}', 'true');" />-->
			             <c:if test="${dutyType==10431004}">
			             <input name="dbtn" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCode','','true', '${orgId}');" />
			            </c:if>
			            <c:if test="${dutyType!=10431004}">
			            	<input name="dbtn" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer3('dealerCode','','true', '${orgId}','true');" />
			        	</c:if> --%>
			        	<input  type="button" value="清空" onclick="clrTxt('dealerCode');" class="u-button u-reset" />
			         </td>
			     </tr>
		     </c:if> 
		     <tr>
		         <td colspan="2" class="center">
		             <input name="bt1" type="button"  onclick="search1Submit()" value="汇总查询"  class="u-button u-query"/>
		             <input name="bt2" type=button onclick="search2Submit();" value="明细查询"  class="u-button u-query"/>
		             <input name="donwtype" id="dthid" type="hidden"  value="" />
		             <input name="bt3" id="dbtn"  type="button"  onclick="doExport();" value="下载"  class="u-button u-submit" />
		         </td>
		         <td class="right">
		             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
		         </td>
		     </tr>
		   </table>
		    </div>
  			</div>
		  <!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		  <!--分页 end --> 
</form>
</div>
</body>
</html>
