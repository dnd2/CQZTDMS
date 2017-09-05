<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时查询</title>
<script>
	function doInit(){
   		__extQuery__(1);
	}
</script>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时大类维护</div>
    <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
          <tr>
            <td style="text-align:right">工时大类代码：</td>
            <td>
				<input name="CODE" id="CODE" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>
            <td style="text-align:right">工时大类名称：</td>
            <td>
				<input name="CODE_NAME" id="CODE_NAME" datatype="1,is_null,30"  type="text" class="middle_txt"/>
            </td>                        
           </tr>
		   <tr>    
		   <td colspan="6" style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn"  value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="关闭" name="closeBtn" onclick="_hide();"/>
           </td></tr>
       
  </table>
  </div>
  </div>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </div>
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborBigClassMain/laborPaterClassQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'ID',renderer:mySelect,align:'center'},
				{header: "工时大类代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},				
				{header: "工时大类名称",sortable: false,dataIndex: 'CN_DES',align:'center'}
		      ];

	function mySelect(value,metaDate,record){
		 return String.format("<input type='radio' name='rd' onclick='setPaterClass(\""+record.data.ID+"\",\""+record.data.LABOUR_CODE+"\")' />");
	}
	
	function setPaterClass(paterid,paterCode){
		 //调用父页面方法
		 if(paterid==null || paterid=="null"){
		 	paterid = "";
		 }
		 if(paterCode==null || paterCode=="null"){
		 	paterCode = "";
		 }
		 var parentContainer = __parent();
 		parentContainer.setPaterClass(paterid,paterCode);
 		//关闭弹出页面
 		_hide();
	}
</script>
</body>
</html>