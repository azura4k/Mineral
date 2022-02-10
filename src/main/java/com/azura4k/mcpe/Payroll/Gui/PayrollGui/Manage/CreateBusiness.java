package com.azura4k.mcpe.Payroll.Gui.PayrollGui.Manage;

import cn.nukkit.Player;
import com.azura4k.mcpe.Payroll.PayRollAPI;
import net.lldv.llamaeconomy.LlamaEconomy;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Button;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.handler.CustomFormHandler;

public class CreateBusiness {

    CustomForm form = new CustomForm("Create A Business");
    Double StartupCost = PayRollAPI.PluginConfig.getDouble("IntialStartupCost");
    PayRollAPI api = new PayRollAPI();

    public void initialize(Player player) {

        form.addElement("BusinessName", Input.builder().setName("Business Name").build());
        form.addElement("BusinessDesc", Input.builder().setName("Business Description").build());
        form.addElement("Startup Cost Will Be: " + StartupCost);
        form.send(player);
        form.setHandler((p, response) -> {
            if (LlamaEconomy.getAPI().getMoney(p.getPlayer()) >= StartupCost){
                LlamaEconomy.getAPI().reduceMoney(p.getUniqueId(), StartupCost);
                if (api.CreateBusiness(response.getInput("BusinessName").getValue(), response.getInput("BusinessDesc").getValue(), p.getPlayer())){

                }
                else {
                    p.sendMessage("Business Name Already Took");
                }
            }
            else {
                p.sendMessage("Not Enough Money. Need: " + StartupCost);
            }
        });
    }





}

