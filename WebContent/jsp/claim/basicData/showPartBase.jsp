<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询新增工时代码</title>
</head>
<div class="wbox">
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：补录&gt;配件代码选择</div>
 <form method="post" name = "fm" id="fm">
   <div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table class="table_query" border="0" >
      <tr>
        <td style="text-align:right">配件代码：</td>
        <td>
            <input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/>
        </td>
        <td style="text-align:right">配件名称：</td>
        <td>
            <input name="PART_NAME" type="text" class="middle_txt" id="PART_NAME"/>
        </td>
      </tr>
        <tr>
        <td colspan="4"  style="text-align:center">
        	<input type="button" id="queryBtn" name="queryBtn"  onclick="__extQuery__(1);" class="normal_btn"  value="查询" />&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
    </div>
    </div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
var flag = true ;

var url = "<%=contextPath%>/claim/basicData/WorkRankMain/partListQuery.json";
			
var title = null;

var columns = [
           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
           {header:'选择',dataIndex:'PART_CODE',renderer:myCheckBox,align:'center',width:'4%'},
           {header:'配件代码',dataIndex:'PART_CODE',align:'center'},
           {header:'配件名称',dataIndex:'PART_NAME',align:'center'}
	   		]; 
	      
function myCheckBox(value,meta,record){
	var ipt = '' ;
	ipt+='<input type="radio" name="code" value="'+record.data.LABOUR_CODE+'" onclick="getLabourData(\''+ record.data.PART_CODE +'\',\''+ record.data.PART_NAME +'\')" />';
	return String.format(ipt);
}
function getLabourData(code, name){
			if(!__parent().setPartBase)
				MyAlert('调用父页面setLaborList方法出现异常!');
			else{
				__parent().setPartBase(code,name);  //从子层返回数据
				_hide();
			}
}
</script>
</div>
</body>
</html>