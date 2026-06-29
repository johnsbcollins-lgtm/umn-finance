import React, { useState, useEffect } from 'react';
import API_URL from '../config';
import { authHeaders } from '../config';

function AvMonSpend({ expenses, total}) {

    const [months, setMonths] = useState(null);


    useEffect(() => {
        fetch(`${API_URL}/expenses/months`,{
            headers: authHeaders()
        })
            .then(response => response.json())
            .then(data => {
                setMonths(data.amount);
            })
            .catch(error => console.error(error));
            console.log("Months: ", months);
    }, []);
        
    return (
        <div>
            {months && total > 0 && <h2>Average Monthly Expense: ${(total / months).toFixed(2)}</h2>}
        </div>
    );
}

export default AvMonSpend;