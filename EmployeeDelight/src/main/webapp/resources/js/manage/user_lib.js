var users=[];
var shouldSave;
var obj;


function confirmAction(msg){
	var flag;
	var r=confirm(msg);
	if (r==true)
	  {
		flag =true;
	  }
	else
	  {
		flag = false;
	  }
	return flag;
}

function clearValues() {
	$("#now_id").val("");
    $("#name").val("");
    $("#email").val("");
    $("#user").val("");
    $("#pass").val("");
    $("#points").val("");
    $("#group").val("");
    $("#isAdmin").prop('checked', false);
}

function populateValues(id){
	console.log("popval" + id);
	$.each( users, function( index, value )  {
		if (value.id  ==  parseInt(id)) {
			obj = value;
			$("#now_id").val(id);
			$("#name").val(obj.name);
			$("#email").val(obj.email);
			$("#user").val(obj.username);
			$("#pass").val(obj.password);
		    $("#points").val(obj.points);
		    $("#group").val(obj.userGroup.name);
		    if (obj.isAdmin) {
		    	$("#isAdmin").prop("checked", true);
			}
		}
	});
}


function initSideBarX(){
	// X display true
	$(".delete_button").click(function() {
		if (confirmAction("Do you want to delete this object?")) {
			var id = $(this).find('.objId').val();
			var data = {"int" : id}
			 $.ajax({
					headers: { 
			            'Accept': 'application/json',
			        },
					url : "users/remove?id="+id,
					type : "POST",
					contentType: "application/json",
					success : function(){
						// TODO
						$.ajax({
							url : 'users/get',
							type : 'post',
							success : function(output) {
								users = JSON.parse(output);
								console.log(users);
								$('#userList').empty()
								$.each(users, function(index,entry) {
									console.log("loop : ");
								    $('#userList').append('<li><div class="mouseover_delete"><a href="#">'+entry.name+'</a> <span class="delete_button"><a class="delete-icon">&#215;</a><input type="hidden" class="objId" value="'+entry.id+'"/></span>')
								  });
								initSideBarX();
							},
							error: callbackFunctionFail()
						});
					}
				});
		}
	});
	// display / hidde X
	$(".mouseover_delete").mouseenter(function() {
		$(this).find('.delete_button').show();
	}); 
	$(".mouseover_delete").mouseleave(function() {
		$(this).find('.delete_button').hide();
	}); 		
	
	
	$(".mouseover_delete").click(function() {
		var id = $(this).find('.objId').val();
		$("#add_user").prop('disabled', false);
		if (shouldSave) {
			if(confirmAction("Do you want to save changes to current item?")){
				addUser();
			}
		}
		else{
//			clearValues();
			populateValues(id);
		}
		$(".panel_right").first().show();
		$("#name").focus();
	});
	
	
	$(".delete_button").hide();
	$(".panel_right").hide();

}

function refreshUsers() {
	$.ajax({
		url : 'users/get',
		type : 'post',
		success : function(output) {
			users = JSON.parse(output);
			console.log(users);
			$('#userList').empty();
			$.each(users, function(index,entry) {
				console.log("loop : " + entry);
			    $('#userList').append('<li><div class="mouseover_delete"><a href="#">'+entry.name+'</a> <span class="delete_button"><a class="delete-icon">&#215;</a><input type="hidden" class="objId" value="'+entry.id+'"/></span>')
			  });
			initSideBarX();
		}
	});
//	event.preventDefault();
	
}

function updateUser(){
	var url = 'users/update';
	var id = obj.id;
	var name = $('#name').val();
	var user = $('#user').val();
	var email = $('#email').val();
	var password = $('#pass').val();
	var group = $('#group').val();
	var isAdmin = $("#isAdmin").attr("checked") ? true : false;
	var points = $('#points').val();
	var data = {
		"id" : id,
		"name" : name,
		"username" : user,
		"email" : email,
		"password" : password,
		"userGroup":{"name" : group},
		"isAdmin" : isAdmin,
		"points" : points
	}; 
	ajaxPost(url, data, 
			function(){
				$("#add_user").prop('disabled', false);
				callbackFunction(); 
				refreshUsers();
				clearValues();
				shouldSave = false;
	})
}

function addUser(){
		var url = 'users/add';
		
		var name = $('#name').val();
		var user = $('#user').val();
		var email = $('#email').val();
		var password = $('#pass').val();
		var group = $('#group').val();
		var isAdmin = $("#isAdmin").attr("checked") ? true : false;
		var points = $('#points').val();
		var data = {
			"name" : name,
			"username" : user,
			"email" : email,
			"password" : password,
			"userGroup":{"name" : group},
			"isAdmin" : isAdmin,
			"points" : points
		}; 
		ajaxPost(url, data, 
				function(){
					$("#add_user").prop('disabled', false);
					callbackFunction(); 
					refreshUsers();
					clearValues();
					shouldSave = false;
		})
}

function callbackFunction() {
	var respContent = "";

	respContent += "<span> Operation ended with success!</span>";

	$("#response").show().html(respContent);
	
}

function callbackFunctionFail() {
	var respContent = "";

	respContent += "<span> Operation failed!</span>";

	$("#response").show().html(respContent);
	
}

// post call
 function ajaxPost(url, data, callback){
	 $.ajax({
			headers: { 
	            'Accept': 'application/json',
	            'Content-Type': 'application/json' 
	        },
			url : url,
			data : JSON.stringify(data),
			type : "POST",
			contentType: "application/json",
			success :  callback,
			error: callbackFunctionFail()
		});
 }

$(document).ready(function(){
	$("#refresh").click(function() {
		refreshUsers();
	});

	$(".isChanged").change(function() {
		shouldSave = true;
	});


	$("#cancel").click(function() {
		if ( confirmAction("Do you want to cancel changes?")) {
			$("#add_user").prop('disabled', false);
			clearValues();
			$('.panel_right').first().hide();
		}
	});

	$("#add_user").click(function() {
		$("#now_id").val('-1');
		$('.panel_right').first().show();
		$("#add_user").prop('disabled', true);
	});

	$('#save').click(function(event) {
		if ($("#now_id").val() != '-1') {
			updateUser();
		}else{
			addUser();
		}
	});
	
	refreshUsers();
});
