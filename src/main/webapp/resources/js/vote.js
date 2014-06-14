$('body').ready(function() { 
	get_pair();	
})

$('.round').click(function() {
	var id = '';
	if ($(this).attr('id') == 'left') {
		id = left;
	} else {
		id = right;
	}
	$.post('vote/vote', {user_id: id, group_id: getParameterByName('group_id')});
	get_pair();
})

var left = '';
var right = '';

function get_pair() {
	$.get('vote.json?group_id='+getParameterByName('group_id'), {}, function(resp) {
		var data=$.parseJSON(resp);
		$('#left_link').attr('href', 'vk.com/id'+data['left']['id']);
		$('#right_link').attr('href', 'vk.com/id'+data['right']['id']);
		$('#left').attr("src", data['left']['photoUrl']);
		$('#right').attr("src", data['right']['photoUrl']);
		left = data['left']['id'];
		right = data['right']['id'];
	})	
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

