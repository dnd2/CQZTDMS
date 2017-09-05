<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />	
<title>配件拆合比例新增</title>

</head>
<body onunload='javascript:destoryPrototype()'" onload="checkRowNum();">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		基础信息管理 &gt; 配件基础信息维护 &gt; 配件拆合比例设置 &gt; 查看
</div>
<form method="post" name ="fm" id="fm">
	<input type="hidden" name="PART_ID" id="PART_ID" value=""/>
	<input type="hidden" name="myIndex" id="myIndex" value=""/>
  <table width="101%" border="1" class="table_edit" cellpadding="0" cellspacing="0">
    <tr>
      <th colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" />总成件信息</th>
    </tr>
    <tr>
	      <td width="10%"   align="right" >总成件编码：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" disabled="disabled" value="${partSplit.partOldcode }"/>
	      </td>
	      <td width="10%"   align="right"  >总成件件号：</td>
	      <td  width="20%">
	      <input class="middle_txt" type="text"  name="PART_CODE" id="PART_CODE" disabled="disabled" value="${partSplit.partCode }"/>
	      </td>
	      <td  width="10%"  align="right" >总成件名称：</td>
	      <td width="20%"  >
	      <input class="middle_txt" type="text"  name="PART_CNAME" id="PART_CNAME" disabled="disabled" value="${partSplit.partCname }"/>
	      </td>
      </tr>
  </table>
  <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
  <tr>
      <th colspan="18" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />分总成件信息
        </th>
    </tr>
  </table>
   <table id="subpart" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr class="table_list_row0">
      <!-- ><td><input type="checkbox" onclick="selectAll()"/></td> -->
      <td>序号</td>
      <td>分总成件件号</td>
      <td>分总成件编码</td>
      <td>分总成件名称</td>
      <td>拆分数量</td>
      <td>拆分成本比例</td>
      <td>备注</td>
    </tr>
    <c:forEach varStatus="idx" items="${splitInfo}" var="partSplit">
    <tr>
       <td>${idx.index+1}</td>
       <td>${partSplit.subpartCode }</td>
       <td>${partSplit.subpartOldcode }</td>
       <td>${partSplit.subpartCname }</td>
       <td>${partSplit.splitNum}</td>
       <td>${partSplit.costRate }</td>
       <td>${partSplit.remark }</td>
       </tr>
    </c:forEach>
  </table>
  <table  class="table_list" style="border-bottom:1px solid #DAE0EE">
  <td align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="关闭" onclick="_hide();"  class="normal_btn"/>
        </td>
  </table>
</form>

<script type="text/javascript">

</script>
</body>
</html>