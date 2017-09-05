<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导入待分配资源</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 区域配额计算</div>
<form method="POST" name="fm" id="fm">
  <table class=table_list style="border-bottom:1px solid #DAE0EE">
    <tr align=center >
      <th><strong>配置参数名称</strong></th>
      <th><strong>参数用途</strong></th>
      <th><strong>数值%</strong></th>
    </tr>
    <tr class="table_list_row1">
      <td nowrap><div>${paraPO1.paraName}</div></td>
      <td nowrap><div align="center">
        <div>${paraPO1.typeName}</div>
      </div></td>
      <td nowrap><div align="center"><font class=frontpage>
        <input type="text" id="paraValue1" name="paraValue1" value="${paraPO1.paraValue}" datatype="0,is_digit,3" size="10">
      </font></div></td>
    </tr>
    <tr class="table_list_row2">
      <td nowrap><div>${paraPO2.paraName}</div>      </td>
      <td nowrap><div align="center">
        <div>${paraPO2.typeName}</div>
      </div></td>
      <td nowrap><div align="center"><font class=frontpage>
        <input type="text" id="paraValue2" name="paraValue2" value="${paraPO2.paraValue}" datatype="0,is_digit,3" size="10">
      </font></div></td>
    </tr>
    <tr class="table_list_row1">
      <td nowrap><div>${paraPO3.paraName}</div></td>
      <td nowrap><div align="center">
        <div>${paraPO2.typeName}</div>
      </div></td>
      <td nowrap><div align="center"><font class=frontpage>
        <input type="text" id="paraValue3" name="paraValue3" value="${paraPO3.paraValue}" datatype="0,is_digit,3" size="10">
      </font></div></td>
    </tr>
  </table>
  <TABLE width="95%" class=table_query >
    <TR>
      <TD width="27%"><div align="left">
          <input name="btn4" type="button" class="cssbutton_large" value="确定" onClick="confirmAdd();">
      </div></TD>
    </TR>
  </TABLE>
  <p>&nbsp;</p>
</form>
<script type="text/javascript">
	function confirmAdd(){
		if(submitForm('fm')){
			MyConfirm("是否确认保存?", orderAdd);
		}
	}
	
	function orderAdd(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/AreaQuotaCalculate/areaQuotaCalculateParaSet.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sales/planmanage/QuotaAssign/AreaQuotaCalculate/areaQuotaCalculatePre.do';
		}else{
			MyAlert("保存失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
