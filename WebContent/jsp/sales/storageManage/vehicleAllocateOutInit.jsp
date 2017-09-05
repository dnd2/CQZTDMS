<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
  <%@ page import="com.infodms.dms.common.Constant" %>
  <%@taglib uri="/jstl/cout" prefix="c" %>
  <% String contextPath = request.getContextPath(); %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <script type="text/javascript" src="getJs.do?fileName=vehicleAllocateOutInit">
    </script>
    <title>退车车辆选择</title>
  </head>
  <body>
    <div class="navigation">
      <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 调拨出库
    </div>
    <form id="fm" name="fm" method="post">
      <input type="hidden" name="curPage" id="curPage" value="1" /><input type="hidden" id="dealerId" name="dealerId" value="" />
      <table class="table_query" border="0">
        <tr>
          <td width="20%" class="tblopt">
            <div class="right">
              VIN：
            </div>
          </td>
          <td width="39%">
            <textarea id="vin" name="vin" cols="18" rows="3">
            </textarea>
          </td>
          <td class="table_query_3Col_input">
            <input type="hidden" name="exVeh" id="exVeh" value="" /><input type="button" class="normal_btn" onclick="__extQuery__(1);" value="  查  询  " id="queryBtn" />
          </td>
        </tr>
      </table><jsp:include page="${contextPath}/queryPage/orderHidden.html" /><jsp:include page="${contextPath}/queryPage/pageDiv.html" />
      <table class="table_query" width="85%" align="center" border="0" id="roll">
        <tr>
          <td width="37%" align="center">
            <input name="button2" id="button2" type="button" class="normal_btn" onclick="AllocateOut(this);" value="调拨出库" />
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>