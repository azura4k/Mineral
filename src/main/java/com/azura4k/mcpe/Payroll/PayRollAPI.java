package com.azura4k.mcpe.Payroll;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                    employee.MaximumHours = BusinessData.getDouble(key + ".MaxHrs");
                    employee.Rank = BusinessData.getInt(key + ".Rank");
                    employee.Wage = BusinessData.getDouble(key + ".Wage");
                    employee.Fired = BusinessData.getBoolean(key + ".Fired");
                    employee.FiredDate = BusinessData.getString(key + ".FiredDate");
                    employee.HoursWorkedPerPayPeriod = BusinessData.getDouble(key + ".HoursWorked");
                    employee.TotalWorkHours = BusinessData.getDouble(key + ".TotalHoursWorked");
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
        String key = business.BusinessName + ".ownerName";
        BusinessData.set(key, newOwnerName);
        HireOwner(business.BusinessName, plugin.getServer().getPlayerExact(newOwnerName), "New Owner", 20,business.MaxRank, 20);
        Employee OldOwner = LoadEmployee(business,player.getName());
        OldOwner.Rank = business.MaxRank - 1;

    }
    public void OfferPosition(String businessName, Player player, String title, double MaxHours, int rank, double wage){
        //If business data does not exist
        String key2 = player.getUniqueId().toString() + "." + businessName;

        String key = businessName + ".employees." + player.getName().toLowerCase();

        if (!BusinessData.exists(key, true)){
            JobList.set(key2 + ".Name", player.getUniqueId().toString());
            JobList.set(key2 + ".PlayerUUID", player.getUniqueId().toString());
            JobList.set(key2 + ".StartDate", getDate());
            JobList.set(key2 + ".Title", title);
            JobList.set(key2 + ".PlayerName", player.getName());
            JobList.set(key2 + ".MaxHrs", MaxHours);
            JobList.set(key2 + ".Rank", rank);
            JobList.set(key2 + ".Wage", wage);
            JobList.set(key2 + ".Fired", false);
            JobList.set(key2 + ".FiredDate", "null");
            JobList.set(key2 + ".HoursWorked", 0.0);
            JobList.set(key2 + ".TotalHoursWorked", 0.0);
            JobList.save();
            JobList.reload();
       }
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
        employee.MaximumHours = JobList.getDouble(key + ".MaxHrs");
        employee.Rank = JobList.getInt(key + ".Rank");
        employee.Wage = JobList.getDouble(key + ".Wage");

        return employee;
    }
    public ArrayList<String> getAllJobOffers(Player player) {
        ArrayList<String> businesses = new ArrayList<>();
        JobList.reload();

        Set<String> Businesses =  JobList.getSections(player.getUniqueId().toString()).getKeys(false);
        for (String job : Businesses) {
            businesses.add(job);
            }
        return businesses;
    }
    public void HireEmployee(String businessName, Player player, String title, double MaxHours, int rank, double wage){
        String key = businessName + ".employees." + player.getName().toLowerCase();
        if (!BusinessData.exists(key, true)){
            BusinessData.set(key + ".PlayerUUID", player.getUniqueId().toString());
            BusinessData.set(key + ".StartDate", getDate());
            BusinessData.set(key + ".Title", title);
            BusinessData.set(key + ".PlayerName", player.getName());
            BusinessData.set(key + ".MaxHrs", MaxHours);
            BusinessData.set(key + ".Rank", rank);
            BusinessData.set(key + ".Wage", wage);
            BusinessData.set(key + ".Fired", false);
            BusinessData.set(key + ".FiredDate", "null");
            BusinessData.set(key + ".HoursWorked", 0.0);
            BusinessData.set(key + ".TotalHoursWorked", 0.0);
            BusinessData.save();

            JobList.remove(player.getUniqueId().toString() + "." + businessName);
        }
    }
    public void HireOwner(String businessName, Player player, String title, double MaxHours, int rank, double wage){
        //If business data does not exist
        String key = businessName + ".employees." + player.getName().toLowerCase();
        if (!BusinessData.exists(key, true)){
            BusinessData.set( key + ".PlayerUUID", player.getUniqueId().toString());
            BusinessData.set(key + ".StartDate", getDate());
            BusinessData.set(key + ".Title", title);
            BusinessData.set(key + ".PlayerName", player.getName());
            BusinessData.set(key + ".MaxHrs", MaxHours);
            BusinessData.set(key+ ".Rank", rank);
            BusinessData.set(key + ".Wage", wage);
            BusinessData.set(key + ".Fired", false);
            BusinessData.set(key + ".FiredDate", "null");
            BusinessData.set(key + ".HoursWorked", 0.0);
            BusinessData.set(key + ".TotalHoursWorked", 0.0);
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
            employee.MaximumHours = BusinessData.getDouble(key + ".MaxHrs");
            employee.Rank = BusinessData.getInt(key + ".Rank");
            employee.Wage = BusinessData.getDouble(key + ".Wage");
            employee.Fired = BusinessData.getBoolean(key + ".Fired");
            employee.FiredDate = BusinessData.getString(key + ".FiredDate");
            employee.HoursWorkedPerPayPeriod = BusinessData.getDouble(key + ".HoursWorked");
            employee.TotalWorkHours = BusinessData.getDouble(key + ".TotalHoursWorked");
            return employee;
        }
        return null;
    }
    public static void SaveNewEmployeeData(Employee employee) {
        String key = employee.EmployerName + ".employees." + employee.PlayerName;
        plugin.getLogger().info(key);
        if (BusinessData.exists(key, true)){
            BusinessData.reload();
            BusinessData.set(key + ".Title", employee.Title);
            BusinessData.set(key + ".MaxHrs", employee.MaximumHours);
            BusinessData.set(key+ ".Rank", employee.Rank);
            BusinessData.set(key + ".Wage", employee.Wage);
            BusinessData.set(key + ".Fired", employee.Fired);
            BusinessData.set(key + ".FiredDate", employee.FiredDate);
            BusinessData.set(key + ".HoursWorked",employee.HoursWorkedPerPayPeriod);
            BusinessData.set(key + ".TotalHoursWorked", employee.TotalWorkHours);
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

    public void PayEmployeeHourlyRate(Employee employee){
        double TimeWorked = employee.HoursWorkedPerPayPeriod;
        double Tax;
        Tax = plugin.getConfig().getDouble("TaxPercent");
        Double Income = TimeWorked * employee.Wage;
        Double TaxSubtraction = Income * Tax;

        LlamaEconomy.getAPI().addMoney(employee.playerUUID, Income - TaxSubtraction);

        Business Employer = LoadBusiness(employee.EmployerName);
        Employer.Balance -= Income - TaxSubtraction;
        Employer.SaveData();
    }

    public void RegisterOnClock(Player player,Employee employee) throws ParseException {
        String Key = employee.PlayerName.toLowerCase();
        if (!Clock.isSection(Key)) {
            Date Current = new SimpleDateFormat().parse(getTime());
            Clock.set(Key + ".ClockInTime", Current.toString());
            Clock.set(Key + ".MaxHours", employee.MaximumHours);
            Clock.set(Key + ".BusinessName", employee.EmployerName);
        }
        else {
            player.sendMessage("You Are Already Clocked In");
        }
    }
    public void RegisterOffClock(Employee employee) throws ParseException {
        String Key = employee.PlayerName.toLowerCase();
        if (Clock.isSection(Key)) {
            Date Current = new SimpleDateFormat().parse(getTime());
            Date ClockInTime = new SimpleDateFormat(" HH:mm").parse(Clock.getString(Key));
            long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(Current.getTime() - ClockInTime.getTime()));
            employee.HoursWorkedPerPayPeriod += Difference;
            employee.SaveData();
            Clock.remove(employee.PlayerName);
        }
    }

    public void windDownTimer() throws ParseException {
        for (String PlayerName: Clock.getKeys()) {
                String Key1 = PlayerName.toLowerCase() + ".ClockInTime";
                String Key2 = PlayerName.toLowerCase() + ".MaxHours";
                String Key3 = PlayerName.toLowerCase() + ".BusinessName";
                Date ClockInTime = new SimpleDateFormat(" HH:mm").parse(Clock.getString(Key1));
                Date Current = new SimpleDateFormat().parse(getTime());
                double MaxTime = Clock.getDouble(Key2);
                long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(Current.getTime() - ClockInTime.getTime()));
                if (Difference >= MaxTime) {
                    RegisterOffClock(LoadEmployee(LoadBusiness(Clock.getString(Key3)), PlayerName));
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
        Date now = new Date();
        return now;
    }

    public static String getTime(){
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(now);
    }

    public static boolean NameNotTook(String Name){
       if (!BusinessData.exists(Name.toLowerCase(),true)) {
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
