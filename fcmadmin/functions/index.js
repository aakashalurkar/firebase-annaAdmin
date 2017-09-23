'use strict'

var topic = "orders";

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.updateOrder = functions.database.ref('/ORDER/CurrentOrder').onWrite(event =>{

 	const CurrentOrder = event.params.CurrentOrder;
	const notification = event.params.notification;
		
		const token = admin.database().ref(`TOKEN/deviceToken/deviceToken`).once('value');
		return token.then(result =>{
			
			const token_id = result.val();
			
				const payload = {
				notification: {
				title: "Today's order is updated",
				body: "Click here to save your preference",
				icon: "ic_launcher_round",
				sound: "default",
				color: "#2c438d",
				priority: "high"
			}
			
		};
			return admin.messaging().sendToTopic(topic,payload).then(response => {
			
			});
			
		});
});

