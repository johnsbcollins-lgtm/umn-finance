import React, { useState} from 'react';
import API_URL from '../config';
import { authHeaders } from '../config';

function ChangeExpenseTotals({ expenses }){
    const [amount, setAmount] = useState('');
    const [vendor, setVendor] = useState('Other');
    const vendors = [...new Set(expenses.map(e => e.store))];
    const [type, setType] = useState('positive');

    function handleChange() {
        fetch(`${API_URL}/expenses/change-vendor-totals`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ vendor, amount, type })
        })
        .then(response => response.text())
        .then(message => {alert(message);})
        .catch(error => console.error(error));
    }
    return(
        <div>
            <h2>Change Expense Totals</h2>
            <select value={vendor} onChange={(e) => setVendor(e.target.value)}>
                 <option value="" disabled>Select a Vendor</option>
                {vendors.map((vndr) => (
                    <option key={vndr.value} value={vndr.value}>
                        {vndr.label}
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
    )
}
export default ChangeExpenseTotals;