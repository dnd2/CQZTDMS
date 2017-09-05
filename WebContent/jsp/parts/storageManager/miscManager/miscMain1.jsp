<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>杂项入库</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件仓库管理&gt; 杂项入库(新)</div>
<form method="post" name ="fm" id="fm">
    <table class="table_query" >
	    <tr id="groupId">
	        <td width="20%" align="right">入库单号：</td>
	        <td  width="30%"><input class="middle_txt" type="text" id="orderCode" name="orderCode"/  jset="para"></td>
            <td width="20%"><div align="right">制单时间:</div></td>
            <td width="30%" align="left">
                <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
                &nbsp;至&nbsp;
                <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para"/>
                <input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
            </td>
        </tr>	
        <tr>
            <td align='center' colspan=4>
            <input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/>
            <c:forEach items="${actionList}" var="action">
                <c:if test="${action.ACTION_TYPE==2}">
                    <input name="addBtn" id="addBtn" type="button" class="normal_btn" onclick="fm.action='<%=contextPath%>${action.ACTION_CODE}.do';fm.submit();" value="${action.ACTION_NAME}"/>
                </c:if>           
            </c:forEach>
            </td>
            <td colspan=2></td>
        </tr>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>

<script language="javascript">
	loadcalendar();  //初始化时间控件   
	var myPage;
	var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager1/getMainList.json";
	var title = null;
	var columns = [
				{header: "序号", align:'center', renderer:getIndex},
                {header: "入库单号", dataIndex: 'MISC_ORDER_CODE', align:'center'},
                {header: "入库单位", dataIndex: 'ORG_NAME', align:'center'},
                {header: "入库仓库", dataIndex: 'WH_NAME', align:'center'},
                {header: "备注", dataIndex: 'REMARK', style: 'text-align:left'},
                {header: "制单人", dataIndex: 'NAME', align:'center'},
                {header: "入库日期", dataIndex: 'CREATE_DATE', align:'center'},
                {header: "操作",  align:'center',sortable: false,dataIndex: 'MISC_ORDER_ID',renderer:miscView}
		      ];
    //设置超链接  begin	
	function miscView(value, meta, record) {
          var orderId = record.data.MISC_ORDER_ID;
          var orderCode = record.data.MISC_ORDER_CODE;
          var formatString="";
          <c:forEach items="${actionList}" var="action">
              <c:if test="${action.ACTION_TYPE==1}">
                  formatString+="<a href='<%=contextPath%>"+"${action.ACTION_CODE}"+".do?orderId="+orderId+"' >["+"${action.ACTION_NAME}"+"]</a>";
              </c:if>
          </c:forEach>    
          return String.format(formatString);
    }
        
</script>

</body>
</html>