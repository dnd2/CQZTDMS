<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@ page import="java.util.Date" %>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
	__extQuery__(1);
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
<%
 Calendar calendar  =   new  GregorianCalendar();
 calendar.set( Calendar.DATE,  1 );
 SimpleDateFormat simpleFormate  =   new  SimpleDateFormat( "yyyy-MM-dd" );
 %>
</head>
<body onload='doInit() ;'>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  客户关系管理&gt;回访管理&gt;客户回访管理
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" name="ids" id="ids" />
<input type="hidden" name="assId" id="assId" />
<input type="hidden" name="assName" id="assName" />
<TABLE class=table_query>
  <TBODY>
    <tr class="">
      <td width="22%" align="right">客户名称： </td>
      <td width="26%"><input id="RV_CUS_NAME" style="WIDTH: 120px" name="RV_CUS_NAME" /></td>
      <td width="16%" align="right">回访类型： </td>
      <td width="21%"> 
      <script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	  </script>
	</td>
      <td width="15%" align="center">  </td>
    </tr>
    <tr class="">
      <td align="right">生成日期：</td>
      <td>
      <input  name="RV_DATES" id="RV_DATES" 
              value="<%=simpleFormate.format(calendar.getTime())%>" group="RV_DATES,RV_DATEE"
              type="text" class="short_txt" datatype="1,is_date,10" readonly/>
      <input class="time_ico" onclick="showcalendar(event, 'RV_DATES', false);" type="button" value=" " />
 	至 
<%
 calendar.set( Calendar.DATE,  1 );
 calendar.roll(Calendar.DATE,  - 1 );

%>
	<input name="RV_DATEE" class="short_txt" id="RV_DATEE" maxlength="10" group="RV_DATES,RV_DATEE" value="<%=simpleFormate.format(calendar.getTime())%>" datatype="1,is_date,10" readonly/>
	<input class="time_ico" onclick="showcalendar(event, 'RV_DATEE', false);" type="button" value=" " /></td>
     <td align="right">汽车种类：</td>
      <td><select id="vehicleType" name="vehicleType" class="short_sel">
        <option value="">-请选择-</option>
			<c:forEach items="${typeNameList}" var="t" >
				<option value="${t.SERIES_NAME}">${t.SERIES_NAME}</option>
			</c:forEach>
      </select></td>
      <td align="center"> </td>
    </tr>
    <tr class="">
       <td align="right">联系电话：</td>
      <td><input id="TELEPHONE" style="WIDTH: 120px" name=TELEPHONE /></td>
      <td align="right">回访结果：</td>
      <td>
       <script type="text/javascript">
           genSelBoxExp("RD_IS_ACCEPT",<%=Constant.RD_IS_ACCEPT%>,null,true,"short_sel","","false",'');
	   </script>
      </td>
      <td align="center"> </td>
    </tr>
    <tr class="">
      <td align="right">回访人：</td>
      <td><input id="RV_ASS_USER" style="WIDTH: 120px" name="RV_ASS_USER" /></td>
      <td align="right">当前状态 ：</td>
      <td>
      <script type="text/javascript">
           genSelBoxExp("RV_STATUS",<%=Constant.RV_STATUS%>,null,true,"short_sel","","false",'');
	  </script>
      </td>
      <td align="center"> </td>
    </tr>
    <tr>
    	<td align="center" colspan="5">
			<input id="queryBtn" type="button" class="normal_btn"  onclick="__extQuery__(1);" align="center" value="查询" />
			<input name="buttonRt" type="button" class="normal_btn"  onclick="changeRt();" align="center" value="修改回访人" />
    	</td>
    </tr>
  </TBODY>
</TABLE>
 
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
var myPage;
//查询路径
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewSearchQuery.json";
	var title = null;
	var columns = [
// 				{id:"action",header: "操作",sortable: false,align:'center',renderer:myCheckBox},
				{header: "编号",sortable: false,align:'center',renderer:getIndex},
				{id:'action',header: "选择<input type='checkbox' id='checks' onclick='checkAll();' />",sortable: false,dataIndex: 'RV_ID',renderer:myCheckBox},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "回访类型 ",sortable: false,dataIndex: 'RV_TYPE',align:'center'},
				{header: "生成时间",sortable: false,dataIndex: 'RV_DATE',align:'center'},
				{header: "当前状态",sortable: false,dataIndex: 'RV_STATUS',align:'center'},
				{header: "回访人",sortable: false,dataIndex: 'RV_ASS_USER',align:'center'},
				{header: "备注",sortable: false,dataIndex: 'RD_CONTENT',align:'center'},
				{header: "查看",sortable: false,dataIndex: 'RV_ID',renderer:myHandler ,align:'center'}
		      ];
		      
	//查看回访超链接		      
  	function myHandler(value,meta,record){
  		if(record.data.MANAGER=='YES')
  		{
  			if(record.data.RV_STATUS=="未回访"||record.data.RV_STATUS=="继续回访")
  			{
  				return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID='+record.data.RV_ID+'">[查看]</a><a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/Review.do?page=search&RV_ID='+record.data.RV_ID+'">[回访]</a>' ;
  			}else{
  				return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID='+record.data.RV_ID+'">[查看]</a><a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/updateReview.do?RV_ID='+record.data.RV_ID+'">[修改]</a>' ;
  			}
  			  		}else
  		{
  			return '<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/seeReview.do?RV_ID='+record.data.RV_ID+'">[查看]<a href="<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/updateReview.do?RV_ID='+record.data.RV_ID+'">[修改]</a></a>' ;
  		}
	}
	
	function myCheckBox(value,meta,record){
		return "<input type='checkbox' name='checkbox' value='"+record.data.RV_ID+"' />";
	}
	
	function changeRt(){
		var rvIds = document.getElementsByName("checkbox");
		var ids = "";
		if(rvIds != null && rvIds.length > 0){
			for(var i = 0;i < rvIds.length;i++){
				if(rvIds[i].checked){
					ids += rvIds[i].value+",";
				}
			}
		}
		if(ids != ""){
			if(ids.lastIndexOf(",") == ids.length - 1){
				ids = ids.substring(0, ids.length - 1);
			}
			$("ids").value = ids;
			OpenHtmlWindow('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/querySet.do',800,500);
		}else{
			MyAlert("请选择要修改的数据!");
		}
	}
	
	function returnFunction(id,name){
		if(id.split(",").length > 1){
			MyAlert("只能指定一个回访人,请重新选择!");
			return;
		}
		$("assId").value = id;
		$("assName").value = name;
		makeNomalFormCall('<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/updateAss.json',changeData,'fm','');
	}
	
	function changeData(json){
		__extQuery__(1);
	}
	
	function checkAll(){
		var checks = document.getElementById("checks");
		var rvIds = document.getElementsByName("checkbox");
		if(rvIds != null && rvIds.length > 0){
			if(checks.checked){
				for(var i = 0;i < rvIds.length;i++){
					rvIds[i].checked = true;
				}
			}else{
				for(var i = 0;i < rvIds.length;i++){
					rvIds[i].checked = false;
				}
			}
		}
	}
</script>  