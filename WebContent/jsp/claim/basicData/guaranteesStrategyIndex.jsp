<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% String contextPath = request.getContextPath(); %>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>三包策略查询</title>
	</head>
<body>
<div class="wbox">
	<div class="navigation">
  		<img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔基础数据&gt;整车三包策略维护
  	</div>
  	<form method="post" name="fm" id="fm">
  	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	    <table class="table_query" >
	          <tr>  
				<td style="text-align:right">三包策略类型：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("GAME_TYPE",<%=Constant.GAME_TYPE%>,"",true,"","","false",'');
				    </script>
	            </td>
	            <td style="text-align:right">三包策略状态：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"","","false",'');
				    </script>
	            </td>
	            <td style="text-align:right">三包策略代码：</td>
	            <td align="left"><input type='text'  name='STRATEGY_CODE'  id='STRATEGY_CODE'  class="middle_txt"/></td>
	          </tr>
	          <tr>
	            <td style="text-align:right">三包策略名称：</td>
	            <td align="left"><input type='text'  name='STRATEGY_NAME'  id='STRATEGY_NAME'  class="middle_txt"/></td>
	            <td style="text-align:right">三包规则代码：</td>
	            <td align="left"><input type='text'  name='GUARANTEE_RULE_CODE'  id='GUARANTEE_RULE_CODE'  class="middle_txt"/></td>
	            <td style="text-align:right">三包规则名称：</td>
	            <td align="left"><input type='text'  name='GUARANTEE_RULE_NAME'  id='GUARANTEE_RULE_NAME'  class="middle_txt"/></td>
	          </tr>
	          <tr style="display: none">  
	            <td style="text-align:right">免费保养次数：</td>
	            <td align="left"><input type='text'  name='GUARANTEE_COUNT'  id='GUARANTEE_COUNT' datatype="1,is_digit,20" class="middle_txt"/></td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	          </tr>
			 <tr>
			    <td colspan="6" style="text-align:center">
				   <input class="normal_btn" type="button" name="queryBtn" id="queryBtn" value="查询"  onclick="__extQuery__(1);"/>&nbsp;&nbsp;
				   <input class="normal_btn" type="button" name="button2" value="新增"  onclick="openAdd();"/>	   
			    </td>
		     </tr>   
	  </table>
	  </div>
	  </div>
  </form>
  <!--分页  -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  </div>
	<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/strategyQuery.json";
		var title = null;
		
		var columns = [
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "操作",sortable: false,dataIndex: 'ID',align:'center',renderer:forwordToUpdate},
					{header: "三包策略代码",sortable: false,dataIndex: 'GAME_CODE',align:'center'},
					{header: "三包策略名称",dataIndex: 'GAME_NAME'},	
					{header: "三包策略类型",dataIndex: 'GAME_TYPE',renderer:getItemValue},	
					{header: "状态",dataIndex: 'GAME_STATUS',renderer:getItemValue},			
					//{header: "免费保养次数",dataIndex: 'MAINTAIN_NUM'},
					{header: "三包规则代码",sortable: false,dataIndex: 'RULE_CODE',align:'center'}, 
					{header: "三包规则名称",sortable: false,dataIndex: 'RULE_NAME',align:'center'},
					//{header: "三包策略开始时间",dataIndex: 'START_DATE'},		
					//{header: "三包策略结束时间",dataIndex: 'END_DATE'},		
					{header: "新增时间",sortable: false,dataIndex: 'CREATE_DATE',align:'right'}
			      ];

        //控制表单元素是否显示
		function disableLink(obj,show){
			if(!show){
				obj.disabled = true;
				obj.style.border = '1px solid #999';
				obj.style.background = '#EEE';
				obj.style.color = '#999';
			}else{
				obj.disabled = false;
				obj.style = '';
			}
		}

        //打开新增页面
		function openAdd(){
			var aLink = document.getElementById('addHref');
			aLink.click();
		}

		//跳转到修改页面
		function forwordToUpdate(value,meta,record){
			return String.format("<a  href=\"<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/updateStrategyInit.do?ID="
					+ value + "\" >[修改]</a>");
			//return String.format("<a href=\"#\" onclick=\"toUpdatePage("+value+")\">[修改]</a>");
		}

		//在OpenHtmlWindow打开修改页面
		function toUpdatePage(id){
			var tarUrl = "<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/updateStrategyInit.do?ID="+id;
			var width=900;
			var height=500;
			var screenW = document.viewport.getWidth()*9/10;	
			var screenH = document.viewport.getHeight()*9/10;
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			
			OpenHtmlWindow(tarUrl,width,height);
		}
		
	</script>
		<a id="addHref" href="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/addStrategyInit.do"/>
</body>
</html>