$(function() {
	
	$("#search").click( function() {
		var key = $("#key").val();
		if (key === "") {
			$("#pronpt").text("検索条件を入れてください!");
			return;
		}

		$("#pronpt").text("俳句っぽいツイートを検索中...");
		showLoadingImage();

		$.ajax({
			type: "POST",
			url : "/myHaiku/",
			data : {'input' : key},
			success : function(response) {
				setHaikus(response);
			},
			complete : function() {
				hideLoadingImage();
			}
		})
/*		$.post("/myHaiku/" + key,
			jsondata,
			function (response) {
				setHaikus(response);
			},
			"json"
		);
*/
	})
	
	function setHaikus(response) {
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
			$("#pronpt").text("failed! 検索時にエラーが発生しました。");
		}
	}
	// クルクル画像表示
	function showLoadingImage() {
	    // ダウンロードしてきたクルクル画像を指定しましょう！
	    $("#searchbtn").hide();
	    $("#loading").show();
	}
	// クルクル画像消去
	function hideLoadingImage() {
	    // ゆっくり消すためにfadeout(500)を指定してますが、hide()とかでも良いです。
	    $("#loading").hide();
	    $("#searchbtn").show();
	}
})