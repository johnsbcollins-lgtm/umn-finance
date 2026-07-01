import React from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

import { API_URL } from '../config';
function HandleVerification() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();

    function verifyEmail() {
        fetch(`${API_URL}/auth/verify?token=${token}`,{
            method: 'POST',
            })
        .then(response => {
            console.log(token);
            if(!response.ok){
                throw new Error("Failed to verify email");
            }
        })
        .then(() => navigate('/dashboard'))
        .catch(error => {console.error(error);});
            
    }
    return(
        <div>
            <button onClick={verifyEmail}>Go to Dashboard</button>
        </div>
    )
}
export default HandleVerification;