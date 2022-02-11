package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

public class Deposit {

    PayRollAPI api = new PayRollAPI();
    CustomForm form = new CustomForm(PayRollAPI.getLanguage("DepositFormTitle"));

    public void initialize(Player player, Business business){

        form.addElement(PayRollAPI.getLanguage("DepositFormCurrentBalance") + business.Balance);
        form.addElement("Amount" ,Input.builder().setName(PayRollAPI.getLanguage("DepositFormEnterAmount")).build());
        form.setHandler((p, response) -> {
            Double Amount = Double.valueOf(response.getInput("Amount").getValue());
            Employee employee = api.LoadEmployee(business, player.getName());
            if (business.Deposit(employee, Amount)){
                p.sendMessage(PayRollAPI.getLanguage("DepositFormSuccesful") + Amount);
            }
            else{
                p.sendMessage(PayRollAPI.getLanguage("OverDraftRisk"));
            }
        });
        form.send(player);
    }

}
