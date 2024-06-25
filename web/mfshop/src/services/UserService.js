import axios from "axios";
const BASE_URL = "http://localhost:8080/api/user";
const SignIn = "/login";
export const signIn = (username, password) => {
    return axios.get(`${BASE_URL}${SignIn}`, {
        params: { id: username, password: password }
    });
};
const ForgotPassword = "/forgot";
export const forgotPassword = (username) => {
    const params = new URLSearchParams();
    params.append('id', username);

    return axios.post(`${BASE_URL}${ForgotPassword}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};


const SignUp = "/signup";
export const signUp = (username, fullname, email, password) => {
    const params = new URLSearchParams();
    params.append('username', username);
    params.append('fullname', fullname);
    params.append('email', email);
    params.append('password', password);

    return axios.post(`${BASE_URL}${SignUp}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};

const CHANGEPASSWORD = "/changepassword";
export const changePassword = (id, password) => {
    const params = new URLSearchParams();
    params.append('id', id);
    params.append('password', password);
    return axios.post(`${BASE_URL}${CHANGEPASSWORD}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};

const FORGOT_NEW_PASS_ENDPOINT = "/forgotnewpass";
export const forgotNewPass = (id, code, password) => {
    const params = new URLSearchParams();
    params.append('id', id);
    params.append('code', code);
    params.append('password', password);

    return axios.post(`${BASE_URL}${FORGOT_NEW_PASS_ENDPOINT}`, params, {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};

export const UPDATE_PROFILE = "/update";
export const updateProfile = (id, avatar, fullname, email, phoneNumber, address) => {
    const formData = new FormData();
    formData.append('id', id);
    if (avatar) {
        formData.append('avatar', avatar);
    }
    formData.append('fullname', fullname);
    formData.append('email', email);
    formData.append('phoneNumber', phoneNumber);
    formData.append('address', address);

    return axios.post(`${BASE_URL}${UPDATE_PROFILE}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};