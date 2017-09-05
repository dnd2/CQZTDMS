<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件车系关系</title>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;配件车系关系</div>
   <table  class="table_query">
          <tr>
            <td class="table_query_2Col_label_6Letter">配件代码：</td>
            <td><input name="PART_OLDCODE" type="text" id="PART_OLDCODE" class="middle_txt"/></td>
            <td class="table_query_2Col_label_6Letter">配件名称：</td>
          	<td><input name="PART_CNAME" type="text" id="PART_CNAME" class="middle_txt"/></td>
           </tr>         
          <tr>
          	<td class="table_query_2Col_label_6Letter">配件件号：</td>
          	<td><input name="PART_CODE" type="text" id="PART_CODE" class="middle_txt" /></td>
          	<td class="table_query_2Col_label_6Letter">配件类型：</td>
          	<td>
          	  <script type="text/javascript">
					 genSelBoxExp("PART_TYPE",9202,"",true,"short_sel","","false",'');
			    </script>
          	</td>
          </tr>
           <tr>
          	<td class="table_query_2Col_label_6Letter">车系代码：</td>
          	<td><input name="GROUP_CODE" type="text" id="GROUP_CODE" class="middle_txt" /></td>
          	<td class="table_query_2Col_label_6Letter">车系名称：</td>
          	<td><input name="GROUP_NAME" type="text" id="GROUP_NAME" class="middle_txt"/></td>
          </tr>
           <tr>
          	<td class="table_query_2Col_label_6Letter">结算基地：</td>
          	<td>
          	   <script type="text/javascript">
					 genSelBoxExp("PART_IS_CHANGHE",9541,"",true,"short_sel","","false",'');
			    </script>
		    </td>
          </tr>
		  <tr>    
		  <td colspan="4" align="center">
		  <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1);"/>
		   <input class="normal_btn" type="button" name="button1" value="新增"  onclick="add_part_series();"/>
		  <input class="normal_btn" type="button" name="button2" value="批量删除"  onclick="deletAll();"/>
		  </td>
          </tr>
       
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/LaborPriceMain/part_series.json?COMMAND=1";
	var title = null;
	var controlCols = "<input type=\"checkbox\" name=\"claimAll\" onclick=\"selectAll(this,'part_serier_id')\"/>全选";
	var columns = [
				{header: '配件代码',align:'center',dataIndex:'PART_OLDCODE'},
				{header: controlCols,align:'center',renderer:createCheckbox},
				{header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',align:'center'},
				{header: '结算基地',align:'center',dataIndex:'PART_IS_CHANGHE',renderer:getItemValue},				
				{header: "配件件号",sortable: false,dataIndex: 'PART_CODE',align:'center'},
			    {header: "配件类型",sortable: false,dataIndex: 'PART_TYPE',align:'center',renderer:getItemValue},
				{header: "车型代码",sortable: false,dataIndex: 'GROUP_CODE',align:'center'},
				{header: "车型名称",sortable: false,dataIndex: 'GROUP_NAME' ,align:'center'},
				{header: "新增人",sortable: false,dataIndex: 'CREATE_BY' ,align:'center'},
				{header: "新增时间",sortable: false,dataIndex: 'CREATE_DATE' ,align:'center'},
				{header: "修改人",sortable: false,dataIndex: 'UPDATE_BY' ,align:'center'},
				{header: "修改时间",sortable: false,dataIndex: 'UPDATE_DATE' ,align:'center'},
				{header: "操作",sortable: false,dataIndex: 'ID' ,align:'center',renderer:UpdateInit }
		      ];
  function UpdateInit(value,meta,record)
  {
	    return String.format(
         "<a href=\"<%=contextPath%>/claim/basicData/LaborPriceMain/updatepart_series.do?id="+ 
         	value + "\">[修改]</a>");
  }	
  
  function deletAll()
  {
     var part_serier_ids = document.getElementsByName('part_serier_id');  
     var fag = false;
     for(var i = 0 ;i < part_serier_ids.length;i++)
     {
        if(part_serier_ids[i].checked)
		{     
			   fag = true;
		}
	    
     }
     if(fag)
     {
        MyConfirm("是否确认删除？",delet);
     }else
     {
        MyAlert('请选择要删除的配件车系关系');
     }
  }
  
  function delet()
  {
	   fm.action = "<%=contextPath%>/claim/basicData/LaborPriceMain/delet_part_series.do";
	   fm.submit();
  }	
    
   function createCheckbox(value,meta,record)
   {
  		resultStr = String.format("<input type=\"checkbox\" name=\"part_serier_id\" value=\""+record.data.ID+"\"/>");
		return resultStr;
	}
	
	 function add_part_series()
	 {
	     fm.action = "<%=contextPath%>/claim/basicData/LaborPriceMain/add_part_series.do";
	     fm.submit();
	 }
  
  		      
</script>
</body>
</html>