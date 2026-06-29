import React from 'react';

function AvMonSpend({ total, months }) {
    return (
        <div>
            {months && total > 0 && <h2>Average Monthly Expense: ${(total / months).toFixed(2)}</h2>}
        </div>
    );
}

export default AvMonSpend;