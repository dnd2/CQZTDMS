/**
*加载基础数据数结构
*/
var codeTree={
	Level:1,
	loadTree:function(url, formName, tree, clickfunc){
			var form = formName || "fm";
			url="/dms/crm/data/DataManage/initALLData.json?codeId=0";
			codeTree.ajax(url,form,codeTree.loadBack(tree, clickfunc));
	},
	
	loadBack:function(tree, clickfunc){
		return function(transport) {
			var dataList = transport.responseText.evalJSON().dataList;
			var treedata = codeTree.buildTree(dataList, -1);
			treedata=treedata.replaceAll("},]","}]");
			codeTree.newTafelTree(treedata, tree, clickfunc);
		}
	},
	
	newTafelTree : function(treedata, tree, clickfunc) {
		tree = new TafelTree('codeTree', eval(treedata), {
			'generate' : true,
			'imgBase' : '/dms/images/imgs/',
			'defaultImg' : 'page.gif',
			'defaultImgOpen' : 'folderopen.gif',
			'defaultImgClose' : 'folder.gif',
			'onClick' : clickfunc
		});
	},
	
	buildTree:function(list, topId) {
		var temp_tree_id = ""; //临时保存树形结构的ID，用于获取子节点的父节点
		var treedata = "";
		for(var i=0;i<list.length;i++){
			var treeid = list[i].TREE_ID; //ID
			var parentid = list[i].PARENT_ID; //父ID
			var treename = list[i].TREE_NAME; //名称
			var treelevel = parseInt(list[i].TREE_LEVEL, 10); //所属层级
			var nextcount = parseInt(list[i].NEXT_COUNT, 10); //子级数量
			var ischild = nextcount > 0; //是否有子级：true=有子级; false=没有子级
			
			
			if (parentid == topId){
				treedata += "{";
				treedata += "'id':'" + treeid + "',";
				treedata += "'txt':'" + treename + "'";
				if (nextcount > 0) {
					treedata += ",'items':" + codeTree.buildTree(list, treeid) + "";
				}
				treedata +=  "},";
			}
		}
		return "[" + treedata + "]";
		
		
		
	},
	
	ajax : function(url, formName, callback) {
		new Ajax.Request(url, {
        	method: 'post',
        	parameters: $(formName).serialize(true),
        	onFailure: function(){
            	alert('无法链接服务器！')
        	},
        	onSuccess: callback
    	});
	}
}