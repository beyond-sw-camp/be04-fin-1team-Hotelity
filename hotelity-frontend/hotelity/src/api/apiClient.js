import axios from 'axios';

// nginx 사용
// const baseURL = 'http://ad99c7291a7f6418e86525a012a2df9d-1206189568.ap-northeast-2.elb.amazonaws.com';
const baseURL = 'http://ae582f63474d441b3a1a6e9e71f40bc8-1385203214.ap-northeast-2.elb.amazonaws.com:8888';
// const baseURL = 'http://localhost:8888';

const apiClient = axios.create({
    baseURL: baseURL,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
});

// 요청 인터셉터 설정
apiClient.interceptors.request.use(config => {

    // 요청이 보내지기 전에 작업 수행
    // 인증 토큰 추가
    const accessToken = localStorage.getItem('Authorization');
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 응답 인터셉터 설정
apiClient.interceptors.response.use(response => {
    return response;
}, error => {
    return Promise.reject(error);
});

export default apiClient;
