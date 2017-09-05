<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔外出维修费用设定</title>
</head>
<body>
<div class="wbox">
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据管理&gt;索赔外出维修费用设定</div>
   <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
   <table  class="table_query">
   			<tr>
	          	<td class="right">项目代码：</td>
	          	<td><input name="CODE" type="text" id="CODE"  datatype="1,is_null,20"  class="middle_txt" /></td>
	          	<td  class="right">项目名称：</td>
	          	<td><input name="CODE_NAME" type="text" id="CODE_NAME"  datatype="1,is_null,30"  class="middle_txt"/></td> 				
   			</tr>
		   <tr>    
		   <td  colspan="4" class="center">
            <input class="normal_btn" type="button" name="queryBtn" value="查询"  onclick="__extQuery__(1)"/>
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
<%-- <form name="form1" style="display:none"> --%>
<%-- <table align="center"  class=csstable >--%>
<%--	  <tr>--%>
<%--	  		<td align="center"><input type="button" name="return1"  class="normal_btn" value="全部下发" onclick=""/></td>--%>
<%--	  </tr>--%>
<%--  </table>--%>
<%-- </form>  --%>
<script type="text/javascript" >
<%--document.form1.style.display = "none";--%>
<%--var HIDDEN_ARRAY_IDS=['form1'];--%>
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
{header: "操作",sortable: false,dataIndex: 'FEE_ID',renderer:myLink ,align:'center'},
				{header: "项目代码",sortable: false,dataIndex: 'FEE_CODE',align:'center'},
				{header: "项目名称",sortable: false,dataIndex: 'FEE_NAME',align:'center'}
		      ];

//设置超链接  begin
	
	//修改的超链接设置
	function myLink(value,meta,record){
	var str = "";
	var code = record.data.FEE_CODE;
	var createBy = record.data.CREATE_BY;
	if(createBy == "-1"){
		str = "<a  href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>"
	} else if(createBy == "0"){
		str = "<a  href=\"#\" onclick=\"updateIt(" + value + ") ;\">[修改]</a>" +"<a href=\"#\" onclick='sel(\""+value+"\")'>[删除]</a>";
	} else if (createBy == "1") {
		str = "" ;
	}else {
		str = "<a  href=\"#\" onclick=\"updateIt(" + value + ") ;\">[修改]</a>" ;
	}
	    return String.format(str);
	}
	
//设置超链接 end
  //新增
  function subFun(){
    var url = "<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeAddInit.do" ;
	OpenHtmlWindow(url, 500, 300, '索赔外出维修费用新增');
  }
  
  function updateIt(value) {
		var url = "<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeUpdateInit.do?FEE_ID=" + value ;
		OpenHtmlWindow(url, 500, 300, '索赔外出维修费用新增');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
  
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeDel.json?FEE_ID='+str,delBack,'fm','');
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
</div>
</body>
</html>