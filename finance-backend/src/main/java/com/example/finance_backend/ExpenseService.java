package com.example.finance_backend;

import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class ExpenseService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TimeRepository timeRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, TimeRepository timeRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.timeRepository = timeRepository;
    }

    public List<Expense> getExpenses(String email) {
        User owner = getOwner(email);
        return expenseRepository.findAllByOwner(owner);
    }


    public void parseAndSaveCSV(MultipartFile file, String email) throws Exception{
        User owner = getOwner(email);
        long daysBetween  = 1;
        StoreData other = new StoreData("Other");
        Map<String, StoreData> storeDataMap = Map.of( "KKS", new StoreData("KOLLEGE"), "Venmo", new StoreData("VENMO"),
                "Blarnes", new StoreData("BLARNEY"), "Royal", new StoreData("ROYAL"), "TopTen", new StoreData("LIQUOR"),
                "Uber", new StoreData("UBER"), "Chipotle", new StoreData("CHIPOTLE"), "DoorDash", new StoreData("DOORDASH"),
                "McDonald's", new StoreData("MCDONALDS"));

        double kkSpending = 0, salsSpending = 0, blarnesSpending = 0, topSpending = 0, royalSpending = 0,
                chipotle = 0, mcDonalds = 0, venmo = 0, genAmount = 0, uber = 0, doordash = 0;
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
                    entry.getValue().setAmount(entry.getValue().getAmount() + amount);
                    entry.getValue().setNumPurchases(entry.getValue().getNumPurchases() + 1);
                    otherCheck = false;
                    break;
                }
            }
            if(otherCheck){
                other.setAmount(other.getAmount() + amount);
                other.setNumPurchases(other.getNumPurchases() + 1);
            }
            /*
            if(amount > 0 && store.contains("VENMO"))
                venmo -= amount;
            //theres a better way of doing this fs
            if(amount < 0){
                amount = Math.abs(amount);
                if (store.contains("KOLLEGE"))
                    kkSpending += amount;
                else if (store.contains("SALLYS")) {
                    salsSpending += amount;
                } else if (store.contains("BLARNEY")) {
                    blarnesSpending += amount;
                } else if (store.contains("ROYAL"))
                    royalSpending += amount;
                else if (store.contains("LIQUOR"))
                    topSpending += amount;
                else if (store.contains("CHIPOTLE"))
                    chipotle += amount;
                else if (store.contains("MCDONALDS"))
                    mcDonalds += amount;
                else if (store.contains("VENMO"))
                    venmo += amount;
                else if (store.contains("UBER"))
                    uber += amount;
                else if (store.contains("DOORDASH"))
                    doordash += amount;
                else
                    genAmount += amount;
            }

             */
        }
        String date = " -- " + dayFinal + " to " + day1;
        LocalDate date1 = LocalDate.parse(day1, formatter);
        LocalDate date2 = LocalDate.parse(dayFinal, formatter);
        daysBetween = ChronoUnit.DAYS.between(date1, date2);
        long months = Math.abs(daysBetween/30);
        for(Map.Entry<String, StoreData> entry : storeDataMap.entrySet()){
            expenseRepository.save(new Expense(entry.getKey(), entry.getValue().getAmount(), owner));
        }
        expenseRepository.save(new Expense("Other", other.getAmount(), owner));
        /*
        expenseRepository.save(new Expense("KKs", kkSpending, owner));
        expenseRepository.save(new Expense("Sals", salsSpending, owner));
        expenseRepository.save(new Expense("Blarnes", blarnesSpending, owner));
        expenseRepository.save(new Expense("Royal", royalSpending, owner));
        expenseRepository.save(new Expense("TopTen", topSpending, owner));
        expenseRepository.save(new Expense("Chipotle", chipotle, owner));
        expenseRepository.save(new Expense("McDonald's", mcDonalds, owner));
        expenseRepository.save(new Expense("DoorDash", doordash, owner));
        expenseRepository.save(new Expense("Uber", uber, owner));
        expenseRepository.save(new Expense("Other", genAmount + venmo, owner));

         */
        timeRepository.save(new Time(months, date, owner));


        csvReader.close();
    }

    public void changeVendorTotals(String store, String type, double amount, String email) {
        User owner = getOwner(email);
        Expense expense = expenseRepository.findFirstByStoreAndOwner(store, owner);
        if(type.equals("positive")){
            expense.setAmount(expense.getAmount() + amount);
        }
        else{
            expense.setAmount(expense.getAmount() - amount);
        }
        expenseRepository.save(expense);
    }

    public Expense getExpensesByStore(String store, String email) {
        User owner = getOwner(email);
        return expenseRepository.findFirstByStoreAndOwner(store, owner);
    }

    @Transactional
    public void deleteAllExpenses(String email) {
        User owner = getOwner(email);
        System.out.println("ExpenseService1");
        expenseRepository.deleteAllByOwner(owner);
        timeRepository.deleteAllByOwner(owner);
    }
    public String getDate(String email){
        User owner = getOwner(email);
        Time date = timeRepository.findFirstByOwner(owner);
        if(date == null)
            return "";
        else
            return date.getDate();
    }

    private User getOwner(String email) {
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

    private int find(Map<String,Integer> map, String... keys) {
        for (String k : keys) {
            if (map.containsKey(k)) return map.get(k);
        }
        throw new RuntimeException("Missing required column");
    }
}

