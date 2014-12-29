$(function() {
	
	$("#search").click( function() {
		var key = $("#key").val();
		if (key === "") {
			$("#pronpt").text("検索条件を入れてください!");
			return;
		}

		$("#pronpt").text("俳句っぽいツイートを検索中...");

		var jsondata = {
		};
		$.post("/myHaiku/" + key,
			jsondata,
			function (response) {
				if (response.result === "OK") {
					// 俳句ツイートがあれば表示
					if (response.tweetList.length > 0) {
						$(response.tweetList).each(function() {
							$(".tweetList").prepend(
								'<div class="tweet">' + 
									'<div class="content">' + this.name + ' ' + this.screenName + '</div>' +
									'<div class="content">' + '<img src="' + this.profileImageUrl + '" />' + '</div>' +
									'<div class="content">' + this.text + '</div>' +
									'<div class="created_at">' + this.createdAt + '</div>' + 
								'</div>'
							);
						})
						$("#pronpt").text("俳句っぽいツイートを取得しました。");
						
					} else {
						$("#pronpt").text("最近の俳句っぽいツイートはありません。");
					}
					
				} else {
					$("#pronpt").text("failed!");
				}
			},
			"json"
		);
	})
})