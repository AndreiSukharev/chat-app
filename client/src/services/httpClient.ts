import axios from 'axios';


// eslint-disable-next-line no-restricted-globals
// export const BASE_URL = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
export const BASE_URL = "http://localhost:8080/"
const httpClient = axios.create({
    baseURL: BASE_URL,
    headers: {
        accept: 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    },
});


export default httpClient;
