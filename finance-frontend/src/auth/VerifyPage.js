import React from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { API_URL } from '../config';
function VerifyPage() {
    const [searchParams] = useSearchParams();
    const email = searchParams.get('userEmail');
    const token = searchParams.get('token');
    const navigate = useNavigate();

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
    function verifyEmail() {
        if(token !== null){
            fetch(`${API_URL}/auth/verify`,{
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ token })
                })
            .then(response => {
                if(!response.ok){
                    throw new Error("Failed to verify email");
                }
                return response.json();
            })
            .then(() => {
                navigate('/dashboard');
            })
            .catch(error => {console.error(error);});
        }
    }
    return(
        <div>
            <h2>Email Verification {verifyEmail}</h2>
            <p>Please check your email for a verification link.</p>
            <p>If you did not receive the email, click the button below to resend the verification email.</p>
            <button onClick={ResendVerification}>Resend Verification Email</button>
        </div>
    )
}
export default VerifyPage;