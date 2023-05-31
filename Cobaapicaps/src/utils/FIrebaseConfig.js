const firebase = require('firebase/app');
require('firebase/firestore');
require('firebase/auth');

const firebaseConfig = {
    apiKey: 'YOUR_API_KEY',
    authDomain: 'YOUR_AUTH_DOMAIN',
    projectId: 'YOUR_PROJECT_ID',
    storageBucket: 'YOUR_STORAGE_BUCKET',
    appId: 'YOUR_APP_ID',
};

// Inisialisasi Firebase
firebase.initializeApp(firebaseConfig);

// Mengatur objek Firestore
const firestore = firebase.firestore();

// Export objek Firebase dan Firestore
module.exports = {
    firebase,
    firestore,
};
