import React from 'react';

function AvMonDeposit({ total, months }) {
    return (
        <div>
            {months > 0 && <h2>Avg. Monthly Deposit: ${(total / months).toFixed(2)}</h2>}
        </div>
    );
}


export default AvMonDeposit;