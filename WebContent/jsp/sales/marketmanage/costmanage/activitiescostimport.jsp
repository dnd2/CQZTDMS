<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contentPath=request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>市场活动费用导入 </title>
<script language="JavaScript">
</script>
</head>

<body>
	<div class="wbox">
	<div class="navigation"><img src="<%=contentPath %>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动费用管理&gt;活动费用导入
	</div>
<form name="fm" method="post"  enctype="multipart/form-data">
  
<table class="table_query">
	<tr>
			<td align="left" colspan="2">1、选择业务范围：
				<select name="areaId" class="short_sel" datatype="0,is_null,20">
					<c:forEach items="${areaList}" var="po">
						<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
					</c:forEach>
				</select>
				<input type="hidden" name="dealerId" id="dealerId" />
				<input type="hidden" name="area_id" id="area_id" value="" />
			</td>
		</tr>
	<tr> 
		<td class="table_query_label" colspan="2">
			2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的的活动费用目标文件,请确定文件的格式为“<strong>经销商代码—经销商名称—-任务数量</strong>”：&nbsp;&nbsp;&nbsp;</td>
    </tr>
	<tr>
	    <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp; 
	      <input type="file" name="uploadFile" datatype="0,is_null,2000" id="upfile" value="" />
	    </td>
    </tr>
	<tr> 
		<td class="table_query_label" width="30%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
	    <td class="table_query_label" align="left" width="56%">
	       <input type="button" id="upbtn" class="cssbutton"  name="vdcConfirm" value="确定" onclick="upload()" />
	       <input type="button" class="long_btn"  name="downloadBtn" value="导入模板下载" onclick="downloadFile()" />
	    </td>
	</tr>
</table>
</form>
</div>
<script type="text/javascript">

	function upload(){
		if(!submitForm('fm')){
			return false;
		}
		document.getElementById("upbtn").disabled=true;
		$('fm').action = "<%=contentPath %>/sales/marketmanage/costmanage/ActivitiesCostImport/activitiesCostExcelOperate.do";
    	$("fm").submit();
	}
	
	function downloadFile(){
	    $('fm').action= "<%=contentPath %>/sales/marketmanage/costmanage/ActivitiesCostImport/downloadTemple.do";
	    $('fm').submit();
	}
	
</script>
</body>
</html>
