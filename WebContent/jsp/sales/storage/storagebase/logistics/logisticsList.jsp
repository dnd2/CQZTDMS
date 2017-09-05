<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 物流商管理 </title>

</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>基础管理>物流商管理</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
<div class="form-body">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >
<tr class="csstr" align="center">
	  <td class="right">物流商代码：</td>
   	<td align="left">
		  <input type="text" id="LOGI_CODE" name="LOGI_CODE" class="middle_txt" datatype="1,is_null,20" size="15" maxlength="20" />
	  </td> 
	  <td class="right">物流商名称：</td> 
	  <td align="left">
		  <input type="text" id="LOGI_FULL_NAME" name="LOGI_FULL_NAME" class="middle_txt" size="15" datatype="1,is_null,20"  maxlength="20" />
	  </td>  
 </tr>
 <tr class="csstr" align="center">
	  <td class="right">联系人：</td>
   	<td align="left">
		  <input type="text" id="CON_PER" name="CON_PER" class="middle_txt" datatype="1,is_null,20" size="15" maxlength="20"  />
	  </td> 
	 <td class="right">联系电话：</td> 
	  <td align="left">
		 <input type="text" id="CON_TEL" name="CON_TEL" class="middle_txt" datatype="1,is_null,20" size="15" maxlength="20"  onkeyup="phonecheck(this)"/>
	  </td>   
 </tr>
  <tr class="csstr" align="center">
  	<td class="right">状态：</td> 
	  <td align="left">
		 <label>
				<script type="text/javascript">
						genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"u-select","","false",'');
					</script>
			</label>
	  </td>
	  <td class="right">产地：</td> 
	  <td align="left">
		 <select name="YIELDLY" id="YIELDLY" class="u-select" >
				 <option value="">--请选择--</option>
				<c:if test="${list!=null}">
					<c:forEach items="${list}" var="list">
						<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
		</td>  
  </tr> 
  <tr align="center">
  <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	  <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" />   	
  		  <input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/>
    	  <input type="button" id="queryBtn" class="u-button u-submit" value="新增" onclick="addReservoir();" />
    </td>
  </tr>
</table>
</div>
</div>
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
	var url = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logisticsQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "物流商代码",dataIndex: 'LOGI_CODE',align:'center'},
				{header: "物流商名称",dataIndex: 'LOGI_FULL_NAME',align:'center'},
				{header: "联系人",dataIndex: 'CON_PER',align:'center'},
				{header: "联系电话",dataIndex: 'CON_TEL',align:'center'},
				{header: "公司地址",dataIndex: 'ADDRESS',align:'center'},
				{header: "产地",dataIndex: 'YIELDLY',align:'center'},
				{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false, dataIndex: 'LOGI_ID', align:'center',renderer:myLink}
		      ];


	//设置超链接
	function myLink(value,meta,record)
	{
  		return String.format("<a href=\"javascript:void(0)\" onclick='sel(\""+value+"\", \""+record.data.POSE_ID+"\")'>[修改]</a>");
	}
	
	//详细页面
	function sel(value,poseId)
	{
		if(poseId==null||poseId==''||poseId=='null'){
			MyAlert("获取职位信息失败！");
		}else{
			window.location.href='<%=contextPath%>/sales/storage/storagebase/LogisticsManage/editLogisticsInit.do?Id='+value+'&poseId='+poseId;  
		}
	 	
	}
	//初始化    
	function doInit(){
		//__extQuery__(1);
	}
	//跳转新增页面
	function addReservoir()
	{
		fm.action = "<%=contextPath%>/sales/storage/storagebase/LogisticsManage/addLogisticsInit.do";
		fm.submit();
	}
</script>
</body>
</html>
