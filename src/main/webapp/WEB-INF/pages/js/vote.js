
$('body').ready(function() { 
	$.post('get_pair.jsp', {}, function(resp) {
		var data=$.parseJSON(resp);
		//if(data['status']==1)
	})
})
	
$('.round').click(function(){ 
	var id = $(this).attr('id');
	$.post('add_vote.jsp', {id: id});
})

$('#text_color').click(function(){ 
//здесь код
})


#text_color - id твоей кнопки (картинки)
$.post('add_pass.php', {key: "key", label:"label"}, function(resp) {
	var data=$.parseJSON(resp);
	//if(data['status']==1)
})