<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售人员区域范围设置</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
 基础信息管理 &gt; 配件基础信息维护 &gt; 配件销售人员区域维护 &gt; 修改</div>

  <form method="post" name="fm" id="fm">
  <input type="hidden" name="userId" id="userId" value="${map.USER_ID}"/>
  <input type="hidden" name="userType" id="userType" value="${userType}"/>
  <input type="hidden" name="DEALER_CODE" id="DEALER_CODE" />
   <!-- 查询条件 begin -->
  <table class="table_query">
 		<tr>
            <td class="table_info_3col_label_4Letter">用户账号：</td>
            <td class="table_info_3col_input">
				${map.ACNT}
            </td>
            <td class="table_info_3col_label_4Letter">用户名称：</td>
            <td class="table_info_3col_input">
				${map.NAME}
            </td>
		    <td class="table_info_3col_label_4Letter"></td>
            <td>
				<input type="button" class="normal_btn" value="新增" onclick="showPartDealer('${map.USER_ID}','S','DEALER_ID','true','',true,true,false,'','${userType}')">
				<input type="button" class="normal_btn" value="返回" onclick="goBack()">
            </td>
          </tr>             
  </table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
  <form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="center">
    		<input class="normal_btn" id="deleteBtn" type="button" value="删 除" onclick="deleteConfirm()">
       </th>
  	  </tr>
   </table>
  </form>
<!--页面列表 begin -->
<script type="text/javascript" >

document.form1.style.display = "none";

var HIDDEN_ARRAY_IDS=['form1'];

	var myPage;
//查询路径
	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/queryScopeByUserList.json";
				
	var title = null;

	var columns = [
				{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />", width:'8%',sortable: false,dataIndex: 'DEFINE_ID',renderer:myCheckBox},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', style:'text-align: left;'},
				{header: "用户类型", dataIndex: 'USER_TYPE', align:'center',renderer:getUserType}
		      ];
		      
//设置超链接  begin      

	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{
	 	return String.format("<input type='checkbox' name='ids' value='" + value + "'/>");
    }

	//获取人员类型
	function getUserType(value,meta,record)
	{
		var partUserType = "3"; //配件销售人员
		var str = "配件销售人员";
		if(partUserType != value)
		{
			str = "整车销售人员";
		}
	  	return String.format(str);
	}

	//删除提醒
	function deleteConfirm()
	{
		var chk = document.getElementsByName("ids");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;
			}
		}
      if(cnt==0)
      {
           MyAlert("请选择要删除的经销商！");
           return;
      }
		MyConfirm("确认删除？",deleteScope);
	}
	
	//删除操作
	function deleteScope()
	{
		makeNomalFormCall("<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/deletePartSalesScope.json",showDeleteValue,'fm','deleteBtn'); 
	}
	
	//删除回调函数
	function showDeleteValue(json)
	{
		if(json.returnValue == '1')
		{
			MyAlert("删除成功！");
			__extQuery__(1);
		}else
		{
			MyAlert("删除失败！请联系系统管理员！");
		}
	}


	function goBack()
	{
		fm.action = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesScopeManager/partSalesScopeQueryInit.do";
		fm.submit();
	}

  
//设置超链接 end
	
</script>
<!--页面列表 end -->

</body>
</html>