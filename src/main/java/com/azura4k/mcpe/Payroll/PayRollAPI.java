package com.azura4k.mcpe.Payroll;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import net.lldv.llamaeconomy.LlamaEconomy;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayRollAPI{

    private static PayrollBase plugin;

    private static Config BusinessData;
    private static Config UIDList;
    private static Config Transactions;
    private static Config EmployeeRegistry;
    public static Config PluginConfig;


    public static void PayRollAPI(PayrollBase instance) {
        instance.saveResource("/data/business.yml");
        BusinessData = new Config(instance.getDataFolder() + "/data/business.yml", Config.YAML);
        instance.getLogger().info("Business Data YAML Ready.");

        instance.saveResource("/data/UIDList.yml");
        UIDList = new Config(instance.getDataFolder() + "/data/UIDList.yml", Config.YAML);
        instance.getLogger().info("UIDList Data YAML Ready.");

        instance.saveResource("/data/Transactions.yml");
        Transactions = new Config(instance.getDataFolder() + "/data/transactions.yml", Config.YAML);
        instance.getLogger().info("Transactions Data YAML Ready.");

        //Used to speed the process of obtaining business's up.
        instance.saveResource("/data/EmployeeRegistry.yml");
        EmployeeRegistry = new Config(instance.getDataFolder() + "/data/EmployeeRegistry.yml", Config.YAML);
        instance.getLogger().info("Employee Registry Data YAML Ready.");

        PluginConfig = instance.getConfig();

        plugin = instance;
    }



    public boolean CreateBusiness(String businessName, String businessDesc, Player owner) {
        String uid = UIDGenerator();
        Employee employee = new Employee();

        if (NameNotTook(businessName)) {
            UIDList.set(businessName.toLowerCase(), uid);
            BusinessData.set(uid + ".name", businessName);
            BusinessData.set(uid + ".desc", businessDesc);
            BusinessData.set(uid + ".ownerName", owner.getName());
            BusinessData.set(uid + ".creationdate", getDate());
            BusinessData.set(uid + ".balance", (double) 0);
            BusinessData.set(uid + ".trustedrank", 4);
            BusinessData.set(uid + ".maxrank", 5);
            BusinessData.set(uid + ".minrank", 1);
            BusinessData.save();
            UIDList.save();

            //Load Owner as Employee
            HireEmployee(uid, owner, "Founder", 20, 5, 10);

            return true;
        }
        return false;
    }
    public boolean DeleteBusiness(Business business, Player commandsender) {
        try {
            if (commandsender == business.Owner || commandsender.isOp()) {
                BusinessData.remove(business.UID);
                UIDList.remove(business.BusinessName.toLowerCase());
                BusinessData.save();
                UIDList.save();
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
           return false;
        }
    }
    public static void SaveNewBusinessData(Business business) {
        BusinessData.set(business.UID  + ".desc", business.BusinessDesc);
        BusinessData.set(business.UID  + ".ownerName", business.Owner.getName());
        BusinessData.set(business.UID  + ".balance", business.Balance);
        BusinessData.set(business.UID  + ".trustedrank", business.TrustedRank);
        BusinessData.set(business.UID  + ".maxrank", business.MaxRank);
        BusinessData.set(business.UID  + ".minrank", business.MinRank);
        BusinessData.save();
    }
    public Business LoadBusiness(String businessName){
        Business business = new Business();
        if (UIDList.exists(businessName.toLowerCase())){
            business.UID = UIDList.get(businessName.toLowerCase()).toString();
            business.BusinessName = BusinessData.get(business.UID + ".name").toString();
            business.BusinessDesc = BusinessData.get(business.UID + ".desc").toString();
            business.CreationDate = BusinessData.get(business.UID + ".creationdate").toString();
            business.Owner = plugin.getServer().getPlayerExact(BusinessData.get(business.UID + ".ownerName").toString());
            business.Balance = (double) BusinessData.get(business.UID + ".balance");
            business.TrustedRank = (int) BusinessData.get(business.UID + ".trustedrank");
            business.MaxRank = (int) BusinessData.get(business.UID + ".maxrank");
            business.MinRank = (int) BusinessData.get(business.UID + ".minrank");
            return business;
        }
        else {
            return business = null;
        }
    }
    public Business LoadBusinessFromID(String EmployerID){
        Business business = new Business();
            business.UID = EmployerID;
            business.BusinessName = BusinessData.get(business.UID + ".name").toString();
            business.BusinessDesc = BusinessData.get(business.UID + ".desc").toString();
            business.CreationDate = BusinessData.get(business.UID + ".creationdate").toString();
            business.Owner = plugin.getServer().getPlayerExact(BusinessData.get(business.UID + ".ownerName").toString());
            business.Balance = (double) BusinessData.get(business.UID + ".balance");
            business.TrustedRank = (int) BusinessData.get(business.UID + ".trustedrank");
            business.MaxRank = (int) BusinessData.get(business.UID + ".maxrank");
            business.MinRank = (int) BusinessData.get(business.UID + ".minrank");
            return business;
    }

    public boolean HireEmployee(String employerID, Player player, String title, double MaxHours, int rank, double wage){
        //If business data does not exist
        String key = employerID + ".employees." + player.getName().toLowerCase();
        String key2 = player.getUniqueId().toString() + "." + employerID;
        if (!BusinessData.exists(key, true)){
            BusinessData.set( key + ".PlayerUid", player.getUniqueId().toString());
            BusinessData.set(key + ".StartDate", getDate());
            BusinessData.set(key + ".Title", title);
            BusinessData.set(key + ".PlayerName", player.getName());
            BusinessData.set(key + ".MaxHrs", MaxHours);
            BusinessData.set(key+ ".Rank", rank);
            BusinessData.set(key + ".Wage", wage);
            BusinessData.set(key + ".Fired", false);
            BusinessData.set(key + ".FiredDate", "null");
            BusinessData.save();

            EmployeeRegistry.set(key2 + ".HoursWorked", 0.0);
            EmployeeRegistry.save();
            return true;
        }
        return false;
    }
    public Employee LoadEmployee(Business business, String EmployeeName){
        String key = business.UID + ".employees." + EmployeeName.toLowerCase();
        Employee employee = new Employee();
        //Check for Employment
        if (BusinessData.exists(key, true)){
            employee.EmployerUID = business.UID;
            employee.playerUUID = UUID.fromString(BusinessData.getString(key+ ".PlayerUid"));
            employee.PlayerName = BusinessData.getString(key+ ".PlayerName");
            employee.StartDate = BusinessData.getString(key +".StartDate");
            employee.Title = BusinessData.getString(key + ".Title");
            employee.MaximumHours = BusinessData.getDouble(key + ".MaxHrs");
            employee.Rank = BusinessData.getInt(key + ".Rank");
            employee.Wage = BusinessData.getDouble(key + ".Wage");
            employee.Fired = BusinessData.getBoolean(key + ".Fired");
            employee.FiredDate = BusinessData.getString(key + ".FiredDate");
            return employee;
        }
        return employee = null;
    }
    public boolean FireEmployee(Employee employee){
        String key = employee.EmployerUID + ".employees." + employee.PlayerName;
        String key2 = employee.playerUUID.toString() + "." + employee.EmployerUID;
        if (BusinessData.exists(key, true)){
            BusinessData.reload();
            BusinessData.set(key + ".Fired", true);
            BusinessData.set(key + ".FiredDate", getDate());
            BusinessData.save();
            BusinessData.reload();

            EmployeeRegistry.reload();
            EmployeeRegistry.remove(key2);
            PayEmployeeHourlyRate(employee);
            EmployeeRegistry.save();
            EmployeeRegistry.reload();

            return true;
        }
        return false;
    }
    public ArrayList<String> getEmployedBusinesses(Player player){
        ArrayList<String> EmployerIDs = new ArrayList<>();

        //Had to get the Set loaded into an Array, due iteration purposes.
        for (Iterator<String> Key = EmployeeRegistry.getSection(player.getUniqueId().toString()).getKeys(false).iterator(); Key.hasNext();){
            EmployerIDs.add(Key.next());
        }
        return EmployerIDs;
    }

    public void PayEmployeeHourlyRate(Employee employee){
        String key = employee.playerUUID.toString() + "." + employee.EmployerUID;
        Double TimeWorked = EmployeeRegistry.getDouble(key + ".HoursWorked");
        Double Tax;
        Tax = 0.0;
        Double Income = TimeWorked * employee.Wage;
        Double TaxSubtraction = Income * Tax;

        LlamaEconomy.getAPI().addMoney(employee.playerUUID, Income - TaxSubtraction);

        Business Employer = LoadBusinessFromID(employee.EmployerUID);
        Employer.Balance -= Income - TaxSubtraction;
        Employer.SaveData();
    }
    
    //Utilities
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
       if (!UIDList.exists(Name.toLowerCase(),true)) {
           if (Name.length() > 0){
               return true;
           }
           else{return false;}
       }
       else {
           return false;
       }
    }

    public static boolean RenameBusiness(String OldName, String NewName){
        if(NameNotTook(NewName)){
            String UID = UIDList.get(OldName).toString().toLowerCase();
            BusinessData.set(UID + ".name", NewName);
            UIDList.set(NewName, UID);
            UIDList.remove(OldName);
            return true;
        }
        else {
            return false;
        }
    }
}
