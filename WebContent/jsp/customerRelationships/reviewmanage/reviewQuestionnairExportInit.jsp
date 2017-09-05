<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
function doInit()
{
	loadcalendar();
}
</script>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body >
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt;回访问卷导出
</div>
<form method="post" name="fm" id="fm">
<TABLE class=table_query>
  <TBODY>
    <tr class="">
      <td width="23%" class="table_query_3Col_label_6Letter">客户名称：</td>
      <td width="25%"><input id="cusName" style="WIDTH: 120px" name="cusName" /></td>
      <td width="16%" class="table_query_3Col_label_6Letter">回访类型：</td>
      <td width="21%"><script type="text/javascript">
           genSelBoxExp("RV_TYPE",<%=Constant.TYPE_RETURN_VISIT%>,null,true,"short_sel","","false",'');
	</script></td>
      <td width="15%">&nbsp;</td>
    </tr>
    <tr class="">
      <td class="table_query_3Col_label_6Letter">生成日期：</td>
      <td><input name="checkSDate" class="short_txt" id="checkSDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" type="button" value=" " />
 至 
<input name="checkEDate" class="short_txt" id="checkEDate" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" type="button" value=" " /></td>
      <td class="table_query_3Col_label_6Letter">汽车种类：</td>
      <td width="31%"><select id="ddlClasses" class="short_sel" name="ddlClasses">
        <option selected="selected" value="">--请选择--</option>
       	<c:if test="${seriesList!=null}">
			<c:forEach items="${seriesList}" var="list3">
				<option value="${list3.GROUP_NAME}">${list3.GROUP_NAME}</option>
			</c:forEach>
		</c:if>
      </select></td>
      <td align="center">&nbsp;</td>
    </tr>
    <tr class="">
     <td class="table_query_3Col_label_6Letter">回访日期：</td>
      <td><input name="checkSDate2" class="short_txt" id="checkSDate2" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
        <input class="time_ico" onclick="showcalendar(event, 'checkSDate2', false);" type="button" value=" " />
 至 
<input name="checkEDate2" class="short_txt" id="checkEDate2" maxlength="10" group="checkSDate,checkEDate" datatype="1,is_date,10" readonly />
<input class="time_ico" onclick="showcalendar(event, 'checkEDate2', false);" type="button" value=" " /></td>	
      <td class="table_query_3Col_label_6Letter">回访人：</td>
      <td><input id="RV_ASS_USER" style="WIDTH: 120px" name="RV_ASS_USER" /></td>
      
    </tr>
    <tr class="">
      <td class="table_query_3Col_label_6Letter">联系电话：</td>
      <td><input id="TELEPHONE" style="WIDTH: 120px" name="TELEPHONE" /></td>
      <td class="table_query_3Col_label_6Letter">问卷模版：</td>
      <td><select name="QR_ID" class="short_sel" id="QR_ID">
	    <!-- <option selected="selected" value="">--请选择--</option> -->
		   <c:forEach var="ql" items="${questionairList}">
				<option value="${ql.QR_ID}" >${ql.QR_NAME}</option>
		   </c:forEach>
	  </select></td>
      <td align="center">&nbsp;</td>
    </tr>
    <tr class="">
      <td class="table_query_3Col_label_6Letter">只显示空问卷：</td>
      <td><input id="showQuest" type="checkbox" name="showQuest" onchange="checkQuest();" value=""/></td>
      <td class="table_query_3Col_label_6Letter">回访结果：</td>
      <td><script type="text/javascript">
           genSelBoxExp("RD_IS_ACCEPT",<%=Constant.RD_IS_ACCEPT%>,null,true,"short_sel","","false",'');
	   </script></td>
      <td align="center">&nbsp;</td>
    </tr>
     <tr class="">
      <td class="table_query_3Col_label_6Letter">汽车型号：</td>
      <td><input type="text" id="ddlAutoTypeCode" name="ddlAutoTypeCode" /></td>
      <!-- <td><select id="ddlAutoTypeCode" class="short_sel" name="ddlAutoTypeCode">
        <option selected="selected" value="">--请选择--</option>
        	<c:if test="${vechileTypeList!=null}">
			<c:forEach items="${vechileTypeList}" var="vl">
				<option value="${vl.GROUP_ID}">${vl.GROUP_NAME}</option>
			</c:forEach>
			</c:if>
      </select></td> -->
      <td align="right">&nbsp;</td>
      <td>&nbsp;</td>
      <td align="center">&nbsp;</td>
    </tr> 
    <tr class="">
      <td colspan="2" align="right">&nbsp;</td>
      <td colspan="2" align="left"><span class="table">
        <input name="BtnQuery" type="button" class="normal_btn" id="queryBtn" align="right" value="查询" onclick="query();"/>
        <input name="exportBut" id="exportBut"  type="button" class="normal_btn" align="right" value="导出" onclick="exportQuestion();" />
      </span></td>
      <td align="center"><input type="hidden"  id="questionsId" name="questionsId"/> </td>
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
	var url = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewExportSearch.json";
	var title = null;
	var columns; 
	function query(){
		queryQuestion();
		__extQuery__(1);
	}
	function queryQuestion(){
		var QR_ID = document.getElementById("QR_ID").value;
		var showQuest = document.getElementById('showQuest').value;
		var url1 = "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/queryQuestions.json";
		makeCall(url1,sub1,{QR_ID:QR_ID,showQuest:showQuest});
    }
    function sub1(json){
       columns = [
				{header: "编号",sortable: false,align:'center',renderer:getIndex},
				{header: "客户姓名",sortable: false,dataIndex: 'RV_CUS_NAME',align:'center'}, 
				{header: "联系电话",sortable: false,dataIndex: 'PHONE',align:'center'},
				{header: "汽车种类 ",sortable: false,dataIndex: 'SERIES_NAME',align:'center'},
				{header: "汽车型号",sortable: false,dataIndex: 'MODEL_CODE',align:'center'},
				{header: "购买日期 ",sortable: false,dataIndex: 'PURCHASED_DATE',align:'center'},
				{header: "省份 ",sortable: false,dataIndex: 'PROVINCE',align:'center'},
				{header: "VIN号",sortable: false,dataIndex: 'VIN',align:'center'},
				{header: "回访结果",sortable: false,dataIndex: 'RV_RESULT',align:'center',renderer:getItemValue},
				{header: "回访人",sortable: false,dataIndex: 'RV_ASS_USER',align:'center'},
				{header: "备注",sortable: false,dataIndex: 'RD_CONTENT',align:'center'}
		      ];
	      if(json.questions!=null && json.questions.length>0){
		      for(var i=0;i<json.questions.length;i++){
		    	  columns.push({header: json.questions[i].QD_QUESTION,sortable: false,dataIndex: 'Q'+i,align:'center'});
		      }
	      }
	      columns.push({header: "修改",sortable: false,dataIndex: 'RV_ID',renderer:reviewLink ,align:'center'});
    }
	//回访超链接	      
	function reviewLink(value,meta,record)
	{
  		return String.format("<a href='#' onclick='review(\""+record.data.RV_ID+"\")'>修改</a>");
	}
	//修改的超链接设置
	function review(RV_ID){
	
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/modifyQuestExport.do?RV_ID=' + RV_ID; 
	 	fm.submit();
	}
	function checkQuest(){
		var obj = document.getElementById("showQuest");
		if(obj.checked){
			obj.value = "true";
		}else{
			obj.value = "";
		}
	}
	function exportQuestion(){
		fm.action = '<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/reviewQuestExport.do'; 
	 	fm.submit();
	}
</script>  
</body>
</html>