$('.group_submit').click(function() {
    $.post('groups/add', {url: $('.group_input').val()}, function(resp) {
        if (resp == "error") {
            window.location = "/groups?error";
            return;
        }
        if (resp == "warning") {
            window.location = "/groups?warning";
            return;
        }
        window.location = "/vote?group_id=" + resp;
    });
});

$('.cross').click(function() {
	var password = prompt("Please enter password");
	$.post('groups/delete', {password:password, groupId: this.getAttribute('id')}, function(resp) {
        window.location = "/groups";
        window.location.reload(true);
    });
})