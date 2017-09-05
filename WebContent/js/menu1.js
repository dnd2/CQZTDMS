var ligerTab=null;

//============================消息提醒 start=================================
var noticeUrl = global_Path + "/sysmng/notice/dealerNotice/DealerNoticeAction/dealerNoticeIndex.do";
//============================消息提醒 end===================================

//============================经销商tip提示信息变量 start=================================
// 开票url
var kpUrl = global_Path+'/claim/application/DealerNewKp/dealerKpQuerydlInit.do';
var kpMenuId;
// 调件url
var djUrl = global_Path+'/claim/oldPart/ClaimBackPieceBackListOrdManager/addOldReturnOrder.do';
var djMenuId;
// 广宣url
var gxUrl = global_Path+'/sales/ordermanage/orderreport/MarketPartDlrInstock/partDlrInstockInit.do';
var gxMenuId;

//============================经销商tip提示信息变量 end=================================

// ==========================ztree配置开始===================================
var rootMenuName = "功能列表";
var curMenu = null;
var menuTree = null;
var menuCssPath = global_Path+'/style/menuCss';
var openBorderColor = '#ade0f2';
var openBGColor = '#b2d3df';
var openFontColor = '#06445b';
var closeBoderColor = '#ffffff';
var closeBGColor = '#caeefb';//#d1bfa1
var closeFontColor = '#999999';//#dfe7ec
var menuBGColor = '#ffffff';
var menuFontColor = '#000000';
var menuImg = menuCssPath+'/selectIcon.gif';
var setting = {
	view : {
		showLine : false,
		selectedMulti : false,
		dblClickExpand : false,
		addDiyDom: menuDom,
		fontCss: menuDisplayCss
	},
	data : {
		simpleData : {
			enable : true,
			idKey: "id",
			pIdKey: "pid",
			rootPId: null
		}
	},
	callback : {
		beforeClick: menuTreeBeforeClick,
		onClick : menuTreeOnClick
	}
};
/**
 * 生成菜单树的json字符串
 * @param res
 * @returns {Array}
 */
function createMenuTree(res){
//	alert(res);
	var json=[];
	for( var i=0;i<res.length;i++ ){
		var row = res[i];
		var red = {};
		red.id = row.funcId;
		red.pid = row.parFuncId;
		red.name = row.funcName;

		if( row.funcCode && row.isFunc==1){
			red.url = global_Path + row.funcCode + ".do?funcId="+row.funcId;
		}
		red.target='displaytabNone';
		json.push(red);
		initTipMenuId(red);
	}
	return json;
	
}

// 获得菜单的层级,用于显示菜单的背景色
function getMenuLevel(res, red, lvl) {
	var tlvl = 0;
	// 根节点的话,层级为0
	if(red.name==rootMenuName){
		return 0;
	}
 	else if (lvl) {
		tlvl = lvl;
	}
	var pMenu = getParentMenu(res, red);
	// 没找到父节点,且不是根节点,是异常菜单.
	if (pMenu == null) {
		tlvl = -1;
	}
	// 父节点是根节点,层级为1;
	else if (pMenu.name == rootMenuName) {
		tlvl++;
	} else {
		tlvl = getMenuLevel(res, pMenu, tlvl+1);
	} 
	return tlvl;
}

/**
 * 获取指定节点的父节点
 * @param res
 * @param red
 * @returns {父节点}
 */
function getParentMenu(res, red) {
	var pMenu = {};
	// 如果是"功能列表"的话直接返回null
	if (red.name == rootMenuName) {
		pMenu = null;
	} else {
		for (var i = 0; i < res.length; i++) {
			var row = res[i];
			if (red.pid == row.funcId) {
				pMenu.id = row.funcId;
				pMenu.pid = row.parFuncId;
				pMenu.name = row.funcName;
				break;
			}
		}
	}
	return pMenu;
}

function menuTreeBeforeClick(treeId, node){
	if (node.isParent) {
		if (node.level === 0) {
			/*var pNode = curMenu;
			while (pNode && pNode.level !==0) {
				pNode = pNode.getParentNode();
			}
			if (pNode !== node) {
				var a = $jq("#" + pNode.id);
				a.removeClass("cur");
				menuTree.expandNode(pNode, false);
			}
			a = $jq("#" + node.id);
			a.addClass("cur");

			var isOpen = false;
			for (var i=0,l=node.children.length; i<l; i++) {
				if(node.children[i].open) {
					isOpen = true;
					break;
				}
			}
			if (isOpen) {
				menuTree.expandNode(node, true);
				curMenu = node;
			} else {
				menuTree.expandNode(node.children[0].isParent?node.children[0]:node, true);
				curMenu = node.children[0];
			}*/
		} else {
			menuTree.expandNode(node);
		}
		reExpandTree(menuTree,node);
		resetTreeCss();
	}
	return !node.isParent;
}

/**
 * 树节点点击事件
 * @param e
 * @param treeId
 * @param node
 */
function menuTreeOnClick(e, treeId, node) {
	if (!node.url) {
		return;
	}
	ligerTab = $jq("#framecenter").ligerGetTabManager();
	if(ligerTab&&ligerTab.getTabItemCount()>10){
		MyAlert('请不要开打过多的菜单!');
		return ;
	}
	clearClick();
	node.myClick = true;
	setNodeCss(node,'open');
	openTabLabel(node);
}

/**
 * 打开指定node的页面
 * @param node
 */
function openTabLabel(node){
	if(ligerTab.isTabItemExist(node.id)){
		ligerTab.removeTabItem(node.id);
	}else {
		
	}
	ligerTab.addTabItem({ tabid : node.id,text: node.name, url: node.url });
	ligerTab.selectTabItem(node.id);
}

function getNodeByID(nodeId){
	var node;
	if(menuTree&&nodeId){
		node = menuTree.getNodeByParam('id',nodeId);
	}
	return node;
}

/**
 * 树的自定义dom
 * @param treeId
 * @param treeNode
 */
function menuDom(treeId, treeNode){
	if(treeId&&treeNode&&treeNode.name==rootMenuName){
		return;
	}
	var spaceWidth = 5;
	var switchObj = $jq("#" + treeNode.tId + "_switch");
	var icoObj = $jq("#" + treeNode.tId + "_ico");
	switchObj.remove();
	icoObj.before(switchObj);
	if (treeNode.level > 0) {
		var spaceStr = "<span style='float:left;width:"
				+ (spaceWidth ) + "px'></span>";
		switchObj.before(spaceStr);
	}
	$jq(icoObj).parent('a').attr('align','left');
	$jq(icoObj).parent('a').find('span').css({'float':'left'});
}
function menuDisplayCss(treeId, treeNode) {
}

/**
 * 重设树的样式
 */
function resetTreeCss(){
	if(menuTree){
		var rootNode = menuTree.getNodes()[0];
		$jq('#'+rootNode.tId+'_ul').css({'background-color': closeBGColor});
		resetTreeCssByParent(rootNode);
	}
}

/**
 * 折叠除了node以外的菜单
 * @param zTre
 * @param node
 */
function reExpandTree(zTre,node){
	var rootNode = menuTree.getNodes()[0];
	var parents = new Array();
	var curNode = node;
	var i=0;
	while(curNode!=rootNode){
		parents[i++]=curNode;
		curNode = curNode.getParentNode();
		if(i>10){
			break;
		}
	}
	parents[parents.length] = rootNode;
	var nodes = zTre.transformToArray(zTre.getNodes());
	if(nodes&&nodes.length>0){
		for(var k=0;k<nodes.length;k++){
			var _node = nodes[k];
			var isExpand = false;
			for(var j=0;j<parents.length;j++){
				if(parents[j]==_node){
					isExpand = true;
					break;
				}
			}
			if(node.open&&!isExpand){
				zTre.expandNode(_node,false);
			}
		}
	}
}

/**
 * 重设指定节点下的所有子节点的样式,包括子节点的子节点
 * 
 * @param pNode
 */
function resetTreeCssByParent(pNode){
	if(pNode){
		var treeNodes = pNode.children;
		if(pNode.open){
			for (var i = 0; i < treeNodes.length; i++) {
				var curNode = treeNodes[i];
				setNodeCss(curNode,'close');
				if(curNode.open==true){
					setNodeCss(curNode,'open');
				}
				resetTreeCssByParent(curNode);
			}
		}
	}
}
function clearClick(){
	if(menuTree){
		var nodes = menuTree.transformToArray(menuTree.getNodes());
		for(var i=0;i<nodes.length;i++){
			var curNode = nodes[i];
			if(curNode.open){
				continue;
			}
			curNode.myClick=null;
			setNodeCss(curNode,'close');
		}
	}
}

function setNodeCss(curNode,flagStr){
	var tid = curNode.tId;
	var aid = tid+'_a';
	var aObj = $jq('#'+aid);
	var fontSpan = $jq('#'+tid+'_span');
	var switchSpan = $jq('#'+tid+'_switch');
	if(flagStr == 'open'){
		setNodeOpenCss(aObj,fontSpan,curNode,switchSpan);
	} else if(flagStr == 'close'){
		setNodeCloseCss(aObj,fontSpan,curNode,switchSpan);
	}
	$jq('#'+tid+'_ul').css({'background-color': closeBGColor});
}

function setNodeCloseCss(aObj,fontSpan,curNode,switchSpan){
	$jq(aObj).css({'background-color': closeBGColor});
	$jq(fontSpan).css({'color':closeFontColor});
	if(curNode&&!curNode.isParent){
		$jq(aObj).css({'border-color': closeBoderColor});
		if(switchSpan){
			$jq(switchSpan).css({'background-image':'none'});
		}
	}
}

function setNodeOpenCss(aObj,fontSpan,curNode,switchSpan){
	$jq(aObj).css({'background-color':openBGColor});
	$jq(fontSpan).css({'color':openFontColor});
	if(curNode&&!curNode.isParent){
		$jq(aObj).css({'border-color': openBorderColor});
		if(switchSpan){
			$jq(switchSpan).css({'background-image':'url('+menuImg+')'
				,'background-repeat':'no-repeat','vertical-align':'middle'});
		}
	}
}
//==========================ztree配置结束===================================

function showDiv() {
    setDivLocation(document.getElementById("billboard"), "a_notice") ;
    document.getElementById("billboard").style.display = "inline" ;
}

function hiddenDiv() {
    document.getElementById("billboard").style.display = "none" ;
}

//设置div控件绝对位置
function setDivLocation(objDiv, getValue) {
 objDiv.style.left = getAbsoluteLeft(getValue) ;
 objDiv.style.top = parseFloat(getAbsoluteTop(getValue)) + parseFloat(getElementWidth(getValue)) ;
}
// 获取控件左绝对位置
function getAbsoluteLeft(value) {
    var oObj = document.getElementById(value)
    var iLeft = oObj.offsetLeft
    while(oObj.offsetParent != null) {
        var oParent = oObj.offsetParent ;
        iLeft += oParent.offsetLeft ;
        oObj = oParent ;
    }
    return iLeft ;
}
// 获取控件上绝对位置
function getAbsoluteTop(value) {
    var oObj = document.getElementById(value) ;
    var iTop = oObj.offsetTop ;
    while(oObj.offsetParent != null)
    {
        var oParent = oObj.offsetParent ;
        iTop += oParent.offsetTop ;  // Add parent top position
        oObj = oParent ;
    }
    return iTop ;
}

// 获取控件长度
function getElementWidth(value) {
    var oObj = document.getElementById(value) ;
    return oObj.offsetHeight ;
}

//加载布局
//布局加载完成加载手风琴
//手风琴加载完成加载功能树
//功能树加载完成加载tabs
//为了迎合老功能,采用老的inIframe名称
//而且根据tabs的变化对应的iframe名称也跟着变化
//当前tba iframe id=inIframe
//其他 id= fun_id	
$jq(document).ready(function() {
	var bodyHeight = $jq(document).height();
	var headHeight = $jq('#headDiv').height();
	$jq('#layout1').height(bodyHeight-headHeight-5);
	var contentHeight = $jq('#layout1').height();
	$jq('#menuTreeDiv').height(contentHeight-15);
	$jq('#home').height(contentHeight-15);
	ligerTab = $jq("#framecenter").ligerTab({ height: contentHeight-5,onAfterSelectTabItem :function(targettabid){
    	var old = $jq("#inIframe");
    	if( old.attr("name") != targettabid ){
    		old.attr( "id",old.attr("name") );
    	}
    	$jq(".l-tab-content-item[tabid='"+targettabid +"'] iframe").attr("id","inIframe");
    	var curIframe = $jq("#inIframe");
    	var ifWindow = null;
    	var ifDoc = null;
    	var myFun = null;
    	// 是否需要调整宽度的方法
    	var isAdjustFun = null;
    	// 是否调整过宽度
    	var adjustedFlag = true;
    	// 调整宽度的方法
    	var adjustFun = null;
    	if('complete'==curIframe[0].readyState){
    		ifWindow = curIframe[0].contentWindow;
    		ifDoc = ifWindow.document;
    		myFun = ifWindow.setGridCss;
    		isAdjustFun = ifWindow.isNeedAdjustWidth;
    		adjustFun = ifWindow.setAdjustedWidth;
    	}
    	if((null!=isAdjustFun)&&(typeof isAdjustFun=='function')){
    		adjustedFlag = isAdjustFun();
    	}
    	if((null!=myFun)&&(typeof myFun=='function')){
    		var myTable = $jq(ifDoc).find('#myTable');
    		myFun();
    		if((null!=adjustFun)&&(typeof adjustFun=='function')){
    			adjustFun();
    		}
    	}
    },
    onBeforeSelectTabItem :function( targettabid ) {
    	var tab =  $jq(".l-tab-content-item[tabid='"+targettabid +"']");
    	var inIframe = $jq(".l-tab-content-item[tabid='inIframe']");
    	if(  0 == tab.length  ){
    		if( targettabid == inIframe.attr( "tabid_old" ) ){
    			inIframe.attr("tabid", targettabid );
    		}
    		
    	}else{
    		if( 0 != inIframe.length ){
    			inIframe.attr("tabid", inIframe.attr( "tabid_old" ) );
    		}
    	}

	}
    
    });
    initAccordion();
    var a_notice = $jq('#a_notice');
    if($(a_notice).html().length>5){
    	queryNotice();
    }
});

function initAccordion() {
	var url = global_Path + "/common/MenuShow/getUserSysFun.json";
	$jq.ajax({
		url:url,
		type:"post",
//		dataType:"json",
		success:function(res){
			res = eval("("+res+")");
			res = res.sysfun;
			var nres = createMenuTree(res);

			menuTree = $jq.fn.zTree.init($jq("#menuTree"), setting, nres);
			curMenu = menuTree.getNodes()[0];
			menuTree.selectNode(curMenu);
			menuTree.expandNode(curMenu);
			resetTreeCss();
		    initTipInfo();
		},
		error:function(){
			alert(1231);
		}
	});
	
}

/**
 * 查询消息提醒
 */
function queryNotice(){
	OpenHtmlWindow(noticeUrl,800,550);
}

/**
 * 选择查看notice的返回function
 * @param menuId
 * @returns {Boolean}
 */
function noticeReturn(menuId){
	return openNoticeMenu(menuId);
}

/**
 * 打开notice对应的菜单
 * @param menuId
 * @returns {Boolean}
 */
function openNoticeMenu(menuId){
	var node = getNodeByID(menuId);
	var flag = true;
	if(node){
		openTabLabel(node);
	} else {
		flag = false;
		alert("你没有权限进行此操作!");
	}
	return flag;
}

/**
 * notice处理成功后的方法
 */
function executedNotice(){
	var numNode = $jq('#noticeNum');
	if(numNode){
		var noticeNum = $jq(numNode).html();
		$jq(numNode).html(parseInt(noticeNum)-1);
	}
}

// ============================经销商tip提示信息=================================
function initTipInfo() {
	var dealerId = $jq('#dealerId').val();
	if (dealerId && dealerId.length > 0) {
		var url = global_Path + '/claim/application/DealerBalance/openclaimbalance.json';
		makeCall(url, afterCallThen, null);
	}
}

function afterCallThen(json){
	var msg=json.kpnum;
	var msgTemp=json.jjnum;
    var msggx = json.gxnum;
    if(kpMenuId){
		if(""!=msg && msg!=null && msg!="null"&&msg!=undefined){
			document.getElementById("divtemp").innerHTML="<a id='msg' herf='#' onclick='javascript: confirm();' ><span style='color: red; font-weight: bold;'>开票通知提醒：单号为:"+msg+"</span></a>";
		}
    }
    if(djMenuId){
		if(""!=msgTemp && msgTemp!=null && msgTemp!="null"&&msgTemp!=undefined){
			document.getElementById("divtemp1").innerHTML="<a id='hreftemp' style='color: bule;' href='#' onclick='javascript: hideDiv();' target='inIframe'>紧急调件通知提醒：单号为:"+msgTemp+"</a>";
		}
    }
    if(gxMenuId){
	    if(""!=msggx && msggx!=null && msggx!="null"&&msggx!=undefined){
	        document.getElementById("divtemp1").innerHTML="<a id='hreftemp' style='color: bule;' href='#' onclick='javascript: hideDiv();' target='inIframe'>广宣接收通知：单号为:"+msggx+"</a>";
	    }
    }
}

function confirm(){
	MyConfirm("是否确认要点击跳转开票页面并记录点击历史？",confirm1,null);
}

function confirm1(){
	var url=global_Path+'/claim/application/DealerNewKp/dealerKpQueryRecord.json';
	makeCall(url ,afterCallRecord,null) ;
}

function afterCallRecord(json){
	if(json.succ=='1'){
		var aDiv = $jq("#divtemp");
		var aNodeHtml = "<a id='hreftemp' style='color: bule;' onclick=\"openTipMenu('"+kpMenuId+"');return false;\" target='displaytabNone' href='#' >请点击并跳转到开票页面...</a>";
		$jq(aDiv).empty();
		$jq(aNodeHtml).appendTo($jq(aDiv));
	}else{
		alert("提示：添加记录失败！");
	}
}

function openTipMenu(menuId){
	var node = getNodeByID(menuId);
	if(node){
		hideDiv();
		openTabLabel(node);
	} else {
		alert("你没有权限进行此操作!");
	}
	return false;
}
function hideDiv(){
	document.getElementById("hreftemp").style.display='none';
}

function initTipMenuId(menuNode){
	if(menuNode){
		if(menuNode.url==kpUrl){
			kpMenuId = menuNode.id
		} else if(menuNode.url==djUrl){
			djMenuId = menuNode.id
		} else if(menuNode.url==gxUrl){
			gxMenuId = menuNode.id
		}
	}
}

//============================经销商tip提示信息=================================


function getNames(id,lt){
	for(var j=0;j<lt.length;j++){
		if(id==lt[j]){
			var funcName = lt[j].funcName;
			return funcName; 
		}
	}
}