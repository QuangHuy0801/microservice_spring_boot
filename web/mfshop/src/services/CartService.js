import axios from "axios";
const BASE_URL = "http://localhost:8080/api/user";
const CARTOFUSER = "/cartofuser";
export const cartOfUser = (user) => {
    return axios.get(`${BASE_URL}${CARTOFUSER}`, {
        params: { user: user }
    });
};

const ADDTOCART = "/addtocart";
export const addToCart = (user_id,product_id,count) => {
    const params = new URLSearchParams();
    params.append('user_id', user_id);
    params.append('product_id', product_id);
    params.append('count', count);
    return axios.post(`${BASE_URL}${ADDTOCART}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};
const DELETECART = "/deletecart";
export const deleteCart = (cart_id,user_id) => {
    const params = new URLSearchParams();
    params.append('cart_id', cart_id);
    params.append('user_id', user_id);
    return axios.post(`${BASE_URL}${DELETECART}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};