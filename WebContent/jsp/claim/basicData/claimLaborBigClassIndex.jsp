<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时查询</title>
</head>
<body>
<div class="wbox">

  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;索赔工时大类维护</div>
   <form name='fm' id='fm'>
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
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
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
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
				{header: "工时大类代码",sortable: false,dataIndex: 'LABOUR_CODE',align:'center'},				
				{header: "工时大类名称",sortable: false,dataIndex: 'CN_DES',align:'center'},
				{header: "上级大类名称",sortable: false,dataIndex: 'P_CN_DES',align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a  href=\"<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassUpdateInit.do?ID="
			+ value + "\">[修改]</a><a  href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>");
	}
	
//设置超链接 end
  //新增
  function subFun(){
    location="<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassAddInit.do";   
  }
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimLaborBigClassMain/claimLaborBigClassDel.json?ID='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}   
</script>
</body>
</html>