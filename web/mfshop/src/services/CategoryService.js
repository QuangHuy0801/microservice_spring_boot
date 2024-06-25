import axios from "axios";
const BASE_URL = "http://localhost:8080/api/product";
const CategoryAPI = "/category";
export const listCategory = () => axios.get(`${BASE_URL}${CategoryAPI}`);