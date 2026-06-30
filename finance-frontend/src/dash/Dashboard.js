import React,{ useState, useEffect } from 'react'; 
import '../App.css';
import ExpenseList from './ExpenseList';
import UploadCsv from './UploadCsv';
import AvMonSpend from './AvMonSpend'
import API_URL from '../config';
import { authHeaders } from '../config';
import { authHeadersForFormData } from '../config';
import ChangeExpenseTotals from './ChangeExpenseTotals';
import ChangeIncomeTotals from './ChangeIncomeTotals';
import IncomeList from './IncomeList';
import AvMonDeposit from './AvgMonDeposit';
import CategoriesList from './CategoriesList';

function Dashboard(){
const [expenses, setExpenses] = useState([]);
const [dates, setDates] = useState('');
const [months, setMonths] = useState(null);
const [income, setIncome] = useState([]);
const [categories, setCategories] = useState([]);
const [categoryTotals, setCategoryTotals] = useState(0);

const fetchData = () => {
    fetch(`${API_URL}/expenses`, {
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => {
        console.log("expenses data:", data[0]);
        setExpenses(data);})
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
    .then(data => {
        console.log("income data:", data[0]);
        setIncome(data);})
    .catch(error => console.error("income error:", error))

    fetch(`${API_URL}/category`, {
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => {
        console.log("category data:", data[0]);
        setCategories(data);
    })
    .catch(error => console.error("category error:", error));
    fetch(`${API_URL}/category/total`, {
        headers: authHeaders()
    })
    .then(response => response.json())
    .then(data => {setCategoryTotals(data);})
    .catch(error => console.error("category total error:", error));
};

useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 30000);
    return () => clearInterval(interval);
// eslint-disable-next-line react-hooks/exhaustive-deps
}, []);

    function clearExpenses() {
    fetch(`${API_URL}/expenses/all`, {
        method: 'DELETE',
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); fetchData();})
    .catch(error => console.error(error));
    }
    function clearIncome() {
    fetch(`${API_URL}/income/all`, {
        method: 'DELETE',
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); fetchData();})
    .catch(error => console.error(error));
    }
    function clearCategories() {
    fetch(`${API_URL}/category/all`, {
        method: 'DELETE',
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); fetchData();})
    .catch(error => console.error(error));
    }
  const totalExpense = expenses.reduce((sum, expense) => sum + parseFloat(expense.amount), 0);
  const totalIncome = income.reduce((sum, income) => sum + parseFloat(income.amount), 0);
    

    return  (
      <div className="Dashboard">
        <h1> UMN Student Finance Dashboard {dates}</h1>

         <div className="lists-container">
            <ExpenseList expenses={expenses} total={totalExpense}/>
            <div>
                <IncomeList income={income} total={totalIncome}/>
                <CategoriesList categories={categories}/>
                <h2>Category Totals: ${categoryTotals.toFixed(2)}</h2>
            </div>
        
        </div>

         <div className="spending-summary-container">
            <h2>Total Spending: ${totalExpense.toFixed(2)}</h2>
            <h2>Total Income: ${totalIncome.toFixed(2)}</h2>
            <AvMonSpend total={totalExpense} months={months}/>
            <AvMonDeposit total={totalIncome} months={months}/>
         </div>

        <UploadCsv onUpload={fetchData}/>

        <div className="change-container">
            <ChangeExpenseTotals expenses={expenses}/>
            <ChangeIncomeTotals income={income}/>
             <button onClick={clearExpenses}>Clear All Expenses</button>
              <button onClick={clearIncome}>Clear All Income</button>
              <button onClick={clearCategories}>Clear All Categories</button>
        </div>

        </div>
    );
  }

  export default Dashboard;