<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String contextPath = request.getContextPath(); %>
<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>维修时间预警规则查询</title>
</head>
<body>
<div class="wbox">
	<div class="navigation">
  		<img src="<%=contextPath%>/img/nav.gif" />当前位置：售后服务管理&gt;索赔基本数据&gt;时间预警规则维护
  	</div>
  	<form method="post" name="fm" id="fm">
  	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	    <table class="table_query" >
	          <tr>  
				<td style="text-align:right">预警规则：</td>
	            <td align="left">
					<input type='text'  name='WAINING_CODE'  id='WAINING_CODE'  class="middle_txt"/>
	            </td>
	            <td style="text-align:right">预警说明：</td>
	            <td align="left">
					<input type='text'  name='WAINING_REMARK'  id='WAINING_REMARK'  class="middle_txt"/>
	            </td>
	            <td style="text-align:right">预警类型：</td>
	            <td align="left">
	            	<script type="text/javascript">
						 genSelBoxExp("WARNING_TYPE",<%=Constant.SWANINGTIME_TYPE%>,"",true,"","","",'');
				    </script>
	            </td>
	          </tr>
	          <tr>
	            <td style="text-align:right">预警等级：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("WARNING_LEVEL",<%=Constant.SWANINGTIME_LEVEL%>,"",true,"","","",'');
				    </script>
	            </td>
	            <td style="text-align:right">状态：</td>
	            <td align="left">
					<script type="text/javascript">
						 genSelBoxExp("STATUS",<%=Constant.STATUS%>,<%=Constant.STATUS_ENABLE%>,true,"","","",'');
				    </script>
				</td>
				<td colspan="2"></td>
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
	<!--分页  -->
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页END  -->
  </form>
  </div>
	<script type="text/javascript">
		var myPage;
		var url = "<%=contextPath%>/claim/basicData/WarningTime/warningTimeQuery.json";
		var title = null;
		
		var columns = [
		            
					{header: "序号",sortable: false,align:'center',renderer:getIndex},
					{header: "操作",sortable: false,dataIndex: 'ID',align:'center',renderer:forwordToUpdate},
					{header: "预警规则",sortable: false,dataIndex: 'WARNING_CODE',align:'center'},
					{header: "预警说明",sortable: false,dataIndex: 'WAINING_REMARK',align:'center'},
					{header: "预警类型",dataIndex: 'WAINING_TYPE',renderer:getItemValue},	
					{header: "预警等级",dataIndex: 'WAINING_LEVEL',renderer:warningTypeImg},	
					{header: "预警起时间（日）",dataIndex: 'WARNING_TIME_START'},			
					{header: "预警止时间（日）",dataIndex: 'WARNING_TIME_END'},
					{header: "法规条款",sortable: false,dataIndex: 'CLAUSE_STATUTE',align:'center'}, 
					{header: "状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue}
			      ];
		//跳转到修改页面
		function forwordToUpdate(value,meta,record){
			return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
		}
		//控制图片
		function warningTypeImg(value,meta,record){
			if(value == <%=Constant.SWANINGTIME_LEVEL_01 %>)
			{
				return String.format("<img src='<%=contextPath%>/img/red.jpg'/>");
			}
			else if(value == <%=Constant.SWANINGTIME_LEVEL_02 %>)
			{
				return String.format("<img src='<%=contextPath%>/img/ced.jpg'/>");
			}
			else
			{
				return String.format("<img src='<%=contextPath%>/img/yellow.jpg'/>");
			}
		}
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
			var url = "<%=contextPath%>/claim/basicData/WarningTime/warningTimeAddInit.do";
			OpenHtmlWindow(url, 950, 520, '时间预警规则新增');
			<%-- window.location.href =  "<%=contextPath%>/claim/basicData/WarningTime/warningTimeAddInit.do"; --%>
		}

		function updateIt(value) {
			var url = "<%=contextPath%>/claim/basicData/WarningTime/warningTimeUpdateInit.do?ID=" + value;
			OpenHtmlWindow(url, 950, 520, '时间预警规则修改');
			
			/* fm.action = url ;
			fm.submit() ; */
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
</body>
</html>