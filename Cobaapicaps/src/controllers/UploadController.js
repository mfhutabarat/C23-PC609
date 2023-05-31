const { v4: uuidv4 } = require('uuid');
const { firebase } = require('../utils/FirebaseConfig');

const firestore = firebase.firestore();

const uploadPhoto = async (req, res) => {
    try {
        if (!req.file) {
            return res.status(400).json({ error: 'No file uploaded' });
        }

        const { originalname, buffer } = req.file;
        const fileName = `${uuidv4()}_${originalname}`;

        // Upload file to Firebase Storage
        const bucket = firebase.storage().bucket();
        const file = bucket.file(fileName);

        await file.save(buffer, {
            metadata: {
                contentType: 'image/jpeg', // Adjust the content type according to your file type
            },
        });

        const photoData = {
            fileName,
            downloadUrl: `https://storage.googleapis.com/${bucket.name}/${fileName}`,
            // Add any additional data or metadata related to the photo
            // For example: userId, caption, timestamp, etc.
        };

        // Save photo data to Firestore
        await firestore.collection('photos').add(photoData);

        return res.status(200).json({ message: 'Photo uploaded successfully' });
    } catch (error) {
        console.error('Error uploading photo:', error);
        return res.status(500).json({ error: 'Failed to upload photo' });
    }
};

module.exports = {
    uploadPhoto,
};
