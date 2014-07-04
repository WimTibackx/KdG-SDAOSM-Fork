angular.module("cpa.ctrl").controller('InboxCtrl', ['$scope', '$http', '$location', 'cpa.svc.api.v1', function ($scope, $http, $location, cpa_api) {
	var currentMessage;
	var selectedMessage = undefined;
	var finished = false;

	var clickedMessage = false;
	var clickedOverlay = false;

	var tabClicked = "recieve";
	
	$scope.page = new PageManagement([]);
	
    var defaultActive = function () {
        $('#tabInbox').removeClass('activeTab');
        $('#tabSend').removeClass('activeTab');
    }
    
    var formPopupEnabled = false;
    
    var currentUsername = undefined;


    $scope.type = "Inbox";
    $scope.hideSend = true;
    $scope.hideMessage = true;
    $scope.hideRecieve = false;
    $scope.recieverName = "";
    $scope.subject = "";
    $scope.detailMsg = undefined;

    $scope.replyMessage = function (message) {

        clickedOverlay = true;
        
        //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
        $('#overlay').show();

        $scope.mBody = "";
        $scope.recieverName = message.senderUsername;
        $scope.sender = message.receiverUsername;
        $scope.subject = "RE: " + message.messageSubject;
    }

    $scope.openDetails = function (message, type) {
    	if (clickedOverlay) {
    		clickedOverlay = false;
    		return;
    	}

        $scope.hideMessage = false;

        clickedMessage = true;

        selectedMessage = message.id;

        currentMessage = message;
        if (!message.read && type == 'recieve') {
        	cpa_api.notifyReadMsg(messageId, function(){}, function(){}, function(){});
        }
        $scope.subjectO = message.messageSubject;
        $scope.messageBody = message.messageBody;
        $scope.sender = message.senderUsername;
        $scope.receiver = message.receiverUsername;
        $scope.hideMessage = false;
        $scope.detailMsg = message;
        
        loadMessages(type);
    }

    $scope.tabInboxClick = function () {
        tabClicked = "recieve";
        defaultActive();
        $('#tabInbox').addClass('activeTab');

        $scope.type = "Inbox";
        $scope.hideSend = true;
        $scope.hideRecieve = false;
        $scope.hideMessage = true;

        loadMessages('recieve');
    }

    $scope.tabSendClick = function () {
    	tabClicked = "send";
        defaultActive();
        $('#tabSend').addClass('activeTab');

        $scope.type = "Verzonden berichten";
        $scope.hideSend = false;
        $scope.hideRecieve = true;
        $scope.hideMessage = true;
        
        loadMessages('send');
    }

    function loadMessages(type) {
    	var cbSuccess = function(data) {
    		$scope.page = new PageManagement(data[(type == "receive" ? "receivedMessages" : "sentMessages")]);
    	};
    	cpa_api.getMsgs(function() {}, cbSuccess, function() {});
    }

    $scope.clickOverlay = function () {

        if (formPopupEnabled) {
            formPopupEnabled = false;
        } else {
            //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
            $('#overlay').hide();
        }

        detailsEnabled = true;
    }

    $scope.clickFormpopup = function () {
        formPopupEnabled = true;
    }

    $scope.sendMessageToUser = function () {
    	var sender = $scope.sender,
    		receiver = $scope.receiverName,
    		msg = $scope.mBody,
    		subject = $scope.subject,
    		
    		cbAll = function() {},
    		cbSuccess = function() {
    			$('#overlay').hide();
    			$scope.tabSendClick();
    		},
    		cbUnknown = function() {};
    	cpa_api.sendMsg(sender, receiver, msg, subject, cbAll, cbSuccess, cbUnknown);
    }

    $scope.sendNewMessage = function () {

        //ng-Show, ng-Hide won't work, it keeps the ng-hide propery active on the overlay
        $('#overlay').show();

        $scope.mBody = "";
        $scope.recieverName = "";
        $scope.sender = currentUsername;
        $scope.subject = "";
    }
    
    function getUsersUsername() {
    	var cbSuccess = function (data) {
    		currentUsername = data.username;
    	};
    	cpa_api.getMyProfile(function() {}, cbSuccess, function() {});
    }
    
    getUsersUsername();
    $scope.tabInboxClick();
}]);