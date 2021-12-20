$( document ).ready(function() {
    console.log("ready!");

//    var banner = $("#banner-message");
    var button = $("#submit_button");
    var numItemsBox = $("#num_items");
    var resultsTable = $("#results table tbody");
    var resultsWrapper = $("#results");
    var noResultsError = $("#no_results_error");

    button.on("click", function(){
//        banner.addClass("alt");

		var payload = createRequest();
		console.log("payload", payload)        

        $.ajax({
          method : "POST",
          contentType: "application/json",
          data: payload,
          url: "shop",
          dataType: "json",
          success: onHttpResponse
          });
      });

    function createRequest() {
        var purchaseQuery = parseInt(numItemsBox.val());
        if (isNaN(purchaseQuery)) {
            purchaseQuery = 1;
        }

        var request = {
            purchase_query: purchaseQuery
        };

        return JSON.stringify(request);
    }

    function onHttpResponse(data, status) {
		console.log(data);
        if (status === "success" ) {
            addResults(data);
        } else {
            alert("Error connecting to the server " + status);
        }
    }

    function addResults(data) {

        resultsTable.empty();

        if (!data.results) {
            resultsWrapper.hide();
            noResultsError.show();
        } else {
            noResultsError.hide();
            resultsWrapper.show();
        }

        var items_available = data.results.available_items;
		var items_purchased = data.results.purchased_items;
        
        resultsTable.append("<tr><td>" + items_purchased + "</td><td>" + items_available + "</td></tr>");
    }
});
