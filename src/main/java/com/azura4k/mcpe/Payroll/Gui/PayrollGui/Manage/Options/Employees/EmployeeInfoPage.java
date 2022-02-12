package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Employees;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;

import java.util.Objects;

public class EmployeeInfoPage {

    public void initialize(Player player, Business business, Employee employee){
        CustomForm Form = new CustomForm(PayRollAPI.getLanguage("EmployeeProfileFormTitle"));

        //Checks if Player is Owner
        if (Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormTotalHoursWorked") + employee.TotalWorkHours));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormHoursWorked") + employee.HoursWorkedPerPayPeriod));
            Form.addElement("Title", Input.builder().setName(PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle")).setPlaceholder(employee.Title).build());
            Form.addElement("Rank", Input.builder().setName(PayRollAPI.getLanguage("EmployeeProfileFormRank")).setPlaceholder(String.valueOf(employee.Rank)).build());
            Form.addElement("MaxHours", Input.builder().setName(PayRollAPI.getLanguage("EmployeeProfileFormMaxHours")).setPlaceholder(String.valueOf(employee.MaximumHours)).build());
            Form.addElement("Wage", Input.builder().setName(PayRollAPI.getLanguage("EmployeeProfileFormWage")).setPlaceholder(String.valueOf(employee.Wage)).build());
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate));
        }
        else if (employee.Rank >= business.TrustedRank && !Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormTotalHoursWorked") + employee.TotalWorkHours));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormHoursWorked") + employee.HoursWorkedPerPayPeriod));
            Form.addElement("Title", Input.builder().setName( PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle")).setPlaceholder(employee.Title).build());
            Form.addElement("Rank", Input.builder().setName( PayRollAPI.getLanguage("EmployeeProfileFormRank")).setPlaceholder(String.valueOf(employee.Rank)).build());
            Form.addElement("MaxHours", Input.builder().setName( PayRollAPI.getLanguage("EmployeeProfileFormMaxHours")).setPlaceholder(String.valueOf(employee.MaximumHours)).build());
            Form.addElement("Wage", Input.builder().setName( PayRollAPI.getLanguage("EmployeeProfileFormWage")).setPlaceholder(String.valueOf(employee.Wage)).build());
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired));
            Form.addElement( new Label(PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate));
        }
        else if (employee.Rank < business.TrustedRank) {
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormName") + employee.PlayerName);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormEmpTitle") + employee.Title);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormRank") + employee.Rank);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormStartDate") + employee.StartDate);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormTotalHoursWorked") + employee.TotalWorkHours);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormIsFired") + employee.Fired);
            Form.addElement( PayRollAPI.getLanguage("EmployeeProfileFormDateFired") + employee.FiredDate);
        }

        //Changes Handler.
        Form.setHandler((p, response) -> {
            //Check for null
            if (!(response.getInput("Title").getValue() == null) && response.getInput("Title").getValue().length() > 0){
                employee.Title = response.getInput("Title").getValue();
            }
            if (!(response.getInput("Rank").getValue() == null) && response.getInput("Rank").getValue().length() > 0){
                int Rank = Integer.parseInt(response.getInput("Rank").getValue());
                if (!(Rank > business.MaxRank || Rank < business.MinRank)){
                    employee.Rank = Integer.parseInt(response.getInput("Rank").getValue());
                }
            }
            if (!(response.getInput("MaxHours").getValue() == null) && response.getInput("MaxHours").getValue().length() > 0 ){
                employee.MaximumHours = Double.parseDouble(response.getInput("MaxHours").getValue());
            }
            if (!(response.getInput("Wage").getValue() == null) && response.getInput("Wage").getValue().length() > 0){
                employee.Wage = Double.parseDouble(response.getInput("Wage").getValue());
            }
            employee.SaveData();
            p.getPlayer().sendMessage(PayRollAPI.getLanguage("SuccessfullyUpdated"));
            EmployeeSelection form = new EmployeeSelection();
            form.Initialize(player, business);
        });


        Form.send(player);
    }
}


