import React, { useState, useEffect } from 'react';

function AvMonSpend({ expenses, total}) {

    const [months, setMonths] = useState(null);


    useEffect(() => {
        fetch('http://localhost:8081/expenses/search?store=Months')
            .then(response => response.json())
            .then(data => {
                setMonths(data.amount);
            })
            .catch(error => console.error(error));
    }, []);
        
    return (
        <div>
            {months && total > 0 && <h2>Average Monthly Expense: ${(total / months).toFixed(2)}</h2>}
        </div>
    );
}

export default AvMonSpend;