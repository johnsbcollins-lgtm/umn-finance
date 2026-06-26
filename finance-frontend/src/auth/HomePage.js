import React from 'react'
import { useNavigate } from "react-router-dom";

function HomePage(){
    const navigate = useNavigate();
    return(
        <div>
            <h1>Welcome to GopherBudget!</h1>
            <button onClick={() => navigate('/login')}>Go to Login Page</button>
            <button onClick={() => navigate('/register')}>Register a new account</button>
        </div>
    );
}

export default HomePage;