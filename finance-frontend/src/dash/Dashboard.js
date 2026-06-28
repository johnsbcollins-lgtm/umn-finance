import React,{ useState, useEffect } from 'react'; 
import '../App.css';
import ExpenseList from './ExpenseList';
import UploadCsv from './UploadCsv';
import AvMonSpend from './AvMonSpend'
import API_URL from '../config';
import { authHeaders } from '../config';
import { authHeadersForFormData } from '../config';
function Dashboard(){
      //expenses is the variable, setExpenses modifies it
  const [expenses, setExpenses] = useState([]);
  const [dates, setDates] = useState('')
  //useEffect does stuff outside of rendering
    //fetch talks to the backend to get expenses
    //response is parsed to JSON
    //and put in expenses
  useEffect(() => {
      console.log("Token:", localStorage.getItem('token'));
      fetch(`${API_URL}/expenses`,{
          headers: authHeaders()
      })
        .then(response => response.json())
        .then(data => setExpenses(data));
      fetch(`${API_URL}/expenses/date`,{
        headers: authHeaders()
      })
        .then(response => response.text())
        .then(data => setDates(data))
    }, []);

    console.log("API :" + API_URL)
    function reloadUpload() {
      fetch(`${API_URL}/expenses`,{
          headers: authHeaders()
      })
        .then(response => response.json())
        .then(data => setExpenses(data));
      fetch(`${API_URL}/expenses/date`,{
          headers: authHeaders()
      })
          .then(response => response.text())
          .then(data => setDates(data));
    }

    useEffect(() => {
      reloadUpload();
    }, []);
    const total = expenses.slice(2).reduce((sum, expense) => sum + parseFloat(expense.amount), 0);

    function clearDatabase() {
    fetch(`${API_URL}/expenses/all`, {
        method: 'DELETE',
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); reloadUpload();})
    .catch(error => console.error(error));
  }
    


    return  (
      <div className="Dashboard">
        <h1> UMN Student Finance Dashboard {dates}</h1>
         <ExpenseList expenses={expenses} total = {total}/>
         <h2>Total Spending: ${total.toFixed(2)}</h2>
         <AvMonSpend expenses={expenses} total = {total}/>
         <UploadCsv onUpload={reloadUpload}/>
         <button onClick={clearDatabase}>Clear All Expenses</button>
        </div>
    );
  }

  export default Dashboard;