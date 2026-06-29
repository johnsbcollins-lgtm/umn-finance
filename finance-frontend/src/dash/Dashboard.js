import React,{ useState, useEffect } from 'react'; 
import '../App.css';
import ExpenseList from './ExpenseList';
import UploadCsv from './UploadCsv';
import AvMonSpend from './AvMonSpend'
import API_URL from '../config';
import { authHeaders } from '../config';
import { authHeadersForFormData } from '../config';
import ChangeVendorTotals from './ChangeVendorTotals';
import IncomeList from './IncomeList';
function Dashboard(){
const [expenses, setExpenses] = useState([]);
const [dates, setDates] = useState('');
const [months, setMonths] = useState(null);
const [income, setIncome] = useState([]);

const fetchData = () => {
    fetch(`${API_URL}/expenses`, {
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => setExpenses(data))
    .catch(error => console.error("expenses error:", error));

    fetch(`${API_URL}/user/date`, {
        headers: authHeaders()
    })
    .then(response => response.text())
    .then(data => setDates(data))
    .catch(error => console.error("date error:", error));

    fetch(`${API_URL}/user/months`, {
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => setMonths(data))
    .catch(error => console.error("months error:", error));
    fetch(`${API_URL}/income`,{
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => setIncome(data))
    .catch(error => console.error("income error:", error))
};

useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 30000);
    return () => clearInterval(interval);
// eslint-disable-next-line react-hooks/exhaustive-deps
}, []);

    function clearDatabase() {
    fetch(`${API_URL}/expenses/all`, {
        method: 'DELETE',
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); fetchData();})
    .catch(error => console.error(error));
  }
  const total = expenses.reduce((sum, expense) => sum + parseFloat(expense.amount), 0);


    return  (
      <div className="Dashboard">
        <h1> UMN Student Finance Dashboard {dates}</h1>
         <div className="lists-container">
            <ExpenseList expenses={expenses} total={total}/>
            <IncomeList income={income} total={total}/>
        </div>
         <h2>Total Spending: ${total.toFixed(2)}</h2>
         <AvMonSpend total={total} months={months}/>
         <UploadCsv onUpload={fetchData}/>
         <ChangeVendorTotals />
         <button onClick={clearDatabase}>Clear All Expenses</button>
        </div>
    );
  }

  export default Dashboard;