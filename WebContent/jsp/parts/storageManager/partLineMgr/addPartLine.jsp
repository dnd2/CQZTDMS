<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<%String contextPath=request.getContextPath();%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>排维护--新增 </title>
</head>

<body>
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：配件管理>基础信息管理>仓储相关信息维护>排维护>新增
	</div>

	<form name="frm" method="post" id="frm">
		<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
			<tr> 
				<th colspan="4"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
	        </tr>
		 	<tr>
	  			<td align="right">货位排代码：</td>
		   		<td align="left" class="table_info_2col_input">
					  <input type="text" id="LINE_CODE" name="LINE_CODE" class="middle_txt" datatype="0,is_null,30" maxlength="20" size="15" />
				  </td> 
				  <td align="right">货位排名称：</td> 
				  <td align="left" class="table_info_2col_input">
					  <input type="text" id="LINE_NAME" name="LINE_NAME" class="middle_txt" datatype="0,is_null,30" maxlength="50" size="15" />
				  </td>  
			 </tr>  
		    <tr>
			    <td align="right">责任人：</td> 
			   	<td align="left" class="table_info_2col_input">
				  	<input type="text" id="DUTY_PER" name="DUTY_PER" class="middle_txt" datatype="1,is_null,30" maxlength="50" value="" size="15" />
				 </td>
				 <td align="right">责任人电话：</td>   
				 <td align="left" class="table_info_2col_input">
					<input type="text" id="DUTY_TEL" name="DUTY_TEL" class="middle_txt" datatype="1,is_null,30" maxlength="50" value="" size="15" />
				 </td>  
		  	</tr> 	    
		  	<!-- 
		    <tr>
			    <td align="right">入库状态：</td>  
			   	<td align="left" class="table_info_2col_input">
				  <label>
					<script type="text/javascript">
						genSelBoxExp("IN_STATUS",<%=Constant.IN_STATUS%>,"",false,"short_sel","","false",'');
					</script>
				  </label>
			  	</td>  
			 	<td align="right">出库状态：</td> 
			  	<td align="left" class="table_info_2col_input">
				   <label>
					<script type="text/javascript">
						genSelBoxExp("OUT_STATUS",<%=Constant.OUT_STATUS%>,"",false,"short_sel","","false",'');
					</script>
				  </label>
			  	</td>  			  
		  	</tr>
		  	 -->
		   <tr >
		    <td align="right">货位排类型：</td>  
		   	<td align="left" class="table_info_2col_input">
				 <label><script type="text/javascript">
						genSelBoxExp("TYPE",<%=Constant.RES_TYPE%>,"",false,"short_sel","","false",'');
				 </script></label>
			</td>
			<td align="right">库房：</td>
            <td align="left" class="table_info_2col_input">
                <select name="WH_ID" id="WH_ID" class="short_sel">
                 <%-- <option selected value=''>-请选择-</option>--%>
                  <c:forEach items="${wareHouseList}" var="wareHouse">
                      <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                  </c:forEach>
                </select>
            </td> 			   
	 	   </tr>
            <tr>
                <td colspan="4" align="center">
                    <input type="button" id="saveButton" class="normal_btn" onclick="saveAdd();"  value="保存"/>&nbsp;&nbsp;
                    <input type="button" class="normal_btn" id="goBack"  onclick="back();"  value="返回"/>
                </td>
            </tr>
		</table>
		<!-- 基本信息end -->
	</form>
</body>
<script type="text/javascript">
	//添加--begin
	function saveAdd(){
		if(!submitForm("frm")){
			return;
		}
		MyConfirm("确认添加该信息！",save);
	}	
	function save()	{ 
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/addSave.json",addBack,'frm','queryBtn'); 
	}
	function addBack(json)	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			frm.action = "<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/init.do";
			frm.submit();
		}else if(json.returnValue == 3){
			disabledButton(["saveButton","goBack"],false);
			MyAlert("该货位排名字已被占用，请重新输入！");
		}else{
			disabledButton(["saveButton","goBack"],false);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	function back(){
		frm.action="<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/init.do";
		frm.submit();
	}
	//添加--end
</script>
</html>
