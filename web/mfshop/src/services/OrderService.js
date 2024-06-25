import axios from "axios";
const BASE_URL = "http://localhost:8080/api/order";

const ORDERAPI = "/order";
export const listOrder = (user_id) => {
    return axios.get(`${BASE_URL}${ORDERAPI}`, {
        params: { user_id: user_id }
    });
};

const PLACEORDER = "/placeorder";
export const placeOrder = (user_id,fullname,phoneNumber,address,paymentMethod) => {
    const params = new URLSearchParams();
    params.append('user_id', user_id);
    params.append('fullname', fullname);
    params.append('phoneNumber', phoneNumber);
    params.append('address', address);
    params.append('paymentMethod', paymentMethod);
    return axios.post(`${BASE_URL}${PLACEORDER}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};

const UPDATESTATUS = "/order/updateStatus/{orderId}";
export const updateStatus = (orderId, newStatus) => {
    const params = new URLSearchParams();
    params.append('newStatus', newStatus);
    return axios.post(`${BASE_URL}${UPDATESTATUS.replace('{orderId}', orderId)}`, params)
};