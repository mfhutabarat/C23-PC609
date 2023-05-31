const { firebase } = require('../utils/FirebaseConfig');

const firestore = firebase.firestore();

const deletePhoto = async (req, res) => {
    try {
        const { photoId } = req.params;

        // Get the photo document from Firestore
        const photoDoc = await firestore.collection('photos').doc(photoId).get();

        if (!photoDoc.exists) {
            return res.status(404).json({ error: 'Photo not found' });
        }

        const photoData = photoDoc.data();

        // Delete the file from Firebase Storage
        const bucket = firebase.storage().bucket();
        const file = bucket.file(photoData.fileName);

        await file.delete();

        // Delete the photo document from Firestore
        await firestore.collection('photos').doc(photoId).delete();

        return res.status(200).json({ message: 'Photo deleted successfully' });
    } catch (error) {
        console.error('Error deleting photo:', error);
        return res.status(500).json({ error: 'Failed to delete photo' });
    }
};

module.exports = {
    deletePhoto,
};
