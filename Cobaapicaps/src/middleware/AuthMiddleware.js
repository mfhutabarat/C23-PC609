const { firebase } = require('../utils/FirebaseConfig');

const firestore = firebase.firestore();

const authMiddleware = async (req, res, next) => {
    try {
        // Extract the user token from the request headers or query parameters
        const token = req.headers.authorization || req.query.token;

        if (!token) {
            return res.status(401).json({ error: 'Unauthorized' });
        }

        // Verify the token using Firebase Authentication
        const decodedToken = await firebase.auth().verifyIdToken(token);
        const userId = decodedToken.uid;

        // Check if the user exists in Firestore
        const userDoc = await firestore.collection('users').doc(userId).get();

        if (!userDoc.exists) {
            return res.status(404).json({ error: 'User not found' });
        }

        // Add the user object to the request for further processing
        req.user = {
            id: userId,
            email: decodedToken.email,
            // Add any additional user data or metadata you want to include
        };

        // Proceed to the next middleware or route handler
        next();
    } catch (error) {
        console.error('Error authenticating user:', error);
        return res.status(500).json({ error: 'Failed to authenticate user' });
    }
};

module.exports = authMiddleware;
