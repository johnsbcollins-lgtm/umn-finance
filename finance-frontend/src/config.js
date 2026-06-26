export const API_URL = 'http://umn-finance-production.up.railway.app'
/*
const localUrl ='http://localhost:8081'
 apiUrl = 'umn-finance-production.up.railway.app'
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