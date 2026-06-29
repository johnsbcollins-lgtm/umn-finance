package com.example.finance_backend.User;

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

    public UserService(ExpenseRepository expenseRepository, UserRepository userRepository,
                       TimeRepository timeRepository, IncomeRepository incomeRepository){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.timeRepository = timeRepository;
        this.incomeRepository = incomeRepository;
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
                Map.entry("KKS", new StoreData("KOLLEGE")),
                Map.entry("Venmo", new StoreData("VENMO")),
                Map.entry("Blarnes", new StoreData("BLARNEY")),
                Map.entry("Royal", new StoreData("ROYAL")),
                Map.entry("TopTen", new StoreData("LIQUOR")),
                Map.entry("Uber", new StoreData("UBER")),
                Map.entry("Chipotle", new StoreData("CHIPOTLE")),
                Map.entry("DoorDash", new StoreData("DOORDASH")),
                Map.entry("McDonald's", new StoreData("MCDONALDS")),
                Map.entry("Payroll", new StoreData("PAYROLL")),
                Map.entry("Account Transfer", new StoreData("ONLINE TRANSFER")),
                Map.entry("Zelle", new StoreData("ZELLE")),
                Map.entry("Lineleap", new StoreData("LINELEAP")),
                Map.entry("Subscriptions", new StoreData("RECURRING")),
                Map.entry("Vending Machines", new StoreData("CANTEEN"))
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
                    amount = amount * -1;
                    other.setExpenses(other.getExpenses() + amount);
                    other.setNumPurchases(other.getNumPurchases() + 1);
                }
                else{
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
            expenseRepository.save(new Expense(entry.getKey(), entry.getValue().getExpenses(), owner, entry.getValue().getNumPurchases()));
            incomeRepository.save(new Income(entry.getKey(), entry.getValue().getIncome(), owner, entry.getValue().getNumDeposits()));
        }
        expenseRepository.save(new Expense("Other", other.getExpenses(), owner, other.getNumPurchases()));
        incomeRepository.save(new Income("Other", other.getIncome(), owner, other.getNumDeposits()));

        timeRepository.save(new Time(months, date, owner));


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


    private int find(Map<String,Integer> map, String... keys) {
        for (String k : keys) {
            if (map.containsKey(k)) return map.get(k);
        }
        throw new RuntimeException("Missing required column");
    }
}


