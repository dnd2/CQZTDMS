<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新增批售项目</title>
<script type="text/javascript">
</script>
</head>
<body>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 集团客户管理&gt;集团客户信息管理&gt;批售项目</div>
 <form method="post" name="fm" id="fm">
 <input type="hidden" id="vin2"/>
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E" class="table_edit">
   		<tr>
          <th colspan="6"><img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 基本信息</th>
        </tr>
		  <tr>
		    <td align="right">项目代码：</td>
		    <td class="table_info_2col_input">
		    	<input type="text" name="pactNo" id="pactNo" datatype="0,is_null,30" maxlength="20" class="middle_txt"/>
		    </td>
		    <td align="right">项目名称：</td>
		    <td class="table_info_3col_input">
		    	<input type="text" name="pactName" id="pactName" datatype="0,is_null,30" maxlength="30" class="middle_txt"/>
		    </td>
	      </tr> 
		  <tr>
		    <td align="right">状态：</td>
		    <td class="table_info_2col_input">
              <script type="text/javascript">
 				 genSelBoxExp("status",<%=Constant.STATUS%>,"",false,"short_sel","","false",'');
			  </script>
		    </td>
		   <td align="right">批售类型：</td>
		    <td class="table_info_3col_input">
              <script type="text/javascript">
 				 genSelBoxExp("pactType",<%=Constant.FLEET_TYPE%>,"",false,"short_sel","","false",'');
			  </script>
		    </td>
	      </tr> 
	      <tr>
		    <td align="right">是否允许报备:</td>
		    <td class="table_info_2col_input">
              <script type="text/javascript">
 				 genSelBoxExp("isAllowApply",<%=Constant.IF_TYPE%>,<%=Constant.IF_TYPE_NO%>,false,"short_sel","","false",'');
			  </script>
		    </td>
		   <td align="right"></td>
		    <td>
		    </td>
	      </tr> 
          <tr >
            <td align="right">说明：</td>
            <td colspan="3">
              <textarea name="remark" id="remark" rows="5" cols="80" ></textarea>
            </td> 
          </tr>
        </table>        
   <!-- 按钮 begin -->
   <table class="table_list">
      <tr > 
      	<th height="12" align=center>
			<input type="button" onClick="addSpeciaExpenses()" class="normal_btn" style="width=8%" value="保存"/>&nbsp;&nbsp;
			<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
	   	</th>
	  </tr>
   </table>
   <!-- 按钮 end -->
</form>
<script type="text/javascript">
	//添加
	function addSpeciaExpenses()
	{
		MyConfirm("确认新增！",addSpecia);
	}
	
	function addSpecia()
	{
		makeNomalFormCall("<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFact/addFact.json",addSpeciaBack,'fm','queryBtn'); 
	}
	
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			$('fm').action= "<%=contextPath%>/sales/fleetmanage/fleetInfoManage/FleetFact/queryFleetFact.do";
			$('fm').submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	
</script>
</body>
</html>
