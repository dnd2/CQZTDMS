<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="<%=(request.getContextPath())%>/style/table.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" >
    var globalContextPath ='<%=(request.getContextPath())%>';
    // by chenyu 存用户选择的复选框value的隐藏input的id
    var hideCheckedId = 'hideCheckedId';
    var hideCheckedRegionId = "hideCheckedRegionId";
    var hideCheckedDealerId = "hideCheckedDealerId";
    var hideCheckedMaterialId = "hideCheckedMaterialId";
    var hideCheckedMaterialGroupId = "hideCheckedMaterialGroupId";
    var checkedNumNodeId = "checkedNumNodeId";

	var hideCheckedIds = [ hideCheckedId, hideCheckedRegionId,
			hideCheckedDealerId, hideCheckedMaterialId,
			hideCheckedMaterialGroupId, checkedNumNodeId ];
	var Options;
	// 表格grid的初始高度nodeId
	var tableInitHeight = "tableInitHeight";
	// 表格grid的用户定义高度nodeId
	var tablePreHeight = "tablePreHeight";

	/*function getModel(dealerId,dealerCode,dealerName){
		document.getElementById("dealerCode").value=dealerId;
		document.getElementById("dealerName").value=dealerName;
	}*/
	window.document.onmousedown = function() {
		var srcNode = event.srcElement;
		if (srcNode
				&& srcNode.nodeType !== 3
				&& (srcNode.tagName.toLowerCase() == 'input'
						&& srcNode.getAttribute('type') == 'text' || srcNode.tagName
						.toLowerCase() == 'textarea')) {
			srcNode.focus();
		}
	}
	function cusGetClickValFun() {
	}
	
	function clearCheckedValue(){
		for(var i=0;i<hideCheckedIds.length;i++){
			var _checkNode = document.getElementById(hideCheckedIds[i]);
			if(_checkNode){
				_checkNode.value = "";
			}
		}
	}
	
</script>
<script type="text/javascript" src="<%=(request.getContextPath())%>/js/jslib/tableUtil.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/queryPage/pageDiv.js"></script>
<body>
</body>
