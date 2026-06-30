import React from 'react';
import {  useSearchParams } from 'react-router-dom';
import { API_URL } from '../config';
function VerifyPage() {
    const [searchParams] = useSearchParams();
    const email = searchParams.get('userEmail');
   
    function ResendVerification(){
            fetch(`${API_URL}/auth/resend-verification`,{
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email })
            })
            .then(response => {
                if(!response.ok){
                    throw new Error("Failed to resend verification email");
                }
                return response.json();
            })
            .catch(error => {console.error(error);});
    }
    
    return(
        <div>
            <h2>Email Verification</h2>
            <p>Please check your email for a verification link.</p>
            <p>If you did not receive the email, click the button below to resend the verification email.</p>
            <button onClick={ResendVerification}>Resend Verification Email</button>
        </div>
    )
}
export default VerifyPage;