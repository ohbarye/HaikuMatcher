$(function() {
	
	$("#search").click( function() {
		if ($("#screenName").val() === "") {
			$("#pronpt").text("failed!");
			return;
		}
		
		var jsondata = {
		};
		$.post("/myHaiku/" + $("#screenName").val(),
			jsondata,
			function (response) {
				if (response.result === "OK") {
					$(response.tweetList).each(function() {
						$(".tweetList").prepend(
							'<div class="tweet">' + 
								'<div class="content">' + this.user + '</div>' +
								'<div class="content">' + this.text + '</div>' +
								'<div class="created_at">' + this.createdAt + '</div>' + 
							'</div>'
						);
					})
					$("#pronpt").text("completed!");
				} else {
					$("#pronpt").text("failed!");
				}
			},
			"json"
		);
	})
})