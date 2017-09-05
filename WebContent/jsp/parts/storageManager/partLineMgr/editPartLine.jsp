<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%String contextPath=request.getContextPath();%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>货位排修改</title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：配件管理>基础信息管理>仓储相关信息维护>排维护>修改
	</div>

<form name="frm" method="post" id="frm">
<table  width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
	<tr> <th colspan="4"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
	 <tr>
  <td align="right">货位排代码：</td>
   	<td align="left" class="table_info_2col_input">
		<input type="text" id="LINE_CODE" name="LINE_CODE" class="middle_txt" datatype="0,is_null,30" value="<c:out value="${map.LINE_CODE}"/>" maxlength="20" size="15" />
	 	<input type="hidden" name="LINE_ID" id="LINE_ID" value="<c:out value="${map.LINE_ID}"/>"/>
	  </td> 
	  <td align="right">货位排名称：</td> 
	  <td align="left" class="table_info_2col_input">
		  <input type="text" id="LINE_NAME" name="LINE_NAME" class="middle_txt" datatype="0,is_null,30" value="<c:out value="${map.LINE_NAME}"/>" maxlength="50" size="15" />
	  </td>  
  </tr>  
    <tr>
    <td align="right">责任人：</td> 
   	<td align="left" class="table_info_2col_input">
		  <input type="text" id="DUTY_PER" name="DUTY_PER" class="middle_txt" maxlength="50" value="<c:out value="${map.DUTY_PER}"/>" size="15" />
	  </td>
	  <td align="right">责任人电话：</td>   
	  <td align="left" class="table_info_2col_input">
		  <input type="text" id="DUTY_TEL" name="DUTY_TEL" class="middle_txt" maxlength="50" value="<c:out value="${map.DUTY_TEL}"/>" size="15" />
	  </td>  
  </tr> 
  <!-- 
      <tr >
    <td align="right">入库状态：</td>  
   	<td align="left" class="table_info_2col_input">
		  <label>
					<script type="text/javascript">
						genSelBoxExp("IN_STATUS",<%=Constant.IN_STATUS%>,"<c:out value="${map.IN_STATUS}"/>",false,"short_sel","","false",'');
					</script>
				</label>
	  </td>  
	  <td align="right">出库状态：</td> 
	  <td align="left" class="table_info_2col_input">
		   <label>
					<script type="text/javascript">
						genSelBoxExp("OUT_STATUS",<%=Constant.OUT_STATUS%>,"<c:out value="${map.OUT_STATUS}"/>",false,"short_sel","","false",'');
					</script>
				</label>
	  </td>  
  </tr>
   -->
   <tr >
    <td align="right">库区类型：</td>  
   	<td align="left" class="table_info_2col_input">
		  <label>
			<script type="text/javascript">
				genSelBoxExp("TYPE",<%=Constant.RES_TYPE%>,"<c:out value="${map.TYPE}"/>",false,"short_sel","","false",'');
			</script>
		</label>
	  </td> 
	  <td align="right">是否有效：</td>  
	   	<td align="left" class="table_info_2col_input">
			  <label>
				<script type="text/javascript">
					genSelBoxExp("STATE",<%=Constant.STATUS%>,"<c:out value="${map.STATE}"/>",false,"short_sel","","false",'');
				</script>
			</label>
	 </td>
  </tr> 
  <!-- 
  <tr >
	  <td align="right">库房：</td>
       <td align="left" class="table_info_2col_input">
           <select name="WH_ID" id="WH_ID" class="short_sel">
           	<option value="">--请选择</option>
             <c:forEach items="${wareHouseList}" var="wareHouse">
	             <c:if test="${wareHouse.WH_ID==map.WH_ID}">
	             	<option select value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	             </c:if>
	             <c:if test="${wareHouse.WH_ID!=map.WH_ID}">
                 	<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                 </c:if>
             </c:forEach>
           </select>
       </td> 
  	</tr>
   -->
	</table>
	<table align=center>
      <tr> 
      	<th height="12" align=center>
			<input type="button" class="cssbutton" id="saveButton" onclick="editSave()" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" class="cssbutton" onclick="back();" id="goBack" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
 	</table>
</form>
<script type="text/javascript" >
	function back(){
		frm.action="<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/init.do";
		frm.submit();
	}
	//添加
	function editSave()
	{
		if(!submitForm("frm")){
			return;
		}
		MyConfirm("确认修改该信息！",save);
	}
	
	function save()
	{
		disabledButton(["saveButton","goBack"],true);
		makeNomalFormCall("<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/editSave.json",saveBack,'frm','queryBtn'); 
	}
	
	function saveBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			frm.action = "<%=contextPath%>/parts/storageManager/partLineMgr/PartLineMgr/init.do";
			frm.submit();
		}else if(json.returnValue == 3){
			disabledButton(["saveButton","goBack"],false);
			MyAlert("该库区名字已被占用，请重新输入！");
		}else{
			disabledButton(["saveButton","goBack"],true);
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
