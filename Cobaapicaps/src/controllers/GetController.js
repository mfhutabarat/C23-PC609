const { firebase } = require('../utils/FirebaseConfig');

const firestore = firebase.firestore();

const getPhotos = async (req, res) => {
    try {
        // Get all the photo documents from Firestore
        const querySnapshot = await firestore.collection('photos').get();

        const photos = [];
        querySnapshot.forEach((doc) => {
            const photoData = doc.data();
            photos.push({
                id: doc.id,
                fileName: photoData.fileName,
                downloadUrl: photoData.downloadUrl,
                // Add any additional data or metadata related to the photo
                // For example: userId, caption, timestamp, etc.
            });
        });

        return res.status(200).json(photos);
    } catch (error) {
        console.error('Error getting photos:', error);
        return res.status(500).json({ error: 'Failed to get photos' });
    }
};

module.exports = {
    getPhotos,
};
