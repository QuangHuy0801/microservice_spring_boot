import axios from "axios";
const BASE_URL = "http://localhost:8080/api/product";
const newProductAPI = "/newproduct";
export const listnewProduct = () => axios.get(`${BASE_URL}${newProductAPI}`);
const blProductAPI = "/bestsellers";
export const listbsProduct = () => axios.get(`${BASE_URL}${blProductAPI}`);
const ProductAPI = "/product";
export const listProduct = () => axios.get(`${BASE_URL}${ProductAPI}`);
const SearchAPI = "/search";
export const listProductSearch = (searchContent) => {
    const url = `${BASE_URL}${SearchAPI}?searchContent=${searchContent}`;
    return axios.get(url);
};
const ProductById = "/product";
export const getProductById = (id) => axios.get(`${BASE_URL}${ProductById}/${id}`);
const ProductDetail = "/productDetail";
export const productdetail = (id) => axios.get(`${BASE_URL}${ProductDetail}/${id}`);