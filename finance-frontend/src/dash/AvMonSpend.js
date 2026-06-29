import React, { useState, useEffect } from 'react';
import API_URL from '../config';
import { authHeaders } from '../config';

function AvMonSpend({ total, months }) {

        
    return (
        <div>
            {months && total > 0 && <h2>Average Monthly Expense: ${(total / months).toFixed(2)}</h2>}
        </div>
    );
}

export default AvMonSpend;