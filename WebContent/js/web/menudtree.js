/*--------------------------------------------------|
| dTree 2.05 | www.destroydrop.com/javascript/tree/ |
|---------------------------------------------------|
| Copyright (c) 2002-2003 Geir Landr?              |
|                                                   |
| This script can be used freely as long as all     |
| copyright messages are intact.                    |
|                                                   |
| Updated: 17.04.2003                               |
| Updated: 2009.08.21 ChenLiang 新增三种状态的复选框 |
|--------------------------------------------------*/
// Node object
function Node(id, pid, name, url, title, target, icon, iconOpen, open) {
	this.id = id;
	this.pid = pid;
	this.name = name;
	this.url = url;
	this.title = title;
	this.target = target;
	this.icon = icon;
	this.iconOpen = iconOpen;
	this._io = open || false;
	this._is = false;
	this._ls = false;
	this._hc = false;
	this._ai = 0;
	this._p;	
};
// Tree object
function dTree(objName,htmlContainer,isSelect,isAll,myLink) {
	this.config = {
		target					: null,
		folderLinks			: true,
		useSelection		: true,
		useCookies			: true,
		useLines				: true,
		useIcons				: true,
		useStatusText		: false,
		closeSameLevel	: false,
		inOrder					: false,
		myfun           : "myfun"
	}
	this.icon = {
		root				: '../img/tree/base.gif',
		folder			: '../img/folder.gif',
		folderOpen	: '../img/folderopen.gif',
		node				: '../img/tree/leaf.gif',
		empty				: '../img/empty.gif',
		line				: '../img/line.gif',
		join				: '../img/join.gif',
		joinBottom	: '../img/joinbottom.gif',
		plus				: '../img/plus.gif',
		plusBottom	: '../img/plusbottom.gif',
		minus				: '../img/minus.gif',
		minusBottom	: '../img/minusbottom.gif',
		nlPlus			: '../img/nolines_plus.gif',
		nlMinus			: '../img/nolines_minus.gif',
		unchecked			: '../img/tree/unchecked.gif',
		square			: '../img/tree/square.gif',
		checked			: '../img/tree/checked.gif',
		loading			: '../img/tree/loading.gif'
	};
	this.obj = objName;
	this.aNodes = [];	
	this.aNodesData = [];	
	this.container=htmlContainer||'dtree';  //树所在的容器
	this.aIndent = [];
	this.root = new Node(-1);
	this.selectedNode = null;
	this.selectedFound = false;
	this.completed = false;
	this.isSelect = isSelect; //带复选框的树
	this.isAll = isAll; //载入全部节点
	this.myLink = myLink; //点击节点调用自己的方法
};

// Adds a new node to the node array
dTree.prototype.add = function(id, pid, name, url,title, target, icon, iconOpen, open) {
	this.aNodesData[this.aNodesData.length] = new Node(id, pid, name, url,title, target, icon, iconOpen, open);    
};
// Open/close all nodes
dTree.prototype.openAll = function() {
	this.oAll(true);
};
dTree.prototype.closeAll = function() {
	this.oAll(false);
};
// Outputs the tree to the page
dTree.prototype.toString = function() {
	var str = '<div class="dtree">\n';
	if (document.getElementById) {
		if (this.config.useCookies) this.selectedNode = this.getSelected();
		str += this.addNode(this.root);
	} else str += 'Browser not supported.';
	str += '</div>';
	if (!this.selectedFound) this.selectedNode = null;
	this.completed = true;
	return str;
};
// Creates the tree structure
dTree.prototype.addNode = function(pNode) {    
	var str = '';
	var n=0;
	if (this.config.inOrder) n = pNode._ai;    
	for (n; n<this.aNodes.length; n++) {        
		if (this.aNodes[n].pid == pNode.id) {            
			var cn = this.aNodes[n];            
			cn._p = pNode;
			cn._ai = n;
			this.setCS(cn);
			if (!cn.target && this.config.target) cn.target = this.config.target;
			if (cn._hc && !cn._io && this.config.useCookies) cn._io = this.isOpen(cn.id);
			if (!this.config.folderLinks && cn._hc) cn.url = null;
			if (this.config.useSelection && cn.id == this.selectedNode && !this.selectedFound) {
					cn._is = true;
					this.selectedNode = n;
					this.selectedFound = true;
			}            
			str += this.node(cn, n);
			if (cn._ls) break;
		}
	}
	return str;
};
// Creates the node icon, url and text
dTree.prototype.node = function(node, nodeId) {        
	var str = '<div class="dTreeNode">' + this.indent(node, nodeId);
	if (this.config.useIcons) {
		if (!node.icon) node.icon = (this.root.id == node.pid) ? this.icon.root : ((node._hc) ? this.icon.folder : this.icon.node);
		if (!node.iconOpen) node.iconOpen = (node._hc) ? this.icon.folderOpen : this.icon.node;
		if (this.root.id == node.pid) {
			node.icon = this.icon.root;
			node.iconOpen = this.icon.root;
		}
		str += '<img id="i' + this.obj + nodeId + '" src="' + ((node._io) ? node.iconOpen : node.icon) + '" alt="" />';
		this.isSelect == "true" ? str += '<img id="ckk' + this.obj + nodeId + '" style="cursor: pointer;" onclick="javascript: ' + this.obj + '.chkclick(' + nodeId +');" src="' + a.icon.unchecked + '" alt="" />' : "";
	}
	if (node.url) {
		if(this.myLink == "true") {
			str += '<a href="javascript: '+this.config.myfun+'(' + nodeId + ');" class="node">';
		} else {
			str += '<a id="s' + this.obj + nodeId + '" class="' + ((this.config.useSelection) ? ((node._is ? 'nodeSel' : 'node')) : 'node') + '" href="' + node.url + '"';
			if (node.title) str += ' title="' + node.title + '"';
			if (node.target) str += ' target="' + node.target + '"';
			if (this.config.useStatusText) str += ' onmouseover="window.status=\'' + node.name + '\';return true;" onmouseout="window.status=\'\';return true;" ';
			if (this.config.useSelection && ((node._hc && this.config.folderLinks) || !node._hc))    
				str += ' onclick="javascript: ' + this.obj + '.s(' + nodeId +');"';
			str += '>';
		}
	}
	else if ((!this.config.folderLinks || !node.url) && node._hc && node.pid != this.root.id)
		if(this.myLink == "true") {
			str += '<a href="javascript: '+this.config.myfun+'(' + nodeId + ');" class="node">';
		} else {
			str += '<a href="javascript: ' + this.obj + '.o(' + nodeId + ');" class="node">';
		}
	str += node.name;
	if (node.url || ((!this.config.folderLinks || !node.url) && node._hc)) str += '</a>';
	str += '</div>';
	if (node._hc) {
		str += '<div id="d' + this.obj + nodeId + '" class="clip" style="display:' + ((this.root.id == node.pid || node._io) ? 'block' : 'none') + ';">';
		str += this.addNode(node);
		str += '</div>';
	}
	this.aIndent.pop();
	return str;
};

dTree.prototype.chkclick = function(nodeId) {
	var funId = this.aNodes[nodeId].id; //该节点对应的功能ID
	var ckimg = $('ckk' + this.obj + nodeId).src; //当前选择中状态
	if(ckimg.contains("unchecked.gif") || ckimg.contains("square.gif")) { //若为全选或部分选择则改为选中状态
		$('ckk' + this.obj + nodeId).src = a.icon.checked;
		this.modifclick(nodeId,"checked");
	} else { //改为未选中状态
		$('ckk' + this.obj + nodeId).src = a.icon.unchecked;
		this.modifclick(nodeId,"unchecked");
	}
}

dTree.prototype.chkclickByFunId = function(funID) {
	var nodeId = this.getNodeIdByFunId(funID);
	$('ckk' + this.obj + nodeId).src = a.icon.checked;
	this.modifclick(nodeId,"checked");
}

dTree.prototype.getNodeIdByFunId = function(funID) {
	for(var n=0; n<this.aNodes.length; n++) {
		if(this.aNodes[n].id == funID) {
			return n;
		}
	}
}

dTree.prototype.modifclick = function(nodeId,st) {
	var pId = this.aNodes[nodeId].pid; //上级ID
	var funid = this.aNodes[nodeId].id;
	if("checked" == st) { //选中
		//处理所有子节点
		for(var n=0; n<this.aNodes.length; n++) {
			if(this.aNodes[n].id.indexOf(funid) == 0) { //是其子节点
				$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.checked;
			}
		}
		//处理所有父节点
		var tfunid = funid;
		while(tfunid.length>2) {
			tfunid = tfunid.substring(0,tfunid.length-2);
			var stt = true; // true: checked , false : square
			for(var n=0; n<this.aNodes.length; n++) {
				if(this.aNodes[n].id.indexOf(tfunid) == 0 && this.aNodes[n].pid == tfunid && $('ckk' + this.obj + this.aNodes[n]._ai).src.contains("unchecked.gif")) { //其父节点的其它子节点有未选中的
					stt = false; //设置其图标为部分选中
					break;
				}
			}
			for(var n=0; n<this.aNodes.length; n++) {
				if(this.aNodes[n].id.indexOf(tfunid) == 0) {
					if(!stt) {
						$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.square;
						break;
					}else {
						$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.checked;
						break;
					}
				}
			}
			stt = true;
		}
	} else { //不选中
		//处理所有子节点
		for(var n=0; n<this.aNodes.length; n++) {
			if(this.aNodes[n].id.indexOf(funid) == 0) { //是其子节点
				$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.unchecked;
			}
		}
		//处理所有父节点
		var tfunid = funid;
		while(tfunid.length>2) {
			tfunid = tfunid.substring(0,tfunid.length-2);
			var lp = tfunid.length+2;
			var stt = true; // true: checked , false : square
			for(var n=0; n<this.aNodes.length; n++) {
				if(this.aNodes[n].id.length == lp && this.aNodes[n].pid == tfunid && ($('ckk' + this.obj + this.aNodes[n]._ai).src.contains("/checked.gif") || $('ckk' + this.obj + this.aNodes[n]._ai).src.contains("square.gif"))) {
					stt = false; //设置其图标为部分选中
					break;
				}
			}
			for(var n=0; n<this.aNodes.length; n++) {
				if(this.aNodes[n].id.indexOf(tfunid) == 0) {
					if(!stt) {
						$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.square;
						break;
					}else {
						$('ckk' + this.obj + this.aNodes[n]._ai).src = a.icon.unchecked;
						break;
					}
				}
			}
			stt = true;
		}
	}
}

// Adds the empty and line icons
dTree.prototype.indent = function(node, nodeId) {
	var str = '';
	if (this.root.id != node.pid) {
		for (var n=0; n<this.aIndent.length; n++)
			str += '<img src="' + ( (this.aIndent[n] == 1 && this.config.useLines) ? this.icon.line : this.icon.empty ) + '" alt="" />';
		(node._ls) ? this.aIndent.push(0) : this.aIndent.push(1);
		if (node._hc) {
			str += '<a href="javascript: ' + this.obj + '.o(' + nodeId +');"><img id="j' + this.obj + nodeId + '" src="';
			if (!this.config.useLines) str += (node._io) ? this.icon.nlMinus : this.icon.nlPlus;
			else str += ( (node._io) ? ((node._ls && this.config.useLines) ? this.icon.minusBottom : this.icon.minus) : ((node._ls && this.config.useLines) ? this.icon.plusBottom : this.icon.plus ) );
			str += '" alt="" /></a>';
		} else str += '<img src="' + ( (this.config.useLines) ? ((node._ls) ? this.icon.joinBottom : this.icon.join ) : this.icon.empty) + '" alt="" />';
	}
	return str;
};

// Checks if a node has any children and if it is the last sibling
dTree.prototype.setCS = function(node) {
	var lastId;
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.id) node._hc = true;
		if (this.aNodes[n].pid == node.pid) lastId = this.aNodes[n].id;
	}
	if (lastId==node.id) node._ls = true;
};
// Returns the selected node
dTree.prototype.getSelected = function() {
	var sn = this.getCookie('cs' + this.obj);
	return (sn) ? sn : null;
};
// Highlights the selected node
dTree.prototype.s = function(id) {
    this.delegate(id);    
	if (!this.config.useSelection) return;
	var cn = this.aNodes[id];
	if (cn._hc && !this.config.folderLinks) return;
	if (this.selectedNode != id) {        
		if (this.selectedNode || this.selectedNode==0) {
			eOld = document.getElementById("s" + this.obj + this.selectedNode);
			eOld.className = "node";
		}
		eNew = document.getElementById("s" + this.obj + id);
		eNew.className = "nodeSel";
		this.selectedNode = id;
		if (this.config.useCookies) this.setCookie('cs' + this.obj, cn.id);
	}
};
// Toggle Open or close
dTree.prototype.o = function(id) {    
	var cn = this.aNodes[id];
	this.nodeStatus(!cn._io, id, cn._ls);
	cn._io = !cn._io;
	if (this.config.closeSameLevel) this.closeLevel(cn);
	if (this.config.useCookies) this.updateCookie();    
    this.delegate(id);
};
//delegate,handle custom click event
dTree.prototype.delegate=function(id){}

// Open or close all nodes
dTree.prototype.oAll = function(status) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n]._hc && this.aNodes[n].pid != this.root.id) {
			this.nodeStatus(status, n, this.aNodes[n]._ls)
			this.aNodes[n]._io = status;
		}
	}
	if (this.config.useCookies) this.updateCookie();
};
// Opens the tree to a specific node
dTree.prototype.openTo = function(nId, bSelect, bFirst) {
	if (!bFirst) {
		for (var n=0; n<this.aNodes.length; n++) {
			if (this.aNodes[n].id == nId) {
				nId=n;
				break;
			}
		}
	}
	var cn=this.aNodes[nId];
	if (cn.pid==this.root.id || !cn._p) return;
	cn._io = true;
	cn._is = bSelect;
	if (this.completed && cn._hc) this.nodeStatus(true, cn._ai, cn._ls);
	if (this.completed && bSelect) this.s(cn._ai);
	else if (bSelect) this._sn=cn._ai;
	this.openTo(cn._p._ai, false, true);
};

// Closes all nodes on the same level as certain node
dTree.prototype.closeLevel = function(node) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.pid && this.aNodes[n].id != node.id && this.aNodes[n]._hc) {
			this.nodeStatus(false, n, this.aNodes[n]._ls);
			this.aNodes[n]._io = false;
			this.closeAllChildren(this.aNodes[n]);
		}
	}
}

// Closes all children of a node
dTree.prototype.closeAllChildren = function(node) {
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n].pid == node.id && this.aNodes[n]._hc) {
			if (this.aNodes[n]._io) this.nodeStatus(false, n, this.aNodes[n]._ls);
			this.aNodes[n]._io = false;
			this.closeAllChildren(this.aNodes[n]);
		}
	}
}
// Change the status of a node(open or closed)
dTree.prototype.nodeStatus = function(status, id, bottom) {
	eDiv	= document.getElementById('d' + this.obj + id);
	eJoin	= document.getElementById('j' + this.obj + id);
	if (this.config.useIcons) {
		eIcon	= document.getElementById('i' + this.obj + id);
		eIcon.src = (status) ? this.aNodes[id].iconOpen : this.aNodes[id].icon;
	}
	eJoin.src = (this.config.useLines)?
	((status)?((bottom)?this.icon.minusBottom:this.icon.minus):((bottom)?this.icon.plusBottom:this.icon.plus)):
	((status)?this.icon.nlMinus:this.icon.nlPlus);
	eDiv.style.display = (status) ? 'block': 'none';
};
// [Cookie] Clears a cookie
dTree.prototype.clearCookie = function() {
	var now = new Date();
	var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
	this.setCookie('co'+this.obj, 'cookieValue', yesterday);
	this.setCookie('cs'+this.obj, 'cookieValue', yesterday);
};
// [Cookie] Sets value in a cookie
dTree.prototype.setCookie = function(cookieName, cookieValue, expires, path, domain, secure) {
	document.cookie =
		escape(cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
};
// [Cookie] Gets a value from a cookie
dTree.prototype.getCookie = function(cookieName) {
	var cookieValue = '';
	var posName = document.cookie.indexOf(escape(cookieName) + '=');
	if (posName != -1) {
	    var posValue = posName + (escape(cookieName) + '=').length;
		var endPos = document.cookie.indexOf(';', posValue);
		if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));
		else cookieValue = unescape(document.cookie.substring(posValue));
	}
	return (cookieValue);
};

// [Cookie] Returns ids of open nodes as a string
dTree.prototype.updateCookie = function() {
	var str = '';
	for (var n=0; n<this.aNodes.length; n++) {
		if (this.aNodes[n]._io && this.aNodes[n].pid != this.root.id) {
			if (str) str += '.';
			str += this.aNodes[n].id;
		}
	}
	this.setCookie('co' + this.obj, str);
};

// [Cookie] Checks if a node id is in a cookie
dTree.prototype.isOpen = function(id) {
	var aOpen = this.getCookie('co' + this.obj).split('.');
	for (var n=0; n<aOpen.length; n++)
		if (aOpen[n] == id) return true;
	return false;
};

// If Push and pop is not implemented by the browser
if (!Array.prototype.push) {
	Array.prototype.push = function array_push() {
		for(var i=0;i<arguments.length;i++)
			this[this.length]=arguments[i];
		return this.length;
	}
};
if (!Array.prototype.pop) {
	Array.prototype.pop = function array_pop() {
		lastElement = this[this.length-1];
		this.length = Math.max(this.length-1,0);
		return lastElement;
	}
};

//show the tree
dTree.prototype.draw = function(){	
	// renew the two array to save original data.
	this.aNodes=new Array();
	this.aIndent=new Array();
	// dump original data to aNode array.
	for(var i=0 ; i<this.aNodesData.length ; i++){		
		var oneNode=this.aNodesData[i];
		this.aNodes[i]=new Node(oneNode.id,oneNode.pid,oneNode.name,oneNode.url,oneNode.title,oneNode.target,oneNode.icon,oneNode.iconOpen,oneNode.open);	}	
	this.rewriteHTML();
}

// outputs the tree to the page , callled by show()
dTree.prototype.rewriteHTML = function() {
	var str = '';	
	var container;
	container=document.getElementById(this.container);	
	if(!container){		
		alert('dTree can\'t find your specified container to show your tree.\n\n Please check your code!');
		return;
	}
	if (this.config.useCookies) this.selectedNode = this.getSelected();
	str += this.addNode(this.root);
	if (!this.selectedFound) this.selectedNode = null;
	this.completed = true;	
	container.innerHTML=str;
};

// Checks if a node has children
dTree.prototype.hasChildren=function(id){
    for(var i=0 ; i<this.aNodesData.length ; i++){		
		var oneNode=this.aNodesData[i];
		if(oneNode.pid==id)
		    return true;
	}
	return false;
}

//define a remove method for Array
Array.prototype.remove=function(dx) {
  if(isNaN(dx)||dx>this.length){return false;}
　　for(var i=0,n=0;i<this.length;i++)
　　{
　　　　if(this[i]!=this[dx])
　　　　{
　　　　　　this[n++]=this[i]
　　　　}
　　}
　　this.length-=1
}

//remove a node
dTree.prototype.remove=function(id){
    if(!this.hasChildren(id)){
        for(var i=0 ; i<this.aNodesData.length ; i++){
            if(this.aNodesData[i].id==id){
                this.aNodesData.remove(i);
            }
        }
    }
}