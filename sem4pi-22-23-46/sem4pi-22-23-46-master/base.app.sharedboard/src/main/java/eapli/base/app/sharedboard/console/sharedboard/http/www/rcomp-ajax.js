function refreshboard() {
        var request = new XMLHttpRequest();
        var vBoard = document.getElementById("board");

        request.onload = function() {
            vBoard.innerHTML = this.responseText;
            vBoard.style.color = "black";
            setTimeout(refreshboard, 2000);
        };

        request.ontimeout = function() {
            vBoard.innerHTML = "Server timeout, still trying ...";
            vBoard.style.color = "red";
            setTimeout(refreshboard, 100);
        };

        request.onerror = function() {
            vBoard.innerHTML = "No server reply, still trying ...";
            vBoard.style.color = "red";
            setTimeout(refreshboard, 5000);
        };

        request.open("GET", "/board", true);
        request.timeout = 5000;
        request.send();
	}


	

