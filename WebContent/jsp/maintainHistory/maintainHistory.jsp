<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="padding:2px;background-color:#DAE0EE;color:#08327e;">
	<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />&nbsp;SGM维修历史
	<input type="hidden" name="vin" id="vin" value="" />	
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
	function setPrivateVin(vin){
		$("vin").value = vin;
	}
	function addApyOrder(){
		disableBtn($("addBtn"));
		makeNomalFormCall('<%=request.getContextPath()%>/authcar/apply/AuthCarApplyAction/authCarApplySubmit.json?',showPrivateResult,'fm');
	}
	function showPrivateResult(json){
		if(json.ACTION_RESULT == '1'){
			MyConfirm("保存提交！点击确认返回查询界面或者点击左边菜单进入其他功能！",showPrivateConfirm);
		}
	}
	function showPrivateConfirm(){
		window.location.href = "<%=request.getContextPath()%>/authcar/apply/AuthCarApplyAction/authCarApplyPre.do"
	}
	function goPrivateBack(){
		history.go(-1);
	}
	var theTime=0;
	var url = "<%=request.getContextPath()%>/common/MaintainHistoryAction/MaintainHistorySearch.json?command=1";
	//设置表格标题
	var title= null;
	var columns = [
					{header: "序号",  renderer:getIndex}, //设置序号的方式
					{id:'action',header: "工单号",  dataIndex: 'roNo', renderer:myPrivateLink},
					{header: "开单日期",  dataIndex: 'startTime'},
					{header: "车牌号",  dataIndex: 'license'},
					{header: "工单类型",   dataIndex: 'roType'},
					{header: "维修类型",   dataIndex: 'repairType'},
					{header: "进厂里程",   dataIndex: 'inMileage'},
					{header: "换表", width:"5%",  dataIndex: 'isChangeMileage', renderer:getItemPic}
				  ];
	//设置超链接
	var bNO = "";
    function myPrivateLink(value,metaDate,record){
        var vin = $("vin").value;
        theTime++;
        bNO = bNO + record.data.balanceNo + ",";
		return String.format("<a href=\"javascript:OpenHtmlWindow('<%=request.getContextPath()%>/common/MaintainHistoryAction/MaintainHistoryDetailSearch.do?orderNo="+record.data.balanceNo+"&vin="+vin+"&bNO="+bNO+"&theTime="+theTime+"',800,600) \" >"+value+"</a>");
    }
</script>
