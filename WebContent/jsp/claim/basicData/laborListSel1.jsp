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
<div id="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：索赔工时单价设定&gt;车型大类选择</div>
 <form method="post" name = "fm" id="fm">
 <div class="form-panel">
		<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
    <table class="table_query" border="0" >
      <tr>
        <td style="text-align:right">车型代码：</td>
        <td>
            <input name="wrgroup_code" type="text" class="middle_txt"/>
        </td>
      </tr>
      <tr>
        <td class="table_query_2Col_input" colspan="2" style="text-align:center">
        	<input type="button" name="queryBtn" id="queryBtn" onclick="__extQuery__(1);" class="normal_btn"  value="查询" />
        	&nbsp;
        	<input type="button" value="确认" class="normal_btn" onclick="winClose();"/>
        	&nbsp;
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
    </div>
    </div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</div>
<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/laborListQuery1.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex,width:'3%'},
	           {header:'全选<input type="checkbox" name="code_sel" onclick="selectAll();"/>',dataIndex:'wrgroupId',renderer:myCheckBox,align:'center',width:'4%'},
	           {header:'车型大类',dataIndex:'wrgroupCode',align:'center'},
	           {header:'车型大类',dataIndex:'wrgroupName',align:'center'}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		var ipt = '' ;
		ipt+='<input type="checkbox" name="code" value="'+record.data.wrgroupId+'">'+'<input type="hidden" name="Wcode" value="'+record.data.wrgroupCode+'">';
		return String.format(ipt);
	}

	function selectAll(){
		var groupCheckBoxs=document.getElementsByName("code");
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
		var codes = document.getElementsByName('code');
		var Wcodes = document.getElementsByName('Wcode');
		var cs = [];
		var ws=[];
		var k = 0;
		var j=0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				
				cs[k++] = codes[i].value;
				ws[j++] = Wcodes[i].value;
				
			}
		}
		var parentContainer = __parent() ;
		if(parentContainer.setLaborList==undefined)
			MyAlert('调用父页面setLaborList方法出现异常!');
		else{
			parentContainer.setLaborList(cs,ws);
			_hide();
		}
	}
</script>
</body>
</html>