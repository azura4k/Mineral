package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Models.Business;
import com.azura4k.mcpe.Payroll.Models.Employee;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

import java.util.Objects;

public class BusinessInfo {

    public void initialize(Player player, Business business){
        CustomForm Form = new CustomForm(PayRollAPI.getLanguage("ManageBusinessInfoFormTitle"));
        PayRollAPI api = new PayRollAPI();
        Employee employee = api.LoadEmployee(business, player.getName());


        //Checks if Player is Owner
        if (Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner") + business.BusinessName);
            Form.addElement("BusinessDesc", Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc")).setPlaceholder(business.BusinessDesc).build());
            Form.addElement("MaxRank", Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank")).setPlaceholder(String.valueOf(business.MaxRank)).build());
            Form.addElement("MinRank", Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormMin")).setPlaceholder(String.valueOf(business.MinRank)).build());
            Form.addElement("TrustedRank", Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormTrustedRank")).setPlaceholder(String.valueOf(business.TrustedRank)).build());
            Form.addElement(PayRollAPI.getLanguage(PayRollAPI.getLanguage("ManageBusinessInfoFormBalance") + business.Balance));
        }
        else if (employee.Rank >= business.TrustedRank && !Objects.equals(employee.playerUUID.toString(), business.Owner.getUniqueId().toString())){
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner") + business.BusinessName);
            Form.addElement("BusinessDesc",Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc")).setPlaceholder(business.BusinessDesc).build());
            Form.addElement("MaxRank",Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank")).setPlaceholder(String.valueOf(business.MaxRank)).build());
            Form.addElement("MinRank",Input.builder().setName(PayRollAPI.getLanguage("ManageBusinessInfoFormMin")).setPlaceholder(String.valueOf(business.MinRank)).build());
            Form.addElement(PayRollAPI.getLanguage(PayRollAPI.getLanguage("ManageBusinessInfoFormBalance") + business.Balance));
        }
        else if (employee.Rank < business.TrustedRank) {
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormNameBelowOwner") + business.BusinessName);
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc") + business.BusinessDesc);
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormMaxRank") + business.MaxRank);
            Form.addElement(PayRollAPI.getLanguage("ManageBusinessInfoFormDesc") + business.MinRank);
        }

        //Changes Handler.
        Form.setHandler((p, response) -> {
            //Check for null
            if (!(response.getInput("BusinessDesc").getValue() == null) && response.getInput("BusinessDesc").getValue().length() > 0){
                business.BusinessDesc = response.getInput("BusinessDesc").getValue();
            }
            if (!(response.getInput("MaxRank").getValue() == null) && response.getInput("MaxRank").getValue().length() > 0){
                business.MaxRank = Integer.parseInt(response.getInput("MaxRank").getValue());
            }
            if (!(response.getInput("MinRank").getValue() == null) && response.getInput("MinRank").getValue().length() > 0 ){
                business.MinRank = Integer.parseInt(response.getInput("MinRank").getValue());
            }
            if (!(response.getInput("TrustedRank").getValue() == null) && response.getInput("TrustedRank").getValue().length() > 0){
                business.TrustedRank = Integer.parseInt(response.getInput("TrustedRank").getValue());
            }
            business.SaveData();
            p.getPlayer().sendMessage(PayRollAPI.getLanguage("SuccessfullyUpdated"));

            Options options = new Options();
            options.initialize(player, business);
        });


        Form.send(player);
    }
}


