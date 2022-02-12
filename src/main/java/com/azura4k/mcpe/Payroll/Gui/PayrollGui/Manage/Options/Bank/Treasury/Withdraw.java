package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Bank.Treasury;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

public class Withdraw {
    PayRollAPI api = new PayRollAPI();
    CustomForm form = new CustomForm(PayRollAPI.getLanguage("DepositFormTitle"));

    public void initialize(Player player, Business business, Employee employee){

        form.addElement(PayRollAPI.getLanguage("WithdrawFormCurrentBalance") + business.Balance);
        form.addElement("Amount" , Input.builder().setName(PayRollAPI.getLanguage("WithdrawFormEnterAmount")).build());
        form.setHandler((p, response) -> {
            Double Amount = Double.valueOf(response.getInput("Amount").getValue());

            if (business.Withdraw(employee, Amount)){
                p.sendMessage(PayRollAPI.getLanguage("WithdrawFormSuccesful") + Amount);
                Treasury form = new Treasury();
                form.initialize(player, business, employee);
            }
            else{
                p.sendMessage(PayRollAPI.getLanguage("BusinessOverdraftRisk"));
            }
        });
        form.send(player);
    }
}
