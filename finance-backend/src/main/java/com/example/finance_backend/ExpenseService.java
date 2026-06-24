package com.example.finance_backend;

import com.opencsv.CSVReader;
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

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }


    public void parseAndSaveCSV(MultipartFile file) throws Exception{
        long daysBetween  = 1;
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
            dayFinal = row[dateIdx];
            String store = row[descIdx];
            double amount = Double.parseDouble(row[amountIdx]);
            amount = Math.round(amount * 100.0) / 100.0;
            store = store.toUpperCase();
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
        }
        String date = " -- " + dayFinal + " to " + day1;
        LocalDate date1 = LocalDate.parse(day1, formatter);
        LocalDate date2 = LocalDate.parse(dayFinal, formatter);
        daysBetween = ChronoUnit.DAYS.between(date1, date2);
        long months = Math.abs(daysBetween/30);
        expenseRepository.save(new Expense("Months", months));
        expenseRepository.save(new Expense(date, 0));
        expenseRepository.save(new Expense("KKs", kkSpending));
        expenseRepository.save(new Expense("Sals", salsSpending));
        expenseRepository.save(new Expense("Blarnes", blarnesSpending));
        expenseRepository.save(new Expense("Royal", royalSpending));
        expenseRepository.save(new Expense("TopTen", topSpending));
        expenseRepository.save(new Expense("Chiptole", chipotle));
        expenseRepository.save(new Expense("McDonalds", mcDonalds));
        expenseRepository.save(new Expense("Doordash", doordash));
        expenseRepository.save(new Expense("Uber", uber));
        expenseRepository.save(new Expense("Other", genAmount + venmo));


        csvReader.close();
    }


    public Expense getExpensesByStore(String store) {
        return expenseRepository.findFirstByStore(store);
    }

    public void deleteAllExpenses() {
        expenseRepository.deleteAll();
    }
    public String getDate(){
        Expense dateExpense = expenseRepository.findFirstByStoreContaining("to");
        if(dateExpense == null)
            return "";
        else
            return dateExpense.getStore();
    }

    private int find(Map<String,Integer> map, String... keys) {
        for (String k : keys) {
            if (map.containsKey(k)) return map.get(k);
        }
        throw new RuntimeException("Missing required column");
    }
}

