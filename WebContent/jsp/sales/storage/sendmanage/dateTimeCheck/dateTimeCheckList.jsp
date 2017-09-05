<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
List list =(List)request.getAttribute("list_logi");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 物流运输考核管理</title>
<style type="text/css"> 
.x-grid-cell.user-online 
{ 
background-color: #9fc; 
} 
.x-grid-cell.user-offline 
{ 
background-color: blue; 
} 
</style>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 物流运输考核管理
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab">
  <tr class="csstr" align="center" id="o01">
	  <td class="right">选择经销商：</td>
		<td align="left">
      		<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>','dealerName');" value="..." />
    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
    		<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
		</td>
	     <td class="right" nowrap="true">日期：</td>
		<td align="left" nowrap="true">
			<input name="ASS_STARTDATE" type="text" maxlength="20"  class="middle_txt" id="ASS_STARTDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ASS_STARTDATE', false);" />  	
             &nbsp;至&nbsp;
             <input name="ASS_ENDDATE" type="text" maxlength="20"  class="middle_txt" id="ASS_ENDDATE" readonly="readonly"/> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'ASS_ENDDATE', false);" /> 
		</td>
	  <td class="right">物流公司：</td> 
	  <td align="left">
		 <select name="LOGI_NAME" id="LOGI_NAME" class="u-select" >
		 	<option value="">--请选择--</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  </td> 	 
  </tr> 
   <tr class="csstr" align="center" id="o02">	
 		<td class="right">省份：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt1" name="PROVINCE" onchange="_genCity(this,'txt2')"></select>
     	 </td> 
     	 <td class="right">地级市：</td>  
		    <td align="left">
	  		<select class="u-select" id="txt2" name="CITY_ID"></select>
     	 </td>   
		  <td class="right">区县：</td>
		   	   <td align="left">
	  				<input type="text" maxlength="20"  id="COUNTY_ID" datatype="1,is_null,100" maxlength="100" name="COUNTY_ID" class="middle_txt"  size="15" />
			 </td> 
 </tr>
 <tr class="csstr" align="center" id="o03">	
 	<td class="right" width="15%">产地：</td> 
	  <td align="left" colspan="5">
		 <select name="YIELDLY" id="YIELDLY" class="selectlist" >
		 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td> 	
 </tr>
  <tr class="csstr" align="center" id="d01" style="display: none">	
 		<td class="right">VIN：</td>  
		    <td align="left">
	  		<input type="text" maxlength="20"  name="VIN" class="middle_txt"  size="15" />
     	 </td> 
     	 <td class="right">组板号：</td>  
		    <td align="left">
	  		<input type="text" maxlength="20"   name="BOARD_NO" class="middle_txt"  size="15"/>
     	 </td>   
		  <td class="right">运单号：</td>
		  <td align="left">
	  		<input type="text" maxlength="20"   name="WAYBILL_NO" class="middle_txt"  size="15" />
		</td> 
 </tr>
 <tr class="csstr" align="center" >
  <td class="right">统计类型：</td> 
	  <td align="left">
		 <label>
				<script type="text/javascript">
						genSelBoxExp("STA_TYPE",<%=Constant.STA_TYPE%>,"<%=Constant.STA_TYPE_01%>",false,"selectlist","onchange=staType();","false",'');
					</script>
			</label>
	  </td> 
		 <td colspan="2" align="center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="_function(1);" />   
    	  <input type="button" id="queryBtn" class="normal_btn"  value="导出" onclick="_function(2);" />   	 	
    </td>
 </tr>
</table>
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
			<tr class="table_list_row2">
				<td>承运总数：<span id="a1"></span></td>
				<td width="80%"><span></span></td>
			</tr>
		</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->

<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url;
	var title = null;
	var columns;
 
	//初始化    
	function doInit(){
		//日期控件初始化
		 genLocSel('txt1','','');//支持火狐
		//__extQuery__(1);
	}
	//统计类型
	function staType(){
		var staType=document.getElementById("STA_TYPE").value;
		if(staType==<%=Constant.STA_TYPE_03%>){//时间统计
			document.getElementById("o01").style.display="none";
			document.getElementById("o02").style.display="none";
			document.getElementById("o03").style.display="none";
			document.getElementById("d01").style.display="";
		}else{
			document.getElementById("o01").style.display="";
			document.getElementById("o02").style.display="";
			document.getElementById("o03").style.display="";
			document.getElementById("d01").style.display="none";
		}
	}
	function _function(_type){
	var staType=document.getElementById("STA_TYPE").value;
		resetParams(staType);
		var url;
		if(staType==<%=Constant.STA_TYPE_01%>){//配车及时率
				url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryPC.do?common=2";
		}else if(staType==<%=Constant.STA_TYPE_02%>){//配车及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQuery.do?common=2";
		}else{//发运及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryDate.do?common=2";
		}
		if(_type==1){
			  tgSum(staType);
			__extQuery__(1);
		  }
		 if(_type==2){
			  	fm.action=url;  
			   	fm.submit();
		 }
	}
	function resetParams(staType){
		if(staType==<%=Constant.STA_TYPE_01%>){//配车及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryPC.json";
			columns = [
						{header: "组板号",dataIndex: 'BO_NO',align:'center'},
						{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "发运省份",dataIndex: 'PC_NAME',align:'center'},
						{header: "发运城市",dataIndex: 'PC_NAME1',align:'center'},
						{header: "发运区县",dataIndex: 'PC_NAME2',align:'center'},
						{header: "承运物流商",dataIndex: 'LOGI_NAME',align:'center'},
						{header: "承运车牌",dataIndex: 'CAR_NO',align:'center'},
						{header: "承运数量",dataIndex: 'VEH_NUM',align:'center'},
						{header: "分派时间",dataIndex: 'ASS_DATE',align:'center'},
						{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
						{header: "差额天数",dataIndex: 'CS_TIME',align:'center'}
						
				      ];
		}else if(staType==<%=Constant.STA_TYPE_02%>){//发运及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQuery.json";
			columns = [
						{header: "组板号",dataIndex: 'BO_NO',align:'center'},
						{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "发运省份",dataIndex: 'PC_NAME',align:'center'},
						{header: "发运城市",dataIndex: 'PC_NAME1',align:'center'},
						{header: "发运区县",dataIndex: 'PC_NAME2',align:'center'},
						{header: "承运物流商",dataIndex: 'LOGI_NAME',align:'center'},
						{header: "承运车牌",dataIndex: 'CAR_NO',align:'center'},
						{header: "承运数量",dataIndex: 'VEH_NUM',align:'center'},
						{header: "保险单号",dataIndex: 'POLICY_NO',align:'center'},
						{header: "险种",dataIndex: 'POLICY_TYPE',align:'center'},
						{header: "运单时间",dataIndex: 'BILL_CRT_DATE',align:'center'},
						{header: "物流装配时间",dataIndex: 'ZP_DATE',align:'center'},
						{header: "预计到达时间",dataIndex: 'DD_DATE',align:'center',renderer:myLink},
						{header: "应到达天数",dataIndex: 'ARRIVE_DAYS',align:'center'},
						{header: "验收时间",dataIndex: 'ACC_DATE',align:'center'},
						{header: "差额天数",dataIndex: 'D_DAYS',align:'center'}
						
				      ];
		}else{//发运及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryDate.json";
			columns = [
						{header: "组板号",dataIndex: 'BO_NO',align:'center'},
						{header: "运单号",dataIndex: 'BILL_NO',align:'center'},
						{header: "分派时间",dataIndex: 'ASS_DATE',align:'center'},
						{header: "组板时间",dataIndex: 'BO_DATE',align:'center'},
						{header: "运单时间",dataIndex: 'BILL_CRT_DATE',align:'center'},
						{header: "到达时间",dataIndex: 'DD_DATE',align:'center',renderer:myLink},
						{header: "验收时间",dataIndex: 'ACC_DATE',align:'center'},
						{header: "应到达天数",dataIndex: 'ARRIVE_DAYS',align:'center'},
						{header: "差额天数",dataIndex: 'D_DAYS',align:'center'},
						{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
						{header: "发运省份",dataIndex: 'PC_NAME',align:'center'},
						{header: "发运城市",dataIndex: 'PC_NAME1',align:'center'},
						{header: "发运区县",dataIndex: 'PC_NAME2',align:'center'},
						{header: "承运物流商",dataIndex: 'LOGI_NAME',align:'center'},
						{header: "承运车牌",dataIndex: 'CAR_NO',align:'center'},
						{header: "承运数量",dataIndex: 'VEH_NUM',align:'center'},
						{header: "保险单号",dataIndex: 'POLICY_NO',align:'center'},
						{header: "险种",dataIndex: 'POLICY_TYPE',align:'center'}
				      ];
		}

	}
	//统计数量和
	function tgSum(staType){
		var url;
		if(staType==<%=Constant.STA_TYPE_01%>){//配车及时率
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryPC.json?common=1";
		}else if(staType==<%=Constant.STA_TYPE_02%>){
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQuery.json?common=1";
		}else{
			url="<%=contextPath%>/sales/storage/sendmanage/DateTimeCheckManage/dateTimeCheckQueryDate.json?common=1";
		}
		document.getElementById("a1").innerHTML = '';
		makeNomalFormCall(url,function(json){
			document.getElementById("a1").innerHTML = json.valueMap.VEH_NUM == null ? '0' : json.valueMap.VEH_NUM;
		},'fm');
	}
	function myLink(value,metaData,record){
			if(value==""){
				value="未设置预计到达天数"
			}
			return String.format(value);
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</body>
</html>
