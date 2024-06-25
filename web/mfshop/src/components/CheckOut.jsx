import React, { useState,useEffect,useRef } from 'react';
import { cartOfUser } from '../services/CartService';
import { placeOrder } from '../services/OrderService';
import { useNavigate } from 'react-router-dom';
import { ToastContainer,toast } from 'react-toastify';


const Checkout = () => {
  const [fullName, setFullName] = useState('');
  const [address, setAddress] = useState('');
  const [phone, setPhone] = useState('');
  const [cartItems, setCartItems] = useState([]);
  const [paymentMethod, setPaymentMethod] = useState('');
  const user = useRef(JSON.parse(sessionStorage.getItem('user')));
  const navigate = useNavigate(); 

  const total = cartItems.reduce((acc, cartItem) => acc + cartItem.product.price * cartItem.count, 0);

  const fetchCart = async () => {
    try {
      const response = await cartOfUser(user.current.id);
      setCartItems(response.data);
      console.log(response.data);
    } catch (error) {
      console.error('Error fetching cart data:', error);
    }
  };
  useEffect(() => {
    if (user.current) {
      fetchCart();
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if(!fullName||!phone||!address){
        toast.error(`Phải điền đầy đủ các trường thông tin`, {
            position: "top-left",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
    }
    else if(!paymentMethod){
        toast.error(`Phải chọn phương thức thanh toán`, {
            position: "top-right",
            autoClose: 3000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
          });
    }
    else{
    try {
        const response = await placeOrder(user.current.id, fullName, phone, address, paymentMethod);
        navigate(`/invoice/${response.data.id}`);
    } catch (error) {
      toast.error(`Đã có lỗi server xảy ra`, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
        console.error('Error fetching cart data:', error);
    }}
};


  return (
    <div>
     <ToastContainer />
      <section className="breadcrumb-option">
        <div className="container">
          <div className="row">
            <div className="col-lg-12">
              <div className="breadcrumb__text">
                <h4>Check Out</h4>
                <div className="breadcrumb__links">
                  <a href="#">Home</a> <a href="#">Shop</a> <span>Check Out</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Checkout Section */}
      <section className="checkout spad">
        <div className="container">
          <div className="checkout__form">
            <form onSubmit={handleSubmit}>
              <div className="row">
                <div className="col-lg-8 col-md-6">
                  <h6 className="coupon__code">
                    <span className="icon_tag_alt"></span> Have a coupon? <a href="#">Click here</a> to enter your code
                  </h6>
                  <h6 className="checkout__title">Billing Details</h6>
                  <div className="checkout__input">
                    <p>Full Name<span>*</span></p>
                    <input
                      name="fullname"
                      placeholder="Nguyễn Văn A"
                      type="text"
                      value={fullName}
                      onChange={(e) => setFullName(e.target.value)}
                    />
                  </div>
                  <div className="checkout__input">
                    <p>Address<span>*</span></p>
                    <input
                      name="address"
                      type="text"
                      placeholder="Address"
                      className="checkout__input__add"
                      value={address}
                      onChange={(e) => setAddress(e.target.value)}
                    />
                  </div>
                  <div className="row">
                    <div className="col-lg-6">
                      <div className="checkout__input">
                        <p>Phone<span>*</span></p>
                        <input
                          name="phone"
                          placeholder="091234567"
                          type="text"
                          value={phone}
                          onChange={(e) => setPhone(e.target.value)}
                        />
                      </div>
                    </div>
                  </div>
                </div>
                <div className="col-lg-4 col-md-6">
                  <div className="checkout__order">
                    <h4 className="order__title">Your order</h4>
                    <div className="checkout__order__products">
                      Product <span>Total</span>
                    </div>
                    <ul className="checkout__total__products">
                      {cartItems.map((item, index) => (
                        <li key={item.id}>
                          {index + 1}. {item.product.product_Name} <span>{(item.product.price * item.count)}đ</span>
                        </li>
                      ))}
                    </ul>
                    <ul className="checkout__total__all">
                      <li>Total <span>{total}đ</span></li>
                    </ul>
                    <div className="checkout__input__radio">
                      <input
                        id="Pay on Delivery"
                        name="paymentMethod"
                        type="radio"
                        value="Pay on Delivery"
                        checked={paymentMethod === 'Pay on Delivery'}
                        onChange={() => setPaymentMethod('Pay on Delivery')}
                      />
                      <label htmlFor="Pay on Delivery">
                        Payment on delivery <span className="checkmark"></span>
                      </label>
                    </div>
                    <div className="checkout__input__radio">
                      <input
                        id="payWithMomo"
                        name="paymentMethod"
                        type="radio"
                        value="payWithMomo"
                        checked={paymentMethod === 'payWithMomo'}
                        onChange={() => setPaymentMethod('payWithMomo')}
                      />
                      <label htmlFor="payWithMomo">
                        Payment with Momo <span className="checkmark"></span>
                      </label>
                    </div>
                    <div className="checkout__input__radio">
                      <input
                        id="payWithVNPay"
                        name="paymentMethod"
                        type="radio"
                        value="payWithVNPay"
                        checked={paymentMethod === 'payWithVNPay'}
                        onChange={() => setPaymentMethod('payWithVNPay')}
                      />
                      <label htmlFor="payWithVNPay">
                        Payment with VNPay <span className="checkmark"></span>
                      </label>
                    </div>
                    <div className="checkout__input__radio">
                      <input
                        id="payWithPaypal"
                        name="paymentMethod"
                        type="radio"
                        value="payWithPaypal"
                        checked={paymentMethod === 'payWithPaypal'}
                        onChange={() => setPaymentMethod('payWithPaypal')}
                      />
                      <label htmlFor="payWithPaypal">
                        Payment with Paypal <span className="checkmark"></span>
                      </label>
                    </div>
                    <button type="submit" className="site-btn">
                      PLACE ORDER
                    </button>
                    </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Checkout;
