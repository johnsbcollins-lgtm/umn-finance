import React, { useState } from 'react'
import { useNavigate } from "react-router-dom";
import { API_URL } from '../config';

function HandleLogin(){
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    
    function Login(){
        fetch(`${API_URL}/auth/login`,{
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        })
        .then(response =>{
            if(!response.ok){
                throw new Error("Already Registered");
            }
            return response.json();
        })
        .then(data => {localStorage.setItem('token', data.token)})
        .then(() => navigate('/dashboard'))
        .catch(error => {console.error(error);});
    }

    return(
        <div>
            <h2>Login</h2>
            <input
                type="text"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input
                type="text"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={Login}>Login</button>
        </div>
    );
}

export default HandleLogin;