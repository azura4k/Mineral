package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.BusinessCRUD;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.Options.Options;
import com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage.SelectMenu;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import net.lldv.llamaeconomy.LlamaEconomy;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;

public class CreateBusiness {

    CustomForm form = new CustomForm(PayRollAPI.getLanguage("CreateBusinessFormTitle"));
    Double StartupCost = PayRollAPI.PluginConfig.getDouble("IntialStartupCost");
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player) {

        form.addElement("BusinessName", Input.builder().setName(PayRollAPI.getLanguage("CreateBusinessFormNameLabel")).build());
        form.addElement("BusinessDesc", Input.builder().setName(PayRollAPI.getLanguage("CreateBusinessFormDescriptionLabel")).build());
        form.addElement(PayRollAPI.getLanguage("CreateBusinessFormStartupCost") + StartupCost);
        form.send(player);
        form.setHandler((p, response) -> {
            if (LlamaEconomy.getAPI().getMoney(p.getPlayer()) >= StartupCost){
                LlamaEconomy.getAPI().reduceMoney(p.getUniqueId(), StartupCost);
                if (api.CreateBusiness(response.getInput("BusinessName").getValue(), response.getInput("BusinessDesc").getValue(), p.getPlayer())){
                    SelectMenu form = new SelectMenu();
                    form.initialize(player);
                }
                else {
                    p.sendMessage(PayRollAPI.getLanguage("BusinessNameAlrRegistered"));
                }
            }
            else {
                p.sendMessage(PayRollAPI.getLanguage("OverDraftRisk d"));
            }
        });
    }





}

