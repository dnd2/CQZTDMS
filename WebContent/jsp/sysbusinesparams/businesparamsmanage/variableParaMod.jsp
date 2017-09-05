<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<% String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>可变代码维护</title>
<script type="text/javascript">
	function doInit()
	{
		showTypeName();
	}
</script>
</head>

<body>
 <div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统业务参数维护 &gt; 可变代码维护
 </div>
 <form method="post" name="fm">
 	<input type="hidden" name="paraId" id="paraId" value="<c:out value="${paraMap.PARA_ID}"/>"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <tr>
    	   	<td class="table_query_2Col_label_6Letter">参数类型：</td>
		    <td>
			  <input type="text" class="middle_txt" name="paraTypeName" id="paraTypeName" datatype="0,is_null,15" value="" disabled/>
			  <input type="hidden" name="paraType" id="paraType" value="<c:out value="${paraMap.PARA_TYPE}"/>"/>
		    </td>
    	  </tr>
    	  <tr>
    	   	<td class="table_query_2Col_label_6Letter">参数代码：</td>
		    <td>
		    	<input type="text" class="middle_txt" name="paraCode" id="paraCode" datatype="0,is_null,15" value="<c:out value="${paraMap.PARA_CODE}"/>"/>
		    </td>
    	  </tr>
    	  <tr>
    	   	<td class="table_query_2Col_label_6Letter">参数名称：</td>
		    <td>
		    	<input type="text" class="middle_txt" name="paraName" id="paraName" datatype="0,is_null,15" value="<c:out value="${paraMap.PARA_NAME}"/>"/>
		    </td>
    	  </tr>
    	  <tr>
    	   	<td class="table_query_2Col_label_6Letter">状态：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("status",<%=Constant.STATUS%>,"<c:out value="${paraMap.STATUS}"/>",false,"short_sel","","true",'');
                </script>
		    </td>
    	  </tr>
    	  <tr>
    	   	<td class="table_query_2Col_label_6Letter">是否需要下发：</td>
		    <td><script type="text/javascript">
            	genSelBoxExp("issue",<%=Constant.IF_TYPE%>,"<c:out value="${paraMap.ISSUE}"/>",false,"short_sel","","true",'');
                </script>
		    </td>
    	  </tr>
		  <tr>
		    <td class="table_query_2Col_label_6Letter">备注：</td>
		    <td><textarea name="remark" id="remark" cols="50" rows="2"><c:out value="${paraMap.REMARK}"/></textarea></td>
		  </tr>
		  <tr>  	
		    <td>&nbsp;&nbsp;&nbsp;</td>
		    <td>
		        <input name="sureBtn" type="button" class="normal_btn" onclick="updatePara();" value="确定" />
		        <input name="cancelBtn" type="button" class="normal_btn" onclick="history.back();" value="取消" />
		    </td>
	      </tr>
     </table> 
  <!-- 查询条件 end -->
 </form>

<!--页面列表 begin -->
<script type="text/javascript" >
		      
//设置超链接  begin     
 
	//修改的ACTION设置
	function updatePara(){
	 if(submitForm('fm')){
	 	MyConfirm("是否确认修改?",confirmUpdate);
	 }
	}
	
	//确认新增
	function confirmUpdate(){
		makeNomalFormCall('<%=contextPath%>/sysbusinesparams/businesparamsmanage/VariableParaManage/updatePara.json',showResult,'fm');
	}
	
	//新增回调函数
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=contextPath%>/sysbusinesparams/businesparamsmanage/VariableParaManage/variableParaManageInit.do';
		}else{
			MyAlert("修改失败,请联系系统管理员！");
		}
	}
	
	
	
	// 根据参数类型的NUMBER形式显示相应的中文描述
	function showTypeName()
	{
		var ptype = '<c:out value="${paraMap.PARA_TYPE}"/>';
		document.getElementById("paraTypeName").value = getItemValue(ptype);
	}
	
//设置超链接 end
</script>
<!--页面列表 end -->
</body>
</html>