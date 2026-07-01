import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { API_URL } from '../config';
function HandleVerification() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`${API_URL}/auth/verify?token=${token}`,{
            method: 'POST',
            })
        .then(response => {
            console.log("token:", token);
            if(!response.ok){
                throw new Error("Failed to verify email");
            }
            return response.json();
        })
        .then(() => {
            navigate('/dashboard');
        })
        .catch(error => {console.error(error);});
            
    }, [token, navigate]);
    return(
        <div>
            <h2>Loading...</h2>
        </div>
    )
}
export default HandleVerification;