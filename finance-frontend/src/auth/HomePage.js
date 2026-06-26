import React from 'react'
import { useNavigate } from "react-router-dom";

function HomePage(){
    const navigate = useNavigate();
    return(
        <div>
            <h1>Welcome to GopherBudget!</h1>
            <button onClick={() => navigate('/auth/login')}>Go to Login Page</button>
            <button onClick={() => navigate('/auth/register')}>Register a new account</button>
        </div>
    );
}

export default HomePage;