function CategoriesList({ categories }) {
    const category = [...new Set(categories.map((e) => e.category))];
    return (
        <div>
            <h2>Categories</h2>
            {categories
                .sort((a, b) => category.indexOf(a.category) - category.indexOf(b.category))
                .map(category => (
                    <div key={category.id}>
                        <p>{category.category} : ${category.amount.toFixed(2)} - # of Purchases: {category.numPurchases
                            } - Avg. Expense: ${(category.amount 
                            / category.numPurchases).toFixed(2) || 0}</p>
                    </div>
                ))}
        </div>
    );
}
export default CategoriesList;