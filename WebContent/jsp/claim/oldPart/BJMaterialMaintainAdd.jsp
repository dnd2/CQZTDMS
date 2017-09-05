<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;北京车型组维护新增</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
     
       <tr>
          <td class="table_query_2Col_label_6Letter">车型代码：</td>
         <td><input id="group_code" name="groupCode" value="" type="text" class="middle_txt"></td>
          <td class="table_query_2Col_label_6Letter">车型名称：</td>
         <td><input id="group_name" name="groupName" value="" type="text" class="middle_txt"></td>
       </tr>
     
       <tr>
         <td align="center" colspan="2" nowrap="nowrap">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         </td>
         <td>
           <input class="normal_btn" type="button" name="qryButton" value="保 存"  onClick="save();">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/BJMaterialMaintainAddQuery.json";
				
   var title = null;

   var columns = [
  			
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "选择", dataIndex: 'GROUP_ID', align:'center',renderer:seled},
  				{header: "车型代码", dataIndex: 'GROUP_CODE', align:'center'},
  				{header: "车型名称", dataIndex: 'GROUP_NAME', align:'center'}
  		      ];

	function seled(value,meta,record){
		
        	return "<input type='checkbox' name='checkCode' id='checkCode' value='"+ value + "' />";
        
    }

    function save(){
    	  if (confirm('确定新增吗？')){    
    	var chk = document.getElementsByName("checkCode");
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
             MyAlert("请选择车型！");
        }else{
	        var codes = "";
			var ids = "";
	        for(var i=0;i<l;i++)
			{        
				if(chk[i].checked)
				{
					if(chk[i].value)
					{
						var arr = chk[i].value.split("||");
						if(ids)
						ids += "," + arr[0];
				    	else
				        ids = arr[0];
				        if(codes)
						codes += "," + arr[1];
				    	else
				        codes = arr[1];
				    }    
				}				
			}
        }
      
	
        
    	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/BJMaterialMaintainAddSave.do?ids="+ids;
 	   fm.method="post";
       fm.submit();
        }
    }
</script>
</BODY>
</html>