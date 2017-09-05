/*
 * CreateDate : 2009-10-09
 * CreateBy   : ChenLiang
 * Comment    : 功能树
 */

 var mybody;
 var subStr = "funlist";
 var addNodeId;
 var treew;
 var treeh;
 var a;
 
 var mydeptTree = new Element('div',{
 	'class' : 'dtree',
 	'id' : 'dtree',
 	'styles' : {
 		'z-index' : '3000',
 		'position' : 'absolute',
 		'border' : '1px solid #5E7692',
 		'width' : '230px',
 		'height' : '350px',
 		'padding' : '1px',
 		'orphans' : '0'
 	}
 });
 
 var tree_script;
 
 window.addEvent('domready', function(){
 	tree_script = new Element('script',{
 		'type' : 'text/javascript'
 	});
 	mybody = window.document.body;
 	tree_script.injectInside(mydeptTree);
 	mydeptTree.injectInside(mybody);
 	tree_script.setText("a = new dTree('a','dtree','false','false','true');");
 	
 	$("dtree").setOpacity("0");
	$('dtree').setStyle("top",-1000);
	treew = $('dtree').getStyle('width');
	treeh = $('dtree').getStyle('height');
	myEffects = new Fx.Styles('dtree', {duration: 277,transition: Fx.Transitions.linear});
	createFUNTree();
 });
 
 function showFUNC() {
 	
	$('dtree').setStyle("top",$('FUNNAME').getCoordinates().top);
	$('dtree').setStyle("left",$('FUNNAME').getCoordinates().left);
	
	myEffects.start({
	    'opacity': [0,1],
	    'width' : [0,treew],
	    'height' : [0,treeh]
	});
 }

 function createFUNTree() {
	a.config.closeSameLevel=false;
	a.config.myfun="pos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;
	    var nodeID = a.aNodes[id].id;
	}
	sendAjax(func_tree_url,createTree,'fm');
	a.closeAll();
 }

 function pos(id) {
	var funid = a.aNodes[id].id;
	var furl = a.aNodes[id].url;
	var fname = a.aNodes[id].name;

	$('SECTION').value = furl;
	var type;
	if(funid.length == 2) {
		type = "all";
		$('SECTION').value = alls.substr(0,alls.length-1);
	} else if(funid.length == 4) {
		type = "frist";
	} else if(funid.length == 6){
		type = "second";
	} else if(funid.length == 8){
		type = "fun";
	}else if(funid.length == 10){
		type = "action";
	}
	$('TYPE').value = type;
	$('FUNNAME').value = fname;

	closeTreeDiv('dtree');
 }
 
 var alls;
 
 function createTree(reobj) {
 	alls = "";
 	mydeptTree.empty();
 	
 	tree_script = new Element('script',{
 		'type' : 'text/javascript'
 	});
 	mybody = window.document.body;
 	tree_script.injectInside(mydeptTree);
 	tree_script.setText("a = new dTree('a','dtree','false','false','true');");
 	
 	$("dtree").setOpacity("0");
	$('dtree').setStyle("top",-1000);
	treew = $('dtree').getStyle('width');
	treeh = $('dtree').getStyle('height');
	
 	a.config.closeSameLevel=false;
	a.config.myfun="pos";
	a.config.folderLinks=true;
	a.delegate=function (id)
	{
		addNodeId = a.aNodes[id].id;
	    var nodeID = a.aNodes[id].id;
	}
	a.closeAll();
 	
	var funlistobj = reobj[subStr];
	var funcCode,parFuncId,funcId,funcName;
	for(var i=0; i<funlistobj.length; i++) {
		parFuncId = funlistobj[i].parFuncId;
		funcId = funlistobj[i].funcId;
		funcName = funlistobj[i].funcName;
		funcCode = funlistobj[i].funcCode;
		if(funcId.length == 4) {
			alls += funcCode + "^";
		}
		if(parFuncId == 0) {
			a.add(funcId,"-1",funcName,funcCode);
		} else {
			a.add(funcId,parFuncId,funcName,funcCode);
		}
	}
	a.draw();
 }
 