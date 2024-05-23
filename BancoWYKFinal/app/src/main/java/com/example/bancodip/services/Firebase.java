package com.example.bancodip.services;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {	
	private static FirebaseDatabase db_instance = null;
	
	protected Firebase() throws IOException {
	    // make sure Firebase Class is initialized only once
		if (db_instance != null) {
			return;
		}

		FileInputStream serviceAccount =
				new FileInputStream("config/serviceAccount.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.getApplicationDefault())
				.setDatabaseUrl("https://testedatabasekaua-default-rtdb.firebaseio.com/")
				.build();


		FirebaseApp.initializeApp(options);


		db_instance = FirebaseDatabase.getInstance();
	}
	
	public static FirebaseDatabase getDatabaseInstance() {
		if (db_instance == null) {
		    try {
		        new Firebase();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	    
	    return db_instance;
	}
	
	public static void TerminateFirebaseApp() {
	    if (db_instance == null) {
	        return;
	    }
	    
	    FirebaseApp app = db_instance.getApp();
	    
		db_instance = null;
		
		app.delete();
	}
}
