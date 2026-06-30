package com.example.finance_backend.User;

import com.example.finance_backend.Category.CategoryService;
import com.example.finance_backend.Finances.Expense;
import com.example.finance_backend.Finances.ExpenseRepository;

import com.example.finance_backend.Income.Income;
import com.example.finance_backend.Income.IncomeRepository;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.io.InputStreamReader;
import java.util.*;


@Service
public class UserService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TimeRepository timeRepository;
    private final IncomeRepository incomeRepository;
    private final CategoryService categoryService;

    public UserService(ExpenseRepository expenseRepository, UserRepository userRepository,
                       TimeRepository timeRepository, IncomeRepository incomeRepository, CategoryService categoryService){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.timeRepository = timeRepository;
        this.incomeRepository = incomeRepository;
        this.categoryService = categoryService;
    }
    public void findCommonWords(MultipartFile file, String email) throws Exception{
        User owner = getOwner(email);
        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
        String[] row;
        row = csvReader.readNext();
        Map<String, Integer> wordMap = new HashMap<>();
        while((row = csvReader.readNext()) != null) {
            String[] words = row[1].split(" ");
            for (String word : words) {
                if (!word.matches(".*\\d.*")) {
                    if (wordMap.containsKey(word)) {
                        wordMap.put(word, wordMap.get(word) + 1);
                    } else {
                        wordMap.put(word, 1);
                    }
                }
            }
        }
        csvReader.close();
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(wordMap.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue()); // descending order

        for (Map.Entry<String, Integer> entry : sorted) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    public void parseAndSaveCSV(MultipartFile file, String email) throws Exception{
        User owner = getOwner(email);
        long daysBetween  = 1;
        StoreData other = new StoreData("Other");
        Map<String, StoreData> storeDataMap = Map.ofEntries(
                Map.entry("KKS", new StoreData("KOLLEGE", "social")),
                Map.entry("Venmo", new StoreData("VENMO", "money")),
                Map.entry("Blarnes", new StoreData("BLARNEY", "social")),
                Map.entry("Royal", new StoreData("ROYAL", "personal")),
                Map.entry("TopTen", new StoreData("LIQUOR", "social")),
                Map.entry("Uber", new StoreData("UBER", "personal")),
                Map.entry("Chipotle", new StoreData("CHIPOTLE", "food")),
                Map.entry("DoorDash", new StoreData("DOORDASH", "food")),
                Map.entry("McDonald's", new StoreData("MCDONALDS", "food")),
                Map.entry("Payroll", new StoreData("PAYROLL", "money")),
                Map.entry("Account Transfer", new StoreData("ONLINE TRANSFER", "money")),
                Map.entry("Zelle", new StoreData("ZELLE", "money")),
                Map.entry("Lineleap", new StoreData("LINELEAP", "social")),
                Map.entry("Subscriptions", new StoreData("RECURRING", "subscriptions")),
                Map.entry("Vending Machines", new StoreData("CANTEEN", "food")),
                Map.entry("Target", new StoreData("TARGET", "personal")),
                Map.entry("Sals", new StoreData("SALLYS", "social")),
                Map.entry("Canes", new StoreData("CANES", "food")),
                Map.entry("Qdoba", new StoreData("QDOBA", "food")),
                Map.entry("DP Dough", new StoreData("DP DOUGH", "food")),
                Map.entry("Starbucks", new StoreData("STARBUCKS", "food"))
        );

        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
        String dayFinal, day1;
        String[] row;
        row = csvReader.readNext();
        Map<String, Integer> indexMap = new HashMap<>();

        for (int i = 0; i < row.length; i++) {
            indexMap.put(row[i].trim().toLowerCase(), i);
        }
        int dateIdx = find(indexMap, "date", "transaction date", "posted date");
        int descIdx = find(indexMap, "description", "details", "merchant", "memo");
        int amountIdx = find(indexMap, "amount", "transaction amount");

        day1 = csvReader.peek()[dateIdx];
        dayFinal = day1;
        //gotta add difference between spending and income later
        while((row = csvReader.readNext()) != null){
            boolean otherCheck = true;
            dayFinal = row[dateIdx];
            String store = row[descIdx];
            double amount = Double.parseDouble(row[amountIdx]);
            amount = Math.round(amount * 100.0) / 100.0;
            store = store.toUpperCase();
            for(Map.Entry<String, StoreData> entry : storeDataMap.entrySet()){
                if(store.contains(entry.getValue().getVendor())){
                    if (amount < 0) {
                        amount = amount * -1;
                        entry.getValue().setExpenses(entry.getValue().getExpenses() + amount);
                        entry.getValue().setNumPurchases(entry.getValue().getNumPurchases() + 1);
                    }
                    else{
                        entry.getValue().setIncome(entry.getValue().getIncome() + amount);
                        entry.getValue().setNumDeposits(entry.getValue().getNumDeposits() + 1);
                    }
                    otherCheck = false;
                    break;
                }
            }
            if(otherCheck) {
                if (amount < 0) {
                    System.out.println("Description: " + row[descIdx] + " Amount: " + row[amountIdx] + "");
                    amount = amount * -1;
                    other.setExpenses(other.getExpenses() + amount);
                    other.setNumPurchases(other.getNumPurchases() + 1);
                }
                else{
                    System.out.println("Description: " + row[descIdx] + " Amount: " + row[amountIdx] + "");
                    other.setIncome(other.getIncome() + amount);
                    other.setNumDeposits(other.getNumDeposits() + 1);
                }
            }
        }
        String date = " -- " + dayFinal + " to " + day1;
        LocalDate date1 = LocalDate.parse(day1, formatter);
        LocalDate date2 = LocalDate.parse(dayFinal, formatter);
        daysBetween = ChronoUnit.DAYS.between(date1, date2);
        long months = Math.abs(daysBetween/30);
        for(Map.Entry<String, StoreData> entry : storeDataMap.entrySet()){
                expenseRepository.save(new Expense(entry.getKey(), entry.getValue().getExpenses(),
                        owner, entry.getValue().getNumPurchases(), entry.getValue().getCategory()));
                incomeRepository.save(new Income(entry.getKey(), entry.getValue().getIncome(),
                        owner, entry.getValue().getNumDeposits()));
        }
        expenseRepository.save(new Expense("Other", other.getExpenses(), owner, other.getNumPurchases(), "other"));
        incomeRepository.save(new Income("Other", other.getIncome(), owner, other.getNumDeposits(), "other"));
        timeRepository.save(new Time(months, date, owner));
        handleDupes(owner);
        categoryService.handleCategories(owner);
        csvReader.close();
    }

    public String getDate(String email){
        User owner = getOwner(email);
        Time date = timeRepository.findFirstByOwner(owner);
        if(date == null)
            return "";
        else
            return date.getDate();
    }

    public User getOwner(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public double getMonth(String email) {
        User owner = getOwner(email);
        Time date = timeRepository.findFirstByOwner(owner);
        if(date == null) {
            System.out.println("Date: null");
            return 1;
        }
        else{
            System.out.println("Date: " + date.getMonths());
            return date.getMonths();
        }
    }
    private void handleDupes(User owner){
        List<Expense> expenses = expenseRepository.findAllByOwner(owner);
        List<Income> income = incomeRepository.findAllByOwner(owner);
        for(Expense e : expenses){
            for(Income i : income){
                if(e.getStore().equals(i.getStore())){
                    if(e.getAmount() > i.getAmount()){
                        e.setAmount(e.getAmount() - i.getAmount());
                        expenseRepository.save(e);
                        incomeRepository.delete(i);
                    }
                    else{
                        i.setAmount(i.getAmount() - e.getAmount());
                        incomeRepository.save(i);
                        expenseRepository.delete(e);
                    }
                }
            }
        }
    }

    private int find(Map<String,Integer> map, String... keys) {
        for (String k : keys) {
            if (map.containsKey(k)) return map.get(k);
        }
        throw new RuntimeException("Missing required column");
    }
}


