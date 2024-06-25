import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import './App.css';
import HeaderComponent from './components/HeaderComponent';
import Home from './components/Home';
import FooterComponent from './components/FooterComponent';
import Shop from './components/Shop';
import AboutUs from './components/AboutUs';
import Blog from './components/Blog';
import Contact from './components/Contact';
import SignIn from './components/SignIn';
import SignUp from './components/SignUp';
import MyProfile from './components/MyProfile';
import History from './components/History';
import Cart from './components/Cart';
import Checkout from './components/CheckOut';
import Invoice from './components/Invoice';
import ProductDetail from './components/ProductDetail';

function App() {
  return (
    <BrowserRouter>
      <AppContent />
    </BrowserRouter>
  );
}

function AppContent() {
  const location = useLocation();
  const isInvoicePage = location.pathname.startsWith('/invoice/');
  const isSignInPage = location.pathname === '/signin' || location.pathname === '/signup'|| isInvoicePage;

  return (
    <>
      {!isSignInPage && <HeaderComponent currentPage="home" />}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/shop" element={<Shop />} />
        <Route path="/shop/category/:categoryId" element={<Shop />} />
        <Route path="/about" element={<AboutUs />} />
        <Route path="/blog" element={<Blog />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/signin" element={<SignIn />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/myprofile" element={<MyProfile />} />
        <Route path="/myhistory" element={<History />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/checkout" element={<Checkout />} />
        <Route path="/invoice/:index" element={<Invoice />} />
        <Route path="/productdetail/:id" element={<ProductDetail />} />
      </Routes>
      {!isSignInPage && <FooterComponent />}
    </>
  );
}

export default App;
