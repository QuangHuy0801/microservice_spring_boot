import React, { useState, useEffect, useRef } from "react";
import { addToCart, cartOfUser, deleteCart } from "../services/CartService";
import { RiDeleteBin2Fill,RiArrowUpSFill,RiArrowDownSFill  } from "react-icons/ri";


const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);
  const user = useRef(JSON.parse(sessionStorage.getItem('user')));

  const fetchCart = async () => {
    try {
      const response = await cartOfUser(user.current.id);
      calculateTotal(response.data);
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

  const calculateTotal = (items) => {
    let totalPrice = 0;
    items.forEach(item => {
      totalPrice += item.product.price * item.count;
    });
    setTotal(totalPrice);
  };

  const handleDeleteCartItem = async (itemId) => {
    try {
      await deleteCart(itemId, user.current.id);
      console.log(`Deleting item with ID: ${itemId}`);
      fetchCart();
    } catch (error) {
      console.error('Error deleting item from cart:', error);
    }
  };

  const handleQuantityChange = async (newCount, product_id) => { 
    try {
      const response =await addToCart(user.current.id, product_id, newCount); 
      user.cart = response.data
      fetchCart();
    } catch (error) {
      console.error('Error adding item to cart:', error);
    }
  };

  return (
    <section className="shopping-cart spad">
      <div className="container">
        <div className="row">
          <div className="col-lg-8">
            <div className="shopping__cart__table">
              <table>
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {cartItems.map((item, index) => (
                    <tr key={index}>
                      <td className="product__cart__item">
                        <div style={{ maxWidth: '90px', maxHeight: '90px' }} className="product__cart__item__pic">
                          <img src={item.product.productImage[0].url_Image} alt="" />
                        </div>
                        <div className="product__cart__item__text">
                          <h6>{item.product.product_Name}</h6>
                          <h5>{item.product.price.toLocaleString('en-US')} VNĐ</h5>
                        </div>
                      </td>
                      <td className="quantity__item">
                        <div className="quantity">
                          <div className="pro-qty-2">
                            <button onClick={() => handleQuantityChange(+1,item.product.id)}>
                            <RiArrowUpSFill />
                            </button>
                            <input
                              name={`count${index}`}
                              type="number"
                              value={item.count}
                              onChange={(e) => handleQuantityChange( parseInt(e.target.value, 10),item.product.id)}
                            />
                             {item.count > 0 && (
                                <button onClick={() => handleQuantityChange(-1, item.product.id)}>
                                <RiArrowDownSFill />
                                </button>
                              )}
                          </div>
                        </div>
                      </td>
                      <td className="cart__price">{(item.product.price * item.count).toLocaleString('en-US')} VNĐ</td>
                      <td className="cart__close">
                        <button onClick={() => handleDeleteCartItem(item.id)}><RiDeleteBin2Fill /></button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div className="row">
              <div className="col-lg-6 col-md-6 col-sm-6">
                <div className="continue__btn">
                  <a href="/shop">Continue Shopping</a>
                </div>
              </div>
            </div>
          </div>
          {cartItems.length > 0 && (
  <div className="col-lg-4">
    <div className="cart__discount">
      <h6>Discount codes</h6>
      <input type="text" placeholder="Coupon code" />
      <button type="submit">Apply</button>
    </div>
    <div className="cart__total">
      <h6>Cart total</h6>
      <ul>
        <li>Customer <span>{user.current && user.current.user_Name}</span></li>
        <li>Total <span>{total.toLocaleString('en-US')} VNĐ</span></li>
      </ul>
      <a href="/checkout" className="primary-btn">Proceed to checkout</a>
    </div>
  </div>
)}
        </div>
      </div>
    </section>
  );
};

export default Cart;
