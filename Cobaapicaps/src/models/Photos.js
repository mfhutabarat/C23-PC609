const { firebase } = require('../utils/FirebaseConfig');

const firestore = firebase.firestore();

class Photo {
    constructor(id, fileName, downloadUrl, userId) {
        this.id = id;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.userId = userId;
    }

    async save() {
        try {
            const photoData = {
                fileName: this.fileName,
                downloadUrl: this.downloadUrl,
                userId: this.userId,
                // Add any additional data or metadata related to the photo
                // For example: caption, timestamp, etc.
            };

            const docRef = await firestore.collection('photos').add(photoData);
            this.id = docRef.id;
            return this.id;
        } catch (error) {
            console.error('Error saving photo:', error);
            throw new Error('Failed to save photo');
        }
    }

    static async getById(photoId) {
        try {
            const docSnapshot = await firestore.collection('photos').doc(photoId).get();

            if (!docSnapshot.exists) {
                throw new Error('Photo not found');
            }

            const photoData = docSnapshot.data();
            return new Photo(photoId, photoData.fileName, photoData.downloadUrl, photoData.userId);
        } catch (error) {
            console.error('Error getting photo by ID:', error);
            throw new Error('Failed to get photo');
        }
    }
}

module.exports = Photo;
