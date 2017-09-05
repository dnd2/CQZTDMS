<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车型组维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="wbox">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;车型组维护</div>
<form name='fm' id='fm'>
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table  class="table_query" >
          <tr>
            <td style="text-align:right"><label>车型组类型：</label></td>
            <td>
                 <script type="text/javascript">
	              genSelBoxExp("WRGROUP_TYPE",<%=Constant.WR_MODEL_GROUP_TYPE%>,"<%=Constant.WR_MODEL_GROUP_TYPE_01%>",false,"","","false",'<%=Constant.WR_MODEL_GROUP_TYPE_02%>');
	            </script>
            </td>	         
            <td style="text-align:right">车型组代码：</td>
            <td>
            <input type="text"  name="WRGROUP_CODE"  id='WRGROUP_CODE'  class="middle_txt" datatype="1,is_null,20"/>
            </td>
          </tr>
		 <tr>
		  <td colspan="4" style="text-align:center">
            <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>
           	<input class="normal_btn" type="button" name="button2" value="新增"  onclick="subFun();"/>
          </td> 	
		 </tr>
  </table>
  </div>
  </div>
 <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end -->
</form>
 <form name="form1" style="display:none"> 
 <table align="center"  class=csstable >
	  <tr style="display: none">
	  		<td align="center"><input type="button" name="button"  class="normal_btn" value=" " onclick=""/></td>
	  </tr>
  </table>
 </form> 
 </div>
<script type="text/javascript" >
document.form1.style.display = "none";
var HIDDEN_ARRAY_IDS=['form1'];
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/ClaimModelMain/claimModelQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				//{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'WRGROUP_ID',renderer:myCheckBox},
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'WRGROUP_ID',renderer:myLink ,align:'center'},
				{header: "车型组类别",sortable: false,dataIndex: 'WRGROUP_TYPE',align:'center',renderer:getItemValue},
				{header: "车型组代码",sortable: false,dataIndex: 'WRGROUP_CODE',align:'center'},
				{header: "车型组名称",sortable: false,dataIndex: 'WRGROUP_NAME',align:'center'},
				{header: "车型组车型数",sortable: false,dataIndex: 'GROUP_COUNT',align:'center',renderer:mySelect},
				{header: "保养费用",sortable: false,dataIndex: 'FREE',align:'center'},
				{header: "新车整备费",sortable: false,dataIndex: 'NEW_CAR_FEE',align:'center'},
				{header: "首保里程",sortable: false,dataIndex: 'END_MILEAGE',align:'center'},
				{header: "首保时间/天",sortable: false,dataIndex: 'MAX_DAYS',align:'center'}
		      ];

//设置超链接  begin
	function mySelect(value,meta,record){
	 		return String.format(
	        "<a href=\"#\" onclick='selbyid(\""+record.data.WRGROUP_ID+"\",\""+record.data.WRGROUP_TYPE+"\")'>["+ value +"]</a>");
	}
//详细页面
	function selbyid(value1,value2){
		OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelDetail.do?WRGROUP_ID='+value1+'&WRGROUP_TYPE='+value2,900,500);
	}		
	//修改的超链接设置
	function myLink(value,meta,record){
	    return String.format(
         "<a href=\"#\"  onclick=\"subUpdate(" + value+ ")\">[修改]</a><a href=\"#\"  onclick='sel2(\""+value+"\")'>[删除]</a>");
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}	
	
//设置超链接 end
  //新增
  function subFun(){
    var url="<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelAddInit.do";   
    OpenHtmlWindow(url, 650, 380, '售后车型组新增');
  }
  
  function subUpdate(value) {
	  var url = "<%=contextPath%>/claim/basicData/ClaimModelMain/claimModelUpdateInit.do?WRGROUP_ID=" + value ;
	  
	  OpenHtmlWindow(url, 650, 380, '售后车型组修改');
  }
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
function sel2(str){
	MyConfirm("是否确认删除？",del2,[str]);
} 
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeDel.json?FEE_ID='+str,delBack,'fm','');
}
function del2(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimModelDel.json?did='+str,delBack,'fm','');
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
//下发方法：
function sendAll(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/sendAll.json',sendAllBack,'fm','');
}
//下发后的回调方法：
function sendAllBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("新增成功！但经销商："+json.returnValue+"原先已经维护了！");
	    }else {
	    	MyAlert("下发成功 !");
	    }
	} else {
		MyAlert("下发失败！请联系管理员！");
	}
}
</script>
</body>
</html>
