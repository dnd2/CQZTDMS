<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增服务资料明细</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：服务资料审批表维护 &gt; 服务资料明细</div>
 <form method="post" name = "fm" id="fm">
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_input"><div align="right"><span class="tabletitle">服务资料名称：</span></div></td>
        <td class="table_query_3Col_input"><div align="left">
            <input name="dataname" type="text" class="middle_txt" size="15" />
        </div></td>
        <td class="table_query_2Col_input">
        	<input name="buttonSearch" type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
        	&nbsp;
        	<input type="button" value="确认" class="normal_btn" onclick="winClose();"/>
        </td>
      </tr>
    </table>
<!--  <table class="table_list" style="border-bottom:1px solid #DAE0EE">-->
<!--    表格名字-->
<!--    <thead>-->
<!--      <tr >-->
<!--        <th width="7%">选择 </th>-->
<!--        <th width="54%" class="tdgray">资料名称</th>-->
<!--        <th width="54%" class="tdgray">单价</th>-->
<!--        </tr>-->
<!--  </table>-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var myPage;

	var flag = true;

	var url = "<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/queryServiceData.json?commind=1";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'全选<input type="checkbox" onclick="selectAll();"/>',dataIndex:'DATA_ID',renderer:myCheckBox,align:'center',width:'4%'},
	           {header:'资料名称',dataIndex:'DATA_NAME',align:'center'},
	           {header:'单价',dataIndex:'PRICE',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		var ipt = '' ;
		ipt+='<input type="checkbox" name="dataId" value="'+record.data.DATA_ID+'">';
		ipt+='<input type="hidden" name="data_name" value="'+record.data.DATA_NAME+'">';
		ipt+='<input type="hidden" name="price" value="'+record.data.PRICE+'">';
		return String.format(ipt);
	}

	function selectAll(){
		var groupCheckBoxs=document.getElementsByName("dataId");
		if(!groupCheckBoxs) return;
		if(flag){
			for(var i=0;i<groupCheckBoxs.length;i++){
				groupCheckBoxs[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<groupCheckBoxs.length;i++){
				groupCheckBoxs[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var ids = document.getElementsByName('dataId');
		var names = document.getElementsByName('data_name');
		var prices = document.getElementsByName('price');
		var is = [];
		var ns = [];
		var ps = [];
		var k = 0;
		for(var i=0;i<ids.length;i++){
			if(ids[i].checked==true){
				is[k] = ids[i].value;
				ns[k] = names[i].value;
				ps[k++] = prices[i].value;
			}
		}
		if(parentContainer.setDates==undefined)
			MyAlert('调用父页面setDates方法出现异常!');
		else{
			parentContainer.setDates(is,ns,ps);
			_hide();
		}
	}
</script>
</body>
</html>