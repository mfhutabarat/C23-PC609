const express = require('express');
const routes = require('./routes');
const { firebase } = require('./utils/FirebaseConfig');

const app = express();
const port = process.env.PORT || 3000;

// Middleware untuk memparse body dari permintaan
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Mengatur rute
app.use('/', routes);

// Menjalankan server
app.listen(port, () => {
    console.log(`Server berjalan pada http://localhost:${port}`);
});
