import React, { useState} from 'react';
import API_URL from '../config';
import { authHeaders } from '../config';

function ChangeIncomeTotals({ income }){
    const [amount, setAmount] = useState('');
        const [vendor, setVendor] = useState('Other');
        const vendors = [...new Set(income.map(e => e.store))];

        const [type, setType] = useState('positive');
        function handleChange() {
                fetch(`${API_URL}/income/change-vendor-totals`, {
                    method: 'POST',
                    headers: authHeaders(),
                    body: JSON.stringify({ vendor, amount, type })
                })
                .then(response => response.text())
                .then(message => {alert(message);})
                .catch(error => console.error(error));
            }


    return (
        <div className="ChangeIncomeTotals">
            <h2>Change Income Totals</h2>
            <select value={vendor} onChange={(e) => setVendor(e.target.value)}> 
                <option value="" disabled>Select a Vendor</option>
                {vendors.map((vndr) => (
                    <option key={vndr} value={vndr}>
                        {vndr}
                    </option>
                ))}
            </select>
            <select value={type} onChange={(e) => setType(e.target.value)}>
                 <option value="" disabled>Select a type</option>
                <option value="positive">Positive Change</option>
                <option value="negative">Negative Change</option>
            </select>
            <input
                type="text"
                placeholder="Amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
            />
            <button onClick={handleChange}>Change Totals</button>
        </div>
    );
}

export default ChangeIncomeTotals;