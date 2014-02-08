var express = require('express'),
	http = require('http'),
	sockjs = require('sockjs'),
	connectedDevices = [];

var app = express();

app.set('port', process.env.PORT || 3000);
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);

// Create the socket
var options = {};

options.log = function(severity, message) {
	console.log("WS LOG: " + message);
};

var echo = sockjs.createServer(options);

echo.on('connection', function(conn) {
	console.log('New connection');
	connectedDevices.push(conn);

	conn.on('data', function(message) {
	});

	conn.on('close', function() {
	console.log('Connection closed');
	connectedDevices.splice(connectedDevices.indexOf(conn), 1);
    });
});

// Route to dispatch a message to all connected devices
app.post('/dispatchMessage', function(req, res) {
	var messageToDispatch = req.body["message"];
	var i = 0;
	if (messageToDispatch && messageToDispatch.length) {
		console.log('dispatching: ' + messageToDispatch);
		console.log('connected devices: ' + connectedDevices);
		for (; i < connectedDevices.length; i++) {
			console.log("dispatching to ");
			connectedDevices[i].write(messageToDispatch);
		}
	}

	res.type('application/json; charset=utf-8');
	res.send('{ "result": "message dispatched to ' + i + ' devices" }');
});

// Display a default message on '/'
app.get('/', function (req, res) {
	res.type('text/plain');
	res.send('Nothing to see here');
});

var server = http.createServer(app);

echo.installHandlers(server, { prefix:'/ws' });

server.listen(app.get('port'), function () {
	console.log('Express server listening on port ' + app.get('port'));
});
