function IncomeList({ income, total }) {
  const storeOrder = [...new Set(income.map(e => e.store))];
      if(total === 0)
        total = 0;
      return(
      <div>
      <h2>Income</h2>
      {income
    .sort((a, b) => storeOrder.indexOf(a.store) - storeOrder.indexOf(b.store))
    .map(income => (
        <div key={income.id}>
            <p>{income.store} : ${income.amount.toFixed(2)} - {(income.amount/total * 
              100).toFixed(2)}% of total - # of Deposits: {income.numDeposits
              } - Avg. Deposit: ${(income.amount 
              / income.numDeposits).toFixed(2) || 0}
              </p>
        </div>
        ))
    }
    </div>
  );

}

export default IncomeList;