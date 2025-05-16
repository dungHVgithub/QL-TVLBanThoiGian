import { initializeApp } from 'firebase/app';
import { getAuth, GoogleAuthProvider, FacebookAuthProvider } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyA_HDFbiw1XTCZL3SyA-DcZkkeIrxwcLSo",
  authDomain: "findjob-491cb.firebaseapp.com",
  projectId: "findjob-491cb",
  storageBucket: "findjob-491cb.firebasestorage.app",
  messagingSenderId: "816763929161",
  appId: "1:816763929161:web:f2885230724bef184eb598"
};


const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const googleProvider = new GoogleAuthProvider();
const facebookProvider = new FacebookAuthProvider();

export { auth, googleProvider, facebookProvider };
