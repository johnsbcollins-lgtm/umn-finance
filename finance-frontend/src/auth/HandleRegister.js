import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import { API_URL } from '../config';

function HandleRegister(){
    const navigate = useNavigate()
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    function Register(){
        fetch(`${API_URL}/auth/register`,{
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, password})
        })
        .then(response => {
            console.log("Status:", response.status);
            console.log("Ok:", response.ok);
            if(!response.ok){
                throw new Error(`Failed with status: ${response.status}`);
        }
            return response.json();
        })
        .then(data => {localStorage.setItem('token', data.token)})
        .then(() => {
                console.log(localStorage.getItem('token'));
                navigate('/dashboard');
            })
        .catch(error => {console.error(error);});
    }
    return(
        <div>
            <h2>Register</h2>
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
            <button onClick={Register}> Register</button>       
        </div>
    )
}

export default HandleRegister;