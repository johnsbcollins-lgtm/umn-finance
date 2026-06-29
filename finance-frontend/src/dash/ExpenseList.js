import React from 'react';
function ExpenseList({ expenses, total }) {
  const storeOrder = [...new Set(expenses.map(e => e.store))];
  return (
    //header(<h2>)
    //curly braces means run javascript
    //div general container element
    //map loops through elements in array and returns something for them
    //<p> is paragraph element
    //react needs a unique key to display elements of a list to keep track of them
    //use the id comes from SQL database
    <div>
      <h2>Expenses</h2>
      {expenses
    .sort((a, b) => storeOrder.indexOf(a.store) - storeOrder.indexOf(b.store))
    .map(expense => (
        <div key={expense.id}>
            <p>{expense.store} : ${expense.amount.toFixed(2)} - {(expense.amount/total * 
              100).toFixed(2)}% of total - # of Purchases: {expense.numPurchases
              } - Avg. Expense: ${(expense.amount 
              / expense.numPurchases).toFixed(2) || 0}</p>
        </div>
    ))
      }
    </div>
      );
}

export default ExpenseList;