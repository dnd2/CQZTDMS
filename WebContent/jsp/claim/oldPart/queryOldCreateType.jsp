<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单管理</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件运输管理</div>
  <form id="fm" name="fm">
    <table class="table_query">
       <tr>
       	<td align="right">类型：</td>
         <td>
         	<select name="selectType" id="selectType" class="short_sel">
         					<option value="-1">-请选择-</option>
							<option value="1">库区</option>
							<option value="2">货架</option>
							<option value="3">层数</option>
						</select>
         </td>
         <td align="right" nowrap="nowrap">代码：</td>
         <td><input id="codeold" name="codeold" type="text" maxlength="20" class="middle_txt" datatype="1,is_null,50"></td>
       </tr>
       <tr>
       		 <td align="right" nowrap="nowrap">名称：</td>
         	<td><input id="nameold" name="nameold" type="text" maxlength="20" class="middle_txt" datatype="1,is_null,50"></td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" name="qryButton"value="查询"  onClick="__extQuery__(1);">
           <input class="normal_btn"  type="button" name="button1" value="新增"  onClick="location='oldTypeCreateAdd.do';"/>
          </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/queryOldCreateType.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "操作",sortable: false,dataIndex: 'ID',renderer:operateLink,align:'center'},
  				{header: "类型",sortable: false,dataIndex:'NAME_TYPE',renderer:ToFamt,align:'center'},
  				{header: "代码",dataIndex: 'CODE_OLD',align:'center'},
  				{header: "名称",dataIndex: 'NAME_OLD',align:'center'},
  				{header: "上报人", dataIndex: 'NAME', align:'center'},
  				{header: "上报时间", dataIndex: 'CREATE_DATE', align:'center'}
  		      ];
  		     
   __extQuery__(1);
 //超链接设置
   function operateLink(value,meta,record){
	   var width=800;
	   var height=500;
	   var screenW = window.screen.width-30;	
	   var screenH = document.viewport.getHeight();
	   if(screenW!=null && screenW!='undefined')
		   width = screenW;
	   if(screenH!=null && screenH!='undefined')
		   height = screenH;
   	   var status=record.data.TRANSPORT_STATUS;
   	   var link = "<a href='#' onclick=\"modifyDetail("+value+")\">[修改]</a>";
	   return String.format(link);
  	 };
 	//修改
   function modifyDetail(id){
	   var i_url="<%=contextPath %>/claim/oldPart/ClaimOldPartTransportManager/oldCreateUpdate.do?id="+id;
	   location.href=i_url;
   }
   function ToFamt(value,meta,record){
   		var text="";
   		if(value==1){
   			text="库区";
   		}
   		if(value==2){
   			text="货架";
   		}
   		if(value==3){
   			text="层数";
   		}
		return String.format(text);
   }
</script>
</body>
</html>