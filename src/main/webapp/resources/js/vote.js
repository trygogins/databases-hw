$('body').ready(function() {
    $('#rating_link').attr('href', '/rating/' + getParameterByName('group_id'));
	get_pair();	
})

$('#next_pair').click(function() {
    get_pair();
})

$('.round').click(function() {
	var selectedUser = '';
	if ($(this).attr('id') == 'left') {
		selectedUser = left;
	} else {
		selectedUser = right;
	}
    var ok = true;
    $.ajax({
        beforeSend: function(xhrObj) {
            xhrObj.setRequestHeader("Content-Type", "application/json");
            xhrObj.setRequestHeader("Accept", "application/json");
        },
        type: "POST",
        url: 'vote/vote?token=' + token,
        data: JSON.stringify(selectedUser),
        dataType: "json",
        success: function(resp) {
            if (resp == "no") {
                window.location = "/vote?groupId=" + getParameterByName('group_id');
                ok = false;
            }
        }
    })
    if (ok) {
        get_pair();
    }
})

var left;
var right;
var token;

function get_pair() {
	$.get('vote.json?group_id='+getParameterByName('group_id'), {}, function(data) {
		var pair = data['pair'];
        $('#left_link').attr('href', 'http://vk.com/id'+pair['left']['id']);
		$('#right_link').attr('href', 'http://vk.com/id'+pair['right']['id']);
        $('#left').css("background-image", 'url(' + pair['left']['photoUrl'] + ")");
        $('#right').css("background-image", 'url(' + pair['right']['photoUrl'] + ")");
		left = pair['left'];
		right = pair['right'];
        token = data['token']
	})	
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}



