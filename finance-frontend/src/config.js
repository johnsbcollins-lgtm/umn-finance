export const API_URL = process.env.REACT_APP_API_URL
console.log("API_URL:", API_URL);
/*
const localUrl ='http://localhost:8081'
 apiUrl = 'https://umn-finance-production.up.railway.app'
*/
export function authHeaders() {
    return {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
        'Content-Type': 'application/json'
    };
}
export function authHeadersForFormData() {
  return {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  };
}
export default API_URL;