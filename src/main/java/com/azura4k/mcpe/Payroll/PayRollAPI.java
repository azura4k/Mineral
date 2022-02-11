package com.azura4k.mcpe.Payroll;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayRollAPI{

    private static PayrollBase plugin;

    private static Config BusinessData;
    public static Config PluginConfig;


    public static void PayRollAPI(PayrollBase instance) {
        instance.saveResource("/data/business.yml");
        BusinessData = new Config(instance.getDataFolder() + "/data/business.yml", Config.YAML);
        instance.getLogger().info("Business Data YAML Ready.");

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
            HireEmployee(businessName, owner, "Founder", 20, 5, 10);

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


            Set<String> Employees = BusinessData.getSection(businessName + ".employees").getKeys(false);

            for (String EmployeeName: Employees) {
                String key = businessName + ".employees." + EmployeeName.toLowerCase();
                Employee employee = new Employee();
                employee.EmployerName = business.BusinessName;
                employee.playerUUID = UUID.fromString(BusinessData.getString(key + ".PlayerUUID"));
                employee.PlayerName = BusinessData.getString(key + ".PlayerName");
                employee.StartDate = BusinessData.getString(key +".StartDate");
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
            return business;
        }
        else {
            return null;
        }
    }


    public boolean HireEmployee(String businessName, Player player, String title, double MaxHours, int rank, double wage){
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
            return true;
        }
        return false;
    }
    public boolean FireEmployee(Employee employee){
        String key = employee.EmployerName + ".employees." + employee.PlayerName;
        if (BusinessData.exists(key, true)){
            BusinessData.reload();
            BusinessData.set(key + ".Fired", true);
            BusinessData.set(key + ".FiredDate", getDate());
            BusinessData.save();
            BusinessData.reload();
            return true;
        }
        return false;
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
        return employee = null;
    }
    public static void SaveNewEmployeeData(Employee employee) {
        String key = employee.EmployerName + ".employees." + employee.PlayerName;
        if (!BusinessData.exists(key, true)){
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
        Iterator EachBusiness = BusinessData.getKeys(false).iterator();
        while (EachBusiness.hasNext()){
            String BusinessName = EachBusiness.next().toString();
            Business business = LoadBusiness(BusinessName);
            for (int i = 0; i < business.Employees.size(); i++) {
                if (Objects.equals(business.Employees.get(i).playerUUID, player.getUniqueId())){
                    EmployedBusinesses.add(BusinessName);
                }
            }
        }
        return EmployedBusinesses;
    }




    public void PayEmployeeHourlyRate(Employee employee){
        Double TimeWorked = employee.HoursWorkedPerPayPeriod;
        Double Tax;
        Tax = plugin.getConfig().getDouble("TaxPercent");
        Double Income = TimeWorked * employee.Wage;
        Double TaxSubtraction = Income * Tax;

        LlamaEconomy.getAPI().addMoney(employee.playerUUID, Income - TaxSubtraction);

        Business Employer = LoadBusiness(employee.EmployerName);
        Employer.Balance -= Income - TaxSubtraction;
        Employer.SaveData();
    }
    
    //Utilities
    //Useful for Transactions Later
    public static String UIDGenerator(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String getDate() {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");
        return dateFormat.format(now);
    }

    public static boolean NameNotTook(String Name){
       if (!BusinessData.exists(Name.toLowerCase(),true)) {
           if (Name.length() > 0){
               return true;
           }
           else{return false;}
       }
       else {
           return false;
       }
    }

    public static String getLanguage(String LanugageTag){
        return PluginConfig.getString("Language." + LanugageTag);
    }
}
