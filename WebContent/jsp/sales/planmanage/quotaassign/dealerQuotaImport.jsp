<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%String contentPath = request.getContextPath();%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商配额导入 </title>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置：整车销售 > 计划管理 > 配额调整 > 经销商配额导入
</div>
<form name="fm" method="post"  enctype="multipart/form-data">
<table class="table_query">
    <tr class="csstr">
	    <td colspan="2" nowrap="nowrap">
	      1、选择业务范围：
	      <select name="areaId">
			<c:forEach items="${areaList}" var="po">
				<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
			</c:forEach>
          </select>
	    </td>
  	</tr>
	<tr class="csstr">
	  	<td colspan="2" nowrap="nowrap">
	  		2、选择要导入的配额月份：
		    <select name="quotaMonth">
		      <c:forEach items="${dateList}" var="po">
					<option value="${po.code}">${po.name}</option>
			  </c:forEach>
	        </select>
 		</td>
    </tr>
	<tr class="csstr">
	  <td colspan="2" nowrap="nowrap">
	  	3、点“<font color="#FF0000">浏览</font>”按钮，找到您所要上载的<font color="#FF0000">配额计划</font>文件(<strong>"经销商代码-经销商名称-省份-配置代码-配置名称-周度配额数量..."</strong>)：
	    <input type="file" name="uploadFile" style="width: 250px"  datatype="0,is_null,2000" id="uploadFile" value="" />
	  </td>
    </tr>
    <tr class="csstr"> 
	  <td width="45%">4、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。	    </td>
    </tr>
    <tr class="csstr"> 
    <td></td>
	  <td width="55%"><input type="button" class="cssbutton"  name="vdcConfirm" id="vdcConfirm" value="确定" onclick="upload()" />
		<input type="button" class="cssbutton"  name="downLoad" id="downLoad" value="模板下载" onclick="downLoadIt()" /></td>
    </tr>
</table>
</form>
</div>
<script type="text/javascript">
	function upload(){
		if(submitForm('fm')){
			document.getElementById('vdcConfirm').disabled = "disabled" ; 
			//document.getElementById('downLoad').disabled = "disabled" ; 
			$('fm').action = "<%=contentPath %>/sales/planmanage/QuotaAssign/DealerQuotaImport/dealerQuotaExcelOperate.do";
	    	$("fm").submit();
		}
	}

	function downLoadIt(){
		$('fm').action = "<%=contentPath %>/sales/planmanage/QuotaAssign/DealerQuotaImport/downLoadDEL.json";
    	$("fm").submit();
	}
</script>
</body>
</html>
