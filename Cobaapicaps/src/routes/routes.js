const express = require('express');
const multer = require('multer');
const { firebase } = require('../utils/FirebaseConfig');
const AuthMiddleware = require('../middlewares/AuthMiddleware');
const UploadController = require('../controllers/UploadController');
const DeleteController = require('../controllers/DeleteController');
const GetController = require('../controllers/GetController');

const router = express.Router();
const upload = multer({ dest: 'uploads/' });

// Middleware untuk memeriksa otorisasi pengguna
router.use(AuthMiddleware);

// Upload foto
router.post('/upload', upload.single('photo'), UploadController.uploadPhoto);

// Hapus foto
router.delete('/photos/:photoId', DeleteController.deletePhoto);

// Dapatkan daftar foto
router.get('/photos', GetController.getPhotos);

module.exports = router;
