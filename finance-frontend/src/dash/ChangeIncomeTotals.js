import React, { useState} from 'react';
import API_URL from '../config';
import { authHeaders } from '../config';

function ChangeIncomeTotals(){
    const [amount, setAmount] = useState('');
        const [vendor, setVendor] = useState('Other');
        const vendors = [{value: 'KKs', label: 'KKs'}, 
            {value: 'Sals', label: 'Sals'}, 
            {value: 'Blarnes', label: 'Blarnes'}, 
            {value: 'Royal', label: 'Royal'}, 
            {value: 'TopTen', label: 'TopTen'}, 
            {value: 'Chipotle', label: 'Chipotle'}, 
            {value: "McDonald’s", label: "McDonald’s"}, 
            {value: 'DoorDash', label: 'DoorDash'}, 
            {value: 'Uber', label: 'Uber'}, 
            {value: 'Other', label: 'Other'}];
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


}