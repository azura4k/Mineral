package com.azura4k.mcpe.Payroll;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import net.lldv.llamaeconomy.LlamaEconomy;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PayRollAPI{

    private static PayrollBase plugin;

    private static Config BusinessData;
    private static Config JobList;
    private static Config Clock;
    public static Config PluginConfig;


    public static void Initialize(PayrollBase instance) {
        instance.saveResource("/data/business.yml");
        BusinessData = new Config(instance.getDataFolder() + "/data/business.yml", Config.YAML);
        instance.getLogger().info("Business Data YAML Ready.");

        instance.saveResource("/data/joblist.yml");
        JobList = new Config(instance.getDataFolder() + "/data/joblist.yml", Config.YAML);
        instance.getLogger().info("Joblist Data YAML Ready.");

        instance.saveResource("/data/clock.yml");
        Clock = new Config(instance.getDataFolder() + "/data/clock.yml", Config.YAML);
        instance.getLogger().info("Clock Data YAML Ready.");

        PluginConfig = instance.getConfig();

        plugin = instance;
    }



    public boolean CreateBusiness(String businessName, String businessDesc, Player owner) {

        if (NameNotTook(businessName)) {
            BusinessData.set(businessName + ".name", businessName);
            BusinessData.set(businessName + ".desc", businessDesc);
            BusinessData.set(businessName + ".ownerName", owner.getName());
            BusinessData.set(businessName + ".creationdate", getDate());
            BusinessData.set(businessName + ".balance", (double) 0);
            BusinessData.set(businessName + ".trustedrank", 4);
            BusinessData.set(businessName + ".maxrank", 5);
            BusinessData.set(businessName + ".minrank", 1);
            BusinessData.save();
            BusinessData.reload();

            //Load Owner as Employee
            HireOwner(businessName, owner, "Founder", 20, 5, PluginConfig.getDouble("MinimumWage"));

            return true;
        }
        return false;
    }
    public void DeleteBusiness(Business business, Player commandeer) {
        try {
            if (commandeer == business.Owner || commandeer.isOp()) {
                BusinessData.remove(business.BusinessName);
                BusinessData.save();
                BusinessData.reload();
            }
        } catch (Exception ignored){
        }
    }
    public static void SaveNewBusinessData(Business business) {
        BusinessData.reload();
        BusinessData.set(business.BusinessName  + ".desc", business.BusinessDesc);
        BusinessData.set(business.BusinessName  + ".ownerName", business.Owner.getName());
        BusinessData.set(business.BusinessName  + ".balance", business.Balance);
        BusinessData.set(business.BusinessName  + ".trustedrank", business.TrustedRank);
        BusinessData.set(business.BusinessName  + ".maxrank", business.MaxRank);
        BusinessData.set(business.BusinessName  + ".minrank", business.MinRank);
        BusinessData.save();
        BusinessData.reload();
    }
    public Business LoadBusiness(String businessName){
        Business business = new Business();
        if (BusinessData.exists(businessName, true)){
            business.BusinessName = BusinessData.get(businessName + ".name").toString();
            business.BusinessDesc = BusinessData.get(businessName + ".desc").toString();
            business.CreationDate = BusinessData.get(businessName + ".creationdate").toString();
            business.Owner = plugin.getServer().getPlayerExact(BusinessData.get(businessName + ".ownerName").toString());
            business.Balance = (double) BusinessData.get(businessName + ".balance");
            business.TrustedRank = (int) BusinessData.get(businessName + ".trustedrank");
            business.MaxRank = (int) BusinessData.get(businessName + ".maxrank");
            business.MinRank = (int) BusinessData.get(businessName + ".minrank");

            try {
                Set<String> Employees = BusinessData.getSection(businessName + ".employees").getKeys(false);

                for (String EmployeeName : Employees) {
                    String key = businessName + ".employees." + EmployeeName.toLowerCase();
                    Employee employee = new Employee();
                    employee.EmployerName = business.BusinessName;
                    employee.playerUUID = UUID.fromString(BusinessData.getString(key + ".PlayerUUID"));
                    employee.PlayerName = BusinessData.getString(key + ".PlayerName");
                    employee.StartDate = BusinessData.getString(key + ".StartDate");
                    employee.Title = BusinessData.getString(key + ".Title");
                    employee.MaximumMinutes = BusinessData.getDouble(key + ".MaxHrs");
                    employee.Rank = BusinessData.getInt(key + ".Rank");
                    employee.Wage = BusinessData.getDouble(key + ".Wage");
                    employee.Fired = BusinessData.getBoolean(key + ".Fired");
                    employee.FiredDate = BusinessData.getString(key + ".FiredDate");
                    employee.MinutesWorkedPerPayPeriod = BusinessData.getDouble(key + ".MinutesWorked");
                    employee.TotalWorkMinutes = BusinessData.getDouble(key + ".TotalMinutesWorked");
                    business.Employees.add(employee);
                }
            }
            catch (Exception ignored){}
            return business;
        }
        else {
            return null;
        }
    }
    public void TransferOwner(Player player, Business business, String newOwnerName) {
        try {

            String key = business.BusinessName + ".ownerName";
            BusinessData.set(key, newOwnerName);
            HireOwner(business.BusinessName, plugin.getServer().getPlayerExact(newOwnerName), "New Owner", 20, business.MaxRank, 20);
            Employee OldOwner = LoadEmployee(business, player.getName());
            OldOwner.Rank = business.MaxRank - 1;

        }
        catch (Exception ignored){}

    }
    public void OfferPosition(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        //If business data does not exist
        try {
            String key2 = player.getUniqueId().toString() + "." + businessName;
            String key = businessName + ".employees." + player.getName().toLowerCase();
            if (!BusinessData.exists(key, true)) {
                JobList.set(key2 + ".Name", player.getUniqueId().toString());
                JobList.set(key2 + ".PlayerUUID", player.getUniqueId().toString());
                JobList.set(key2 + ".StartDate", getDate());
                JobList.set(key2 + ".Title", title);
                JobList.set(key2 + ".PlayerName", player.getName());
                JobList.set(key2 + ".MaxHrs", MaxMinutes);
                JobList.set(key2 + ".Rank", rank);
                JobList.set(key2 + ".Wage", wage);
                JobList.set(key2 + ".Fired", false);
                JobList.set(key2 + ".FiredDate", "null");
                JobList.set(key2 + ".MinutesWorked", 0.0);
                JobList.set(key2 + ".TotalMinutesWorked", 0.0);
                JobList.save();
                JobList.reload();
            }
        }
        catch (Exception ignored){}
    }
    public void AcceptPosition(Player player, String businessName){
        String key = player.getUniqueId().toString() + "." + businessName;

        HireEmployee(businessName, player,JobList.getString(key + ".Title"),  JobList.getDouble(key + ".MaxHrs"),JobList.getInt(key + ".Rank") ,JobList.getDouble(key + ".Wage"));

        JobList.getSection(player.getUniqueId().toString()).remove(businessName);
        JobList.save();
        JobList.reload();
    }
    public void DenyPosition(Player player, String businessName){
        JobList.getSection(player.getUniqueId().toString()).remove(businessName);
        JobList.save();
        JobList.reload();
    }
    public Employee LoadPositionDetails(Player player, String businessName){
        Employee employee = new Employee();
        String key = player.getUniqueId().toString() + "." + businessName;

        employee.EmployerName = JobList.getString(key + ".Name");
        employee.playerUUID = UUID.fromString(JobList.getString(key + ".PlayerUUID"));
        employee.Title = JobList.getString(key + ".Title");
        employee.PlayerName = JobList.getString(key + ".PlayerName");
        employee.MaximumMinutes = JobList.getDouble(key + ".MaxHrs");
        employee.Rank = JobList.getInt(key + ".Rank");
        employee.Wage = JobList.getDouble(key + ".Wage");

        return employee;
    }
    public ArrayList<String> getAllJobOffers(Player player) {
        JobList.reload();

        Set<String> Businesses =  JobList.getSections(player.getUniqueId().toString()).getKeys(false);
        return new ArrayList<>(Businesses);
    }
    public void HireEmployee(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        String key = businessName + ".employees." + player.getName().toLowerCase();
        if (!BusinessData.exists(key, true)){
            BusinessData.set(key + ".PlayerUUID", player.getUniqueId().toString());
            BusinessData.set(key + ".StartDate", getDate());
            BusinessData.set(key + ".Title", title);
            BusinessData.set(key + ".PlayerName", player.getName());
            BusinessData.set(key + ".MaxHrs", MaxMinutes);
            BusinessData.set(key + ".Rank", rank);
            BusinessData.set(key + ".Wage", wage);
            BusinessData.set(key + ".Fired", false);
            BusinessData.set(key + ".FiredDate", "null");
            BusinessData.set(key + ".MinutesWorked", 0.0);
            BusinessData.set(key + ".TotalMinutesWorked", 0.0);
            BusinessData.save();

            JobList.remove(player.getUniqueId().toString() + "." + businessName);
        }
    }
    public void HireOwner(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        //If business data does not exist
        String key = businessName + ".employees." + player.getName().toLowerCase();
        if (!BusinessData.exists(key, true)){
            BusinessData.set( key + ".PlayerUUID", player.getUniqueId().toString());
            BusinessData.set(key + ".StartDate", getDate());
            BusinessData.set(key + ".Title", title);
            BusinessData.set(key + ".PlayerName", player.getName());
            BusinessData.set(key + ".MaxHrs", MaxMinutes);
            BusinessData.set(key+ ".Rank", rank);
            BusinessData.set(key + ".Wage", wage);
            BusinessData.set(key + ".Fired", false);
            BusinessData.set(key + ".FiredDate", "null");
            BusinessData.set(key + ".MinutesWorked", 0.0);
            BusinessData.set(key + ".TotalMinutesWorked", 0.0);
            BusinessData.save();
        }
    }
    public void FireEmployee(Employee employee){
            BusinessData.getSection(employee.EmployerName).getSection("employees").remove(employee.PlayerName);
            BusinessData.save();
            BusinessData.reload();
    }
    public Employee LoadEmployee(Business business, String EmployeeName){
        String key = business.BusinessName + ".employees." + EmployeeName.toLowerCase();
        Employee employee = new Employee();
        //Check for Employment
        if (BusinessData.exists(key, true)){
            employee.EmployerName = business.BusinessName;
            employee.playerUUID = UUID.fromString(BusinessData.getString(key+ ".PlayerUUID"));
            employee.PlayerName = BusinessData.getString(key+ ".PlayerName");
            employee.StartDate = BusinessData.getString(key +".StartDate");
            employee.Title = BusinessData.getString(key + ".Title");
            employee.MaximumMinutes = BusinessData.getDouble(key + ".MaxHrs");
            employee.Rank = BusinessData.getInt(key + ".Rank");
            employee.Wage = BusinessData.getDouble(key + ".Wage");
            employee.Fired = BusinessData.getBoolean(key + ".Fired");
            employee.FiredDate = BusinessData.getString(key + ".FiredDate");
            employee.MinutesWorkedPerPayPeriod = BusinessData.getDouble(key + ".MinutesWorked");
            employee.TotalWorkMinutes = BusinessData.getDouble(key + ".TotalMinutesWorked");
            return employee;
        }
        return null;
    }
    public static void SaveNewEmployeeData(Employee employee) {
        String key = employee.EmployerName + ".employees." + employee.PlayerName;
        if (BusinessData.exists(key, true)){
            BusinessData.reload();
            BusinessData.set(key + ".Title", employee.Title);
            BusinessData.set(key + ".MaxHrs", employee.MaximumMinutes);
            BusinessData.set(key+ ".Rank", employee.Rank);
            BusinessData.set(key + ".Wage", employee.Wage);
            BusinessData.set(key + ".Fired", employee.Fired);
            BusinessData.set(key + ".FiredDate", employee.FiredDate);
            BusinessData.set(key + ".MinutesWorked",employee.MinutesWorkedPerPayPeriod);
            BusinessData.set(key + ".TotalMinutesWorked", employee.TotalWorkMinutes);
            BusinessData.save();
            BusinessData.reload();
        }
    }
    public ArrayList<String> getBusinessesEmployedAt(Player player){

        ArrayList<String> EmployedBusinesses = new ArrayList<>();
        BusinessData.reload();
        for (String s : BusinessData.getKeys(false)) {
            Business business = LoadBusiness(s);
            for (int i = 0; i < business.Employees.size(); i++) {
                if (Objects.equals(business.Employees.get(i).playerUUID, player.getUniqueId())) {
                    EmployedBusinesses.add(s);
                }
            }
        }
        return EmployedBusinesses;
    }

    public void PayEmployeeMinutelyRate(Employee employee){
        double TimeWorked = employee.MinutesWorkedPerPayPeriod;
        double Tax;
        Tax = plugin.getConfig().getDouble("TaxPercent");
        Double Income = TimeWorked * employee.Wage;
        Double TaxSubtraction = Income * Tax;

        LlamaEconomy.getAPI().addMoney(employee.playerUUID, Income - TaxSubtraction);

        plugin.getServer().getPlayerExact(employee.PlayerName).sendMessage(getLanguage("PayOutEmployee") + (Income - TaxSubtraction) + getLanguage("PayOutEmployeeBusiness") + employee.EmployerName);


        Business Employer = LoadBusiness(employee.EmployerName);
        Employer.Balance -= Income - TaxSubtraction;
        employee.MinutesWorkedPerPayPeriod = 0.0;
        Employer.SaveData();
        employee.SaveData();
    }

    public void RegisterOnClock(Player player,Employee employee) {
        String Key = employee.PlayerName.toLowerCase();
        if (!Clock.isSection(Key) && LoadBusiness(employee.EmployerName).Balance > 0) {
            Clock.set(Key + ".ClockInTime", getTime());
            Clock.set(Key + ".MaxMinutes", employee.MaximumMinutes);
            Clock.set(Key + ".BusinessName", employee.EmployerName);
            Clock.save();
            Clock.reload();
        }
        else {
            player.sendMessage(getLanguage("CannotClockInOrInDebt"));
        }
    }
    public void RegisterOffClock(Employee employee) {
        String Key = employee.PlayerName.toLowerCase();
        if (Clock.isSection(Key)) {
            long ClockInTime = Clock.getLong(Key + ".ClockInTime");
            long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(getTime() - ClockInTime ));
            employee.MinutesWorkedPerPayPeriod += Difference;
            employee.TotalWorkMinutes += Difference;
            employee.SaveData();
            Clock.remove(employee.PlayerName);
            Clock.save();
            Clock.reload();
        }
        else{plugin.getServer().getPlayerExact(employee.PlayerName).sendMessage(getLanguage("NotClockedIn"));}
    }

    public void ForceClockOut(Player player) {
        String Key0 = player.getName().toLowerCase();
        String Key1 = player.getName().toLowerCase() + ".ClockInTime";
        String Key2 = player.getName().toLowerCase()  + ".BusinessName";

        if (Clock.isSection(Key0)) {
            long ClockInTime = Clock.getLong(Key1);
            String BusinessName = Clock.getString(Key2);
            long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(getTime() - ClockInTime ));
            Employee employee = LoadEmployee(LoadBusiness(BusinessName), player.getName());
            employee.MinutesWorkedPerPayPeriod += Difference;
            employee.TotalWorkMinutes += Difference;
            employee.SaveData();
            Clock.remove(employee.PlayerName);
            Clock.save();
            Clock.reload();
        }
    }

    public void windDownTimer() {
        for (String PlayerName: Clock.getKeys(false)) {
                String Key1 = PlayerName.toLowerCase() + ".ClockInTime";
                String Key2 = PlayerName.toLowerCase() + ".MaxMinutes";
                String Key3 = PlayerName.toLowerCase() + ".BusinessName";

                long ClockInTime = Clock.getLong(Key1);
                long MaxTime = TimeUnit.MINUTES.toMillis((long) Clock.getDouble(Key2));
                long Difference = getTime() - ClockInTime;

                if (Difference > MaxTime) {
                    Business business = LoadBusiness(Clock.getString(Key3));
                    RegisterOffClock(LoadEmployee(business, PlayerName));
                    plugin.getServer().getPlayerExact(PlayerName).sendMessage(getLanguage("ForceClockOut"));
                }

        }
    }



    //Utilities
    //Useful for Transactions Later
    public static String UIDGenerator(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static Date getDate() {
        return new Date();
    }

    public static long getTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static boolean NameNotTook(String Name){
       if (!BusinessData.exists(Name.toLowerCase(),true) && !Name.contains(".")){
           return Name.length() > 0;
       }
       else {
           return false;
       }
    }

    public static String getLanguage(String LanugageTag){
        return PluginConfig.getString("Language." + LanugageTag);
    }


}
