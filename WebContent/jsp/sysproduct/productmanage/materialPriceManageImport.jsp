<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%String contentPath = request.getContextPath();%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料价格维护 </title>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护
</div>
<form name="fm" method="post"  enctype="multipart/form-data">
<table class="table_query">
    <tr class="csstr">
	    <td colspan="2" nowrap="nowrap">
	      1、选择价格类型：
	      <select name="priceId">
			<c:forEach items="${types}" var="po">
				<option value="${po.PRICE_ID}">${po.PRICE_DESC}</option>
			</c:forEach>
          </select>
	    </td>
  	</tr>
	<tr class="csstr">
	  <td colspan="2" nowrap="nowrap">
	  	2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要上载的<font color="#FF0000">物料价格</font>文件(<strong>"配置代码-销售价格-不含税价格"</strong>)：
	    <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="uploadFile" value="" />
	  </td>
    </tr>
    <tr class="csstr"> 
	  <td width="45%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。	    </td>
      <td width="55%"><input type="button" class="cssbutton" id="queryBtn" name="queryBtn" value="确定" onclick="upload()" /></td>
    </tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
		disableBtn($("queryBtn"));
		if(submitForm('fm')){
			$('fm').action = "<%=contentPath %>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageExcelOperate.do";
	    	$("fm").submit();
		}
	}
	
</script>
</body>
</html>
