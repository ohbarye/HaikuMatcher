$(function() {
	
	$("#search").click( function() {
		if ($("#screenName").val() === "") {
			$("#pronpt").text("ユーザー名を入れてください!");
			return;
		}
		
		var jsondata = {
		};
		$.post("/myHaiku/" + $("#screenName").val(),
			jsondata,
			function (response) {
				if (response.result === "OK") {
					// 俳句ツイートがあれば表示
					if (response.tweetList.length > 0) {
						$(response.tweetList).each(function() {
							$(".tweetList").prepend(
								'<div class="tweet">' + 
									'<div class="content">' + this.user + '</div>' +
									'<div class="content">' + this.text + '</div>' +
									'<div class="created_at">' + this.createdAt + '</div>' + 
								'</div>'
							);
						})
						$("#pronpt").text("俳句っぽいツイートを取得しました。");
						
					} else {
						$("#pronpt").text("俳句っぽいツイートはありません。");
					}
					
				} else {
					$("#pronpt").text("failed!");
				}
			},
			"json"
		);
	})
})