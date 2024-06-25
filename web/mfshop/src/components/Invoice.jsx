import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { listOrder } from '../services/OrderService';

const Invoice = () => {
    const { index } = useParams();
    const [user] = useState(JSON.parse(sessionStorage.getItem('user')));
    const [order, setOrder] = useState(null);
    const [error, setError] = useState(null);
    function findOrderByID(orders, id) {
        for (let i = 0; i < orders.length; i++) {
            if (orders[i].id === id) {
                return orders[i]; // Trả về phần tử nếu tìm thấy id tương ứng
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }
    useEffect(() => {
      const fetchOrders = async () => {
        try {
          const response = await listOrder(user.id);
          const orders = response.data;
          const orderIndex = parseInt(index, 10);
          if (!isNaN(orderIndex) && orderIndex >= 0) {
            const foundOrder = findOrderByID(orders, orderIndex);
            if (foundOrder !== null) {
                setOrder(foundOrder); // Thiết lập order bằng phần tử tìm thấy
            } else {
                setError('Invalid order id');
            }
        } else {
            setError('Invalid order index');
        }
        } catch (error) {
          console.error(error);
          setError(error.message);
        }
      };
  
      fetchOrders();
    }, [user.id, index]);
    
    if (error) {
        return <div>Error: {error}</div>;
    }
    
    if (!order) {
        return <div>Loading...</div>;
    }

    return (
        <div style={{ marginBottom: '50px', marginTop: '80px' }}>
            {/* Print Button */}
        

            {/* Invoice */}
            <div className="container" id="invoice">
                <div style={{ display: 'flex' }}>
                <a href="/home" className="btn btn-dark" style={{ marginRight: '30px' }}>Continue Shopping</a>
                <button onClick={() => window.print()} className="btn btn-dark">Print this invoice</button>
            </div>
                {/* Header */}
                <div className="row">
                    <div className="col-md-6">
                        <div id="logo">
                            {/* Your logo here */}
                        </div>
                    </div>
                    <div className="col-md-6">
                        <p id="details">
                            <strong>Order: </strong>#{order.id}<br />
                            <strong>Issued: </strong>{new Date(order.booking_Date).toLocaleDateString()}<br />
                            Due 7 days from date of issue
                        </p>
                    </div>
                </div>

                {/* Client & Supplier */}
                <div className="row">
                    <div className="col-md-12">
                        <h2>Invoice</h2>
                    </div>
                    <div className="col-md-6">
                        <strong className="margin-bottom-5">Supplier</strong>
                        <p>
                            Male-Fashion <br />
                            97 Đ. Man Thiện, Hiệp Phú, Thủ Đức <br />
                            TP. Hồ Chí Minh<br />
                        </p>
                    </div>
                    <div className="col-md-6">
                        <strong className="margin-bottom-5">Customer</strong>
                        <p>
                            {order.fullname} <br />
                            SĐT: {order.phone} <br />
                            Email: {order.email} <br />
                        </p>
                    </div>
                </div>

                {/* Invoice */}
                <div className="row">
                    <div className="col-md-12">
                        <table className="table margin-top-20">
                            <thead>
                                <tr>
                                    <th>Description</th>
                                    <th>Quantity</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {order.order_Item.map((item, index) => (
                                    <tr key={index}>
                                        <td>{item.product.product_Name}</td>
                                        <td>{item.count}</td>
                                        <td>{(item.product.price * item.count).toLocaleString('en-US')} VNĐ</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                        <p >
                            <strong className="margin-bottom-5">Address: </strong>
                            {order.address}<br />
                            <strong className="margin-bottom-5">Payment method: </strong>
                            {order.payment_Method}<br />
                            <strong className="margin-bottom-5">Status:</strong>
                            <span className={
                                order.status === 'Completed' ? 'text-success' :
                                order.status === 'Pending' ? 'text-warning' :
                                order.status === 'Delivering' ? 'text-info' :
                                order.status === 'Cancel' ? 'text-danger' : ''
                            }>
                                {order.status}
                            </span>
                        </p>
                    </div>
                    <div className="col-md-4 col-md-offset-8">
                        <table className="table" id="totals">
                            <tbody>
                                <tr>
                                    <th>Total Due</th>
                                    <td>{order.total.toLocaleString('en-US')} VNĐ</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                {/* Footer */}
                <div className="row">
                    <div className="col-md-12">
                        <ul id="footer">
                            <li><span>www.example.com</span></li>
                            <li>voquanghuy08102000@gmail.com</li>
                            <li>0375250833</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Invoice;
