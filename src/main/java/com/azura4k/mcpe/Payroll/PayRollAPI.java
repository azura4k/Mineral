package com.azura4k.mcpe.Payroll;


import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PayRollAPI{

    private static Economy econ = null;
    public static PayrollBase plugin;
    public static FileConfiguration PluginConfig;
    static Connection Conn = null;
    public static Economy getEconomy() {
        return econ;
    }
    public static void Initialize(PayrollBase instance) {

        PluginConfig = instance.getConfig();
        plugin = instance;

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();

        File dataFile = new File(Objects.requireNonNull(plugin.getServer().getPluginManager().getPlugin("Payroll")).getDataFolder().getPath() + "// Corp.db");
        try {
            if (dataFile.createNewFile()){
                plugin.getLogger().warning("New File Created");
            } else {
                plugin.getLogger().warning("Using Existing Data");
            }
        } catch (IOException e) {
            plugin.getLogger().warning( e.getMessage());
        }
        try {
            Class.forName("org.sqlite.JDBC");
            Conn = DriverManager.getConnection("jdbc:sqlite:" + dataFile.getAbsolutePath());
            plugin.getLogger().info("Opened database successfully");

        } catch ( Exception e ) {
            plugin.getLogger().warning( e.getClass().getName() + ": " + e.getMessage() );
        }

        //Create Tables
        String CompanyTableSQL =
                "CREATE TABLE IF NOT EXISTS  \"Companies\" (\n" +
                        "\t\"CompanyName\"\tTEXT NOT NULL UNIQUE,\n" +
                        "\t\"CompanyDesc\"\tTEXT,\n" +
                        "\t\"OwnerUUID\"\tTEXT NOT NULL,\n" +
                        "\t\"CreationDate\"\tTEXT NOT NULL,\n" +
                        "\t\"Balance\"\tNUMERIC NOT NULL,\n" +
                        "\t\"TrustedRank\"\tINTEGER NOT NULL,\n" +
                        "\t\"MaxRank\"\tINTEGER NOT NULL,\n" +
                        "\t\"MinRank\"\tINTEGER NOT NULL,\n" +
                        "\tPRIMARY KEY(\"Name\")\n" +
                        ");";


        String EmployeesDableSQL =
                "CREATE TABLE IF NOT EXISTS  \"Employment\" (\n" +
                        "\t\"PlayerUUID\"\tTEXT NOT NULL,\n" +
                        "\t\"CompanyName\"\tINTEGER NOT NULL,\n" +
                        "\t\"Title\"\tTEXT,\n" +
                        "\t\"Wage\"\tREAL NOT NULL,\n" +
                        "\t\"MinutesWorked\"\tREAL NOT NULL,\n" +
                        "\t\"TotalTime\"\tNUMERIC NOT NULL,\n" +
                        "\t\"MaxMinutes\"\tNUMERIC NOT NULL,\n" +
                        "\t\"Rank\"\tINTEGER NOT NULL,\n" +
                        "\t\"Fired\"\tINTEGER NOT NULL,\n" +
                        "\t\"FiredDate\"\tTEXT NOT NULL,\n" +
                        "\t\"StartDate\"\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(\"CompanyUUID\",\"PlayerUUID\",\"CompanyName\")\n" +
                        ");";

        String JobOffersTableSQL =
                "CREATE TABLE IF NOT EXISTS  \"JobOffers\" (\n" +
                        "\t\"CompanyName\"\tTEXT NOT NULL,\n" +
                        "\t\"PlayerUUID\"\tTEXT NOT NULL,\n" +
                        "\t\"OfferedWage\"\tINTEGER NOT NULL,\n" +
                        "\t\"OfferedTitle\"\tTEXT NOT NULL,\n" +
                        "\t\"MaxMinutes\"\tNumeric NOT NULL,\n" +
                        //Good for later (Job Desc)
                        "\t\"JobDesc\"\tTEXT,\n" +
                        "\t\"OfferedRank\"\tNumeric NOT NULL,\n" +
                        "\tPRIMARY KEY(\"PlayerUUID\",\"CompanyName\")\n" +
                        ");";

        String ClockTableSQL =
                "CREATE TABLE IF NOT EXISTS  \"Clock\" (\n" +
                        "\t\"ClockInTime\"\tNUMERIC NOT NULL,\n" +
                        "\t\"PlayerUUID\"\tTEXT NOT NULL UNIQUE,\n" +
                        "\t\"MaxMinutes\"\tNUMERIC NOT NULL,\n" +
                        "\t\"CompanyName\"\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(\"PlayerUUID\",\"CompanyName\",\"CompanyUUID\")\n" +
                        ");";

        try {
            Conn.createStatement().execute(CompanyTableSQL);
            Conn.createStatement().execute(EmployeesDableSQL);
            Conn.createStatement().execute(JobOffersTableSQL);
            Conn.createStatement().execute(ClockTableSQL);
            Conn.commit();
        }
        catch (Exception e){
            plugin.getLogger().info("Just something under the hood");
        }


    }


    public boolean CreateBusiness(String businessName, String businessDesc, Player owner) {

        String sql = "INSERT INTO Companies VALUES(?,?,?,?,?,?)";

        try{
            PreparedStatement pstmt = Conn.prepareStatement(sql);
            pstmt.setString(1, businessName);
            pstmt.setString(2, businessDesc);
            pstmt.setString(3, owner.getUniqueId().toString());
            pstmt.setString(4, getDate().toString());
            pstmt.setDouble(5,0.0);
            pstmt.setInt(6, 4);
            pstmt.setInt(7, 5);
            pstmt.setInt(8, 1);
            pstmt.executeUpdate();
            if (HireOwner(businessName, owner, "Founder", 20, 5, PluginConfig.getDouble("MinimumWage"))){
                return true;
            }
        }catch (SQLException e){
            plugin.getLogger().warning("Company Already Exists");
            return false;
        }
        return false;
    }
    public void DeleteBusiness(Business business, Player commandeer) {
        try {
            if (commandeer == business.Owner || commandeer.isOp()) {
                String sql = "DELETE FROM Companies WHERE CompanyName = ? AND OwnerUUID = ?";
                PreparedStatement statement = Conn.prepareStatement(sql);
                statement.setString(1, business.BusinessName);
                statement.setString(2, commandeer.getUniqueId().toString());
                statement.executeUpdate();
            }
        } catch (Exception e){
            plugin.getLogger().warning(e.getMessage());
        }
    }
    public static void SaveNewBusinessData(Business business) {
        try {
            String sql = "UPDATE Companies SET CompanyDesc = ?, Balance = ?, TrustedRank = ?, MaxRank = ?, MinRank = ? WHERE CompanyName = ?";
            PreparedStatement statement = Conn.prepareStatement(sql);
            statement.setString(1, business.BusinessDesc);
            statement.setDouble(2, business.Balance);
            statement.setInt(3, business.TrustedRank);
            statement.setInt(4, business.MaxRank);
            statement.setInt(5, business.MinRank);
            statement.setString(6, business.BusinessName);
            statement.executeUpdate();
        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }


    }
    public Business LoadBusiness(String businessName){
        Business business = new Business();

        try {
            String sql = "SELECT * FROM Companies WHERE CompanyName = ?";
            PreparedStatement stmt = Conn.prepareStatement(sql);
            stmt.setString(1, businessName);

            //Get Results
            ResultSet results = stmt.executeQuery();


            //Assign Results to variables
            business.BusinessName = results.getString("CompanyName");
            business.BusinessDesc = results.getString("CompanyDesc");
            business.Balance = results.getDouble("Balance");
            business.MinRank = results.getInt("MinRank");
            business.MaxRank = results.getInt("MaxRank");
            business.Owner = plugin.getServer().getPlayer(UUID.fromString(results.getString("OwnerUUID")));
            business.TrustedRank = results.getInt("TrustedRank");
            business.CreationDate = results.getString("CreationDate");

            business.Employees = getAllEmployeesEmployedHere(businessName);


            return business;
        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
            return null;
        }

    }
    protected ArrayList<Employee> getAllEmployeesEmployedHere(String businessName) {
        try {
            ArrayList<Employee> Employees = new ArrayList<>();
            String sql = "SELECT * FROM Employment WHERE CompanyName = ? AND ";
            PreparedStatement stmt = Conn.prepareStatement(sql);
            stmt.setString(1, businessName);

            //Get Results
            ResultSet results = stmt.executeQuery();
            while(results.next()) {
                Employee employee = new Employee();
                employee.EmployerName = businessName;
                employee.playerUUID = UUID.fromString(results.getString("PlayerUUID"));
                employee.Rank = results.getInt("Rank");
                employee.PlayerName = plugin.getServer().getPlayer(employee.playerUUID).getName();
                employee.Fired = results.getBoolean("Fired");
                employee.FiredDate = results.getString("FiredDate");
                employee.MinutesWorkedPerPayPeriod = results.getDouble("MinutesWorked");
                employee.MaximumMinutes = results.getDouble("MaxMinutes");
                employee.TotalWorkMinutes = results.getDouble("TotalTime");
                employee.Wage = results.getDouble("Wage");
                employee.StartDate = results.getString("StartDate");
                Employees.add(employee);
            }
            return Employees;
        }
        catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
            return null;
        }
    }
    public void TransferOwner(Player player, Business business, String newOwnerName) {
        try {
            Player newOwner = plugin.getServer().getPlayerExact(newOwnerName);
            PreparedStatement stmt = Conn.prepareStatement("UPDATE Companies SET OwnerUUID = ? WHERE OwnerUUID = ? AND CompanyName = ?");
            stmt.setString(1, newOwner.getUniqueId().toString());
            stmt.setString(2, player.getUniqueId().toString());
            stmt.setString(3, business.BusinessName);

            HireOwner(business.BusinessName, newOwner, "New Owner", 20, business.MaxRank, 20);
            Employee OldOwner = LoadEmployee(business, player.getName());
            OldOwner.Rank = business.MaxRank - 1;
            OldOwner.SaveData();
        }
        catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }

    }


    public void OfferPosition(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        try {
            PreparedStatement stmt = Conn.prepareStatement("INSERT INTO JobOffers VALUES (?,?,?,?,?,?,?)");
            stmt.setString(1,businessName);
            stmt.setString(2,player.getUniqueId().toString());
            stmt.setDouble(3, wage);
            stmt.setString(4, title);
            stmt.setDouble(5, MaxMinutes);
            stmt.setString(6, "");
            stmt.setInt(7, rank);
            stmt.executeUpdate();
        } catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
    }
    public void AcceptPosition(Player player, String businessName){
        Employee employee = LoadPositionDetails(player, businessName);
        HireEmployee(businessName, player, employee.Title, employee.MaximumMinutes, employee.Rank, employee.Wage);
        DeleteJobOffer(player, businessName);
    }
    public void DeleteJobOffer(Player player, String businessName){
        try {
            PreparedStatement stmt = Conn.prepareStatement("DELETE FROM JobOffers WHERE PlayerUUID = ? AND CompanyName = ? ");
            stmt.setString(1, player.getUniqueId().toString());
            stmt.setString(2, businessName);
        }catch(SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
    }
    public Employee LoadPositionDetails(Player player, String businessName){
        Employee employee = new Employee();
        try {
            PreparedStatement stmt = Conn.prepareStatement("SELECT * FROM JobOffers WHERE PlayerUUID = ? AND CompanyName = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet results = stmt.executeQuery();

            employee.EmployerName = businessName;
            employee.Wage = results.getDouble("OfferedWage");
            employee.playerUUID = UUID.fromString(results.getString("PlayerUUID"));
            employee.Title = results.getString("OfferedTitle");
            employee.MaximumMinutes = results.getDouble("MaxMinutes");
            employee.Rank = results.getInt("OfferedRank");
            return employee;
        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
        return null;
    }
    public ArrayList<String> getAllJobOffers(Player player) {

        ArrayList<String> Businesses = new ArrayList<>();
        try {
            PreparedStatement stmt = Conn.prepareStatement("SELECT CompanyName FROM JobOffers WHERE PlayerUUID = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet results = stmt.executeQuery();
            while (results.next()){
                Businesses.add(results.getString("CompanyName"));
            }
            return new ArrayList<>(Businesses);
        }
        catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
        return null;
    }


    public void HireEmployee(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        try{
            //Check for current employment at company
            PreparedStatement checkFor = Conn.prepareStatement("SELECT CompanyName, PlayerUUID FROM Employment WHERE PlayerUUID = ? AND CompanyName = ?;");
            checkFor.setString(1, player.getUniqueId().toString());
            checkFor.setString(2, businessName);

            if (!checkFor.executeQuery().isBeforeFirst()){
                String sql = "INSERT INTO Employment VALUES (?,?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement pstmt = Conn.prepareStatement(sql);
                pstmt.setString(1, player.getUniqueId().toString().toLowerCase());
                pstmt.setString(2, businessName);
                pstmt.setString(3, title);
                pstmt.setDouble(4, wage);
                pstmt.setDouble(5, 0);
                pstmt.setDouble(6, 0);
                pstmt.setDouble(7, MaxMinutes);
                pstmt.setDouble(8, rank);
                pstmt.setInt(9, 0);
                pstmt.setString(10, "");
                pstmt.setString(11, getDate().toString());
                pstmt.executeUpdate();
                //TODO REMOVE JOB FROM JOB LISTING
            }
        }catch (SQLException e) {
            player.sendMessage(e.getMessage());
        }
    }
    public boolean HireOwner(String businessName, Player player, String title, double MaxMinutes, int rank, double wage){
        try{
                String sql = "INSERT INTO Employment VALUES (?,?,?,?,?,?,?,?,?,?);";
                PreparedStatement pstmt = Conn.prepareStatement(sql);
                pstmt.setString(1, player.getUniqueId().toString().toLowerCase());
                pstmt.setString(2, businessName);
                pstmt.setString(3, title);
                pstmt.setDouble(4, wage);
                pstmt.setDouble(5, 0);
                pstmt.setDouble(6, 0);
                pstmt.setDouble(7, MaxMinutes);
                pstmt.setDouble(8, rank);
                pstmt.setInt(9, 0);
                pstmt.setString(10, "");
                pstmt.executeUpdate();
                return true;
        }catch (SQLException e) {
            player.sendMessage(e.getMessage());
            return false;
        }

    }
    public void FireEmployee(Employee employee){
        String sql = "DELETE FROM Employment WHERE CompanyName = ? AND PlayerUUID = ?;";
        try {
            PreparedStatement pstmt = Conn.prepareStatement(sql);
            pstmt.setString(1,employee.EmployerName);
            pstmt.setString(2,employee.playerUUID.toString());
        }
        catch (SQLException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }


    public Employee LoadEmployee(Business business, String PlayerName){
        try {
            String sql = "SELECT * FROM Employment WHERE CompanyName = ? AND PlayerUUID = ?";
            PreparedStatement stmt = Conn.prepareStatement(sql);
            stmt.setString(1, business.BusinessName);
            stmt.setString(2, plugin.getServer().getPlayer(PlayerName).getUniqueId().toString());
            ResultSet results = stmt.executeQuery();

            Employee employee = new Employee();
            employee.EmployerName = business.BusinessName;
            employee.playerUUID = UUID.fromString(results.getString("PlayerUUID"));
            employee.Rank = results.getInt("Rank");
            employee.PlayerName = plugin.getServer().getPlayer(employee.playerUUID).getName();
            employee.Fired = results.getBoolean("Fired");
            employee.FiredDate = results.getString("FiredDate");
            employee.MinutesWorkedPerPayPeriod = results.getDouble("MinutesWorked");
            employee.MaximumMinutes = results.getDouble("MaxMinutes");
            employee.TotalWorkMinutes = results.getDouble("TotalTime");
            employee.Wage = results.getDouble("Wage");
            employee.StartDate = results.getString("StartDate");
            return employee;

        } catch (SQLException e) {
            plugin.getLogger().warning(e.getStackTrace().toString());
        }



        return null;
    }
    public static void SaveNewEmployeeData(Employee employee) {
        try {
            String sql = "UPDATE Employment SET Title = ?, Wage = ?, MinutesWorked = ?, TotalTime = ?, MaxMinutes = ?, Rank = ?, Fired = ?, FiredDate = ? WHERE PlayerUUID = ?";
            PreparedStatement statement = Conn.prepareStatement(sql);
            statement.setString(1, employee.Title);
            statement.setDouble(2, employee.Wage);
            statement.setDouble(3, employee.MinutesWorkedPerPayPeriod );
            statement.setDouble(4, employee.TotalWorkMinutes);
            statement.setDouble(5, employee.MaximumMinutes);
            statement.setInt(6, employee.Rank);
            statement.setBoolean(7, employee.Fired);
            statement.setString(8, employee.FiredDate);
            statement.setString(9, employee.playerUUID.toString());
            statement.executeUpdate();
        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
    }
    public ArrayList<String> getBusinessesEmployedAt(Player player){

        ArrayList<String> EmployedBusinesses = new ArrayList<>();

        try {
            PreparedStatement stmt = Conn.prepareStatement("SELECT CompanyName FROM Employment WHERE PlayerUUID = ?");
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet results = stmt.executeQuery();
            while(results.next()){
                EmployedBusinesses.add(results.getString("CompanyName"));
            }
        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
        }
        return EmployedBusinesses;
    }
    public void PayEmployeeMinutelyRate(Employee employee){
        double TimeWorked = employee.MinutesWorkedPerPayPeriod;
        double Tax;
        Tax = plugin.getConfig().getDouble("TaxPercent");
        Double Income = TimeWorked * employee.Wage;
        Double TaxSubtraction = Income * Tax;
        getEconomy().depositPlayer(plugin.getServer().getOfflinePlayer(employee.playerUUID), Income - TaxSubtraction);
        plugin.getServer().getPlayerExact(employee.PlayerName).sendMessage(getLanguage("PayOutEmployee") + (Income - TaxSubtraction) + getLanguage("PayOutEmployeeBusiness") + employee.EmployerName);
        Business Employer = LoadBusiness(employee.EmployerName);
        Employer.Balance -= Income - TaxSubtraction;
        employee.MinutesWorkedPerPayPeriod = 0.0;
        Employer.SaveData();
        employee.SaveData();
    }
    public void RegisterOnClock(Player player, Employee employee) {
        try{
            PreparedStatement statement = Conn.prepareStatement("INSERT INTO Clock VALUES (?,?,?,?)");
            statement.setLong(1, getTime());
            statement.setString(2, employee.playerUUID.toString());
            statement.setDouble(3, employee.MaximumMinutes);
            statement.setString(4, employee.EmployerName);
            statement.executeUpdate();
        } catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
            player.sendMessage(getLanguage("CannotClockInOrInDebt"));
        }
    }
    public void RegisterOffClock(Employee employee) {

        try{
            PreparedStatement statement = Conn.prepareStatement("SELECT * FROM Clock WHERE PlayerUUID = ?");
            statement.setString(1, employee.playerUUID.toString());
            ResultSet rs = statement.executeQuery();

            //Get Data and calculate
            long ClockInTime = rs.getLong("ClockInTime");
            long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(getTime() - ClockInTime ));
            employee.MinutesWorkedPerPayPeriod += Difference;
            employee.TotalWorkMinutes += Difference;
            employee.SaveData();

            //Delete From Record
            PreparedStatement stmt = Conn.prepareStatement("DELETE FROM Clock WHERE PlayerUUID = ?");
            stmt.setString(1, employee.playerUUID.toString());
            stmt.executeUpdate();

        }
        catch (SQLException e){
            plugin.getServer().getPlayerExact(employee.PlayerName).sendMessage(getLanguage("NotClockedIn"));
        }
    }



    public void ForceClockOut(Player player) {
            try {
                PreparedStatement statement = Conn.prepareStatement("SELECT * FROM Clock WHERE PlayerUUID = ?");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet rs = statement.executeQuery();

                long ClockInTime = rs.getLong("ClockInTime");
                String BusinessName = rs.getString("CompanyName");

                long Difference = TimeUnit.MILLISECONDS.toMinutes(Math.abs(getTime() - ClockInTime ));
                Employee employee = LoadEmployee(LoadBusiness(BusinessName), player.getName());
                employee.MinutesWorkedPerPayPeriod += Difference;
                employee.TotalWorkMinutes += Difference;
                employee.SaveData();

                PreparedStatement stmt = Conn.prepareStatement("DELETE FROM Clock WHERE PlayerUUID = ?");
                stmt.setString(1, player.getUniqueId().toString());
                stmt.executeUpdate();

            }catch (SQLException e){
                plugin.getLogger().warning(e.getMessage());
            }
    }

    public void windDownTimer() {
        try {
            PreparedStatement statement = Conn.prepareStatement("SELECT * FROM Clock");
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                long ClockInTime = rs.getLong("ClockInTime");
                long MaxTime = TimeUnit.MINUTES.toMillis(rs.getLong("MaxMinutes"));
                long Difference = getTime() - ClockInTime;
                String CompanyName = rs.getString("CompanyName");
                Player player = plugin.getServer().getPlayer(UUID.fromString(rs.getString("PlayerUUID")));
                if (Difference > MaxTime) {
                    Business business = LoadBusiness(CompanyName);
                    RegisterOffClock(LoadEmployee(business, player.getName()));
                    player.sendMessage(getLanguage("ForceClockOut"));
                }
            }

        }catch (SQLException e){
            plugin.getLogger().warning(e.getMessage());
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
    public static String getLanguage(String LanugageTag){
        return PluginConfig.getString("Language." + LanugageTag);
    }
}
