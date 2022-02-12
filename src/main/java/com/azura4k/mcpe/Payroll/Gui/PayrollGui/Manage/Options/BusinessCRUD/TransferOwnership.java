package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

import java.util.Objects;

public class TransferOwnership {
    CustomForm form = new CustomForm();
    PayRollAPI api = new PayRollAPI();
    public void Initalize(Player player, Business business){
        if (Objects.equals(player, business.Owner)){
            form.addElement("NewOwnerName", Input.builder().setName("New Owner").build());

            form.setHandler((p, response) -> {
                if (response.getInput("NewOwnerName").getValue() != null && response.getInput("NewOwnerName").getValue().length() > 0 ){
                    api.TransferOwner(player, business,response.getInput("NewOwnerName").getValue() );
                }
            });

            form.send(player);

        }
    }
}
