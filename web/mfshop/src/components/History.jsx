import React, { useEffect, useState } from 'react';
import { listOrder, updateStatus } from '../services/OrderService';
import { ToastContainer, toast } from 'react-toastify';

const History = () => {
  const [user] = useState(JSON.parse(sessionStorage.getItem('user')));
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchOrders = async () => {
    try {
      if (user) {
        const response = await listOrder(user.id);
        setOrders(response.data);
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [user]); 

  const handleCancelOrder = async (orderId) => {
    const confirmCancel = window.confirm("Are you sure you want to cancel this order?");
    if (!confirmCancel) {
      return;
    }
    try {
      await updateStatus(orderId, "Canceled");
      await fetchOrders();
      toast.success(`Cancel order success`, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
    } catch (error) {
      toast.error(`Cancel order fail`, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      console.error('Error cancelling order:', error);
      setError(error.message);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  return (
    
    <div className="container-fluid mt-5 mb-5" style={{ padding: '0 200px 100px 200px' }}>
    <ToastContainer />
      <div className="row">
        <div className="col-md-12">
          <h2>My History</h2>
          <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
              <li className="breadcrumb-item"><a href="/home">Home</a></li>
              <li className="breadcrumb-item active" aria-current="page">My Profile</li>
            </ol>
          </nav>
        </div>
      </div>

      <div className="row">
        <div className="col-md-12">
          <div className="dashboard-list-box invoices with-icons mt-4 mx-auto">
            <h4>Invoices</h4>
            <ul className="list-group">
              {orders.map((order) => (
                <li key={order.id} className="list-group-item">
                  <i className="list-box-icon sl sl-icon-doc"></i>
                  {
                    order &&
                    order.order_Item &&
                    order.order_Item.length > 0 &&
                    order.order_Item[0].product && (
                      <strong>{order.order_Item[0].product.product_Name}</strong>
                      )
                  }
                  {order.order_Item.length > 1 && (
                    <span className="ml-3">
                      {' and ' + (order.order_Item.length - 1) + ' more ...'}
                    </span>
                  )}
                  <ul className="list-group">
                  <li>Order: #{order.id}</li>
                    <li className={order.payment_Method === 'Pay on Delivery' ? 'text-success' : 'text-primary'}>
                      {order.payment_Method}
                    </li>
                    <li>Date: {new Date(order.booking_Date).toLocaleDateString()}</li>
                    <li>
                        <span className="text-dark">Status:</span>&nbsp;
                        <span className={
                            order.status === 'Completed' ? 'text-success' :
                            order.status === 'Pending' ? 'text-warning' :
                                                order.status === 'Delivering' ? 'text-info' :
                            order.status === 'Canceled' ? 'text-danger' : ''
                        }>
                            {order.status}
                        </span>
                    </li>
                    <li></li>

                  </ul>
                  <div className="float-right">
                      {(order.status === 'Pending' || order.status === 'Delivering') && (
                      <button 
                        className="btn btn-danger ml-3" 
                        onClick={() => handleCancelOrder(order.id)}>
                        Cancel Order
                      </button>
                    )}
                    <a href={`/invoice/${order.id}`} className="btn btn-secondary ml-2">View Invoice</a>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default History;
