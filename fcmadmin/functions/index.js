'use strict'

var topic = "orders";

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const MAX_LOG_COUNT = 1;

admin.initializeApp(functions.config().firebase);

exports.updateOrder = functions.database.ref('/ORDER/CurrentOrder').onWrite(event =>{

 	const CurrentOrder = event.params.CurrentOrder;
	const notification = event.params.notification;
		
		const token = admin.database().ref(`TOKEN/deviceToken/deviceToken`).once('value');
		return token.then(result =>{
			
			const token_id = result.val();
			
				const payload = {
				notification: {
				title: "TODAY'S ORDER IS UPDATED!",
				body: "Click here to save your preference",
				icon: "R.drawable.finaluser",
				sound: "default",
				color: "#12397a",
				priority: "high"
			}
			
		};
			return admin.messaging().sendToTopic(topic,payload).then(response => {
			
			});
			
		});
		
	/*
	exports.truncate = functions.database.ref('/ADMIN/{uid}').onWrite(event => {
		const parentRef = event.data.ref.parent;
		return parentRef.once('value').then(snapshot => {
		if (snapshot.numChildren() >= MAX_LOG_COUNT) {
		let childCount = 0;
		const updates = {};
      snapshot.forEach(function(child) {
        if (++childCount <= snapshot.numChildren() - MAX_LOG_COUNT) {
          updates[child.key] = null;
        }
      });
      // Update the parent. This effectively removes the extra children.
      return parentRef.update(updates);
    }
  });
});
*/
});

