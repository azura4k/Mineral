package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Objects;

public class EmployeeInfoPage {

    public void initialize(Player player, Business business, Employee employee){
        CustomForm.Builder Form = CustomForm.builder();

        PayRollAPI api = new PayRollAPI();
        Employee Manager = api.LoadEmployee(business, player.getName());
        //Checks if Player is Owner
        Form.title(PayRollAPI.getLanguage("EmployeeProfileFormTitle"));
        if (Objects.equals(Manager.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormTotalMinutesWorked") + employee.TotalWorkMinutes);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormMinutesWorked") + employee.MinutesWorkedPerPayPeriod);
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle"), employee.Title);
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormRank"),String.valueOf(employee.Rank));
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormMaxMinutes"), String.valueOf(employee.MaximumMinutes));
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormWage"),String.valueOf(employee.Wage));
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate);
        }
        else if (Manager.Rank >= business.TrustedRank && !Objects.equals(Manager.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormTotalMinutesWorked") + employee.TotalWorkMinutes);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormMinutesWorked") + employee.MinutesWorkedPerPayPeriod);
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle"), employee.Title);
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormRank"),String.valueOf(employee.Rank));
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormMaxMinutes"), String.valueOf(employee.MaximumMinutes));
            Form.input(PayRollAPI.getLanguage("EmployeeProfileFormWage"),String.valueOf(employee.Wage));
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired);
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate);
        }
        else if (Manager.Rank < business.TrustedRank) {
            Form.label(PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle") + employee.Title);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormRank") + employee.Rank);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormTotalMinutesWorked") + employee.TotalWorkMinutes);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired);
            Form.label( PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate);
        }

        Form.responseHandler((form, reponseData) -> {
            CustomFormResponse response = form.parseResponse(reponseData);
            if (!(response.getInput(1) == null) && response.getInput(1).length() > 0){
                employee.Title = response.getInput(1);
            }

            if (!(response.getInput(2) == null) && response.getInput(2).length() > 0){
                int Rank = Integer.parseInt(response.getInput(2));
                if (!(Rank > business.MaxRank || Rank < business.MinRank)){
                    employee.Rank = Integer.parseInt(response.getInput(2));
                }
            }

            if (!(response.getInput(3) == null) && response.getInput(3).length() > 0 ){
                employee.MaximumMinutes = Double.parseDouble(response.getInput(3));
            }
            if (!(response.getInput(4) == null) && response.getInput(4).length() > 0){
                employee.Wage = Double.parseDouble(response.getInput(4));
            }
            employee.SaveData();
            player.getPlayer().sendMessage(PayRollAPI.getLanguage("SuccessfullyUpdated"));
            EmployeeSelection customForm = new EmployeeSelection();
            customForm.Initialize(player, business);
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), Form);
    }
}


