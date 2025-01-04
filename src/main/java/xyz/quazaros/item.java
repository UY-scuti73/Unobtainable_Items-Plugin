package xyz.quazaros;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class item {

    String name;
    ArrayList<Material> materials;
    String min_tool_string;
    ArrayList<Material> min_tool;
    Boolean any;
    Boolean enabled;
    String enabled_string;

    public item(String name, List<String> materials_string, String min_tool, String enabled) {
        this.name = name;
        this.materials = string_to_material(materials_string);

        this.min_tool = string_to_tool(min_tool);
        if (this.min_tool.isEmpty()) {
            this.any = true;
            this.min_tool_string = "Any";
        } else {
            this.any = false;
            this.min_tool_string = camel_case(min_tool);
        }

        if (enabled.equalsIgnoreCase("true")) {
            this.enabled = true;
            this.enabled_string = "Enabled";
        } else {
            this.enabled = false;
            this.enabled_string = "Disabled";
        }
    }

    private ArrayList<Material> string_to_material(List<String> materials_string) {
        ArrayList<Material> materials = new ArrayList<>();
        for (String string : materials_string) {
            for (Material material : Material.values()) {
                if (material.name().equalsIgnoreCase(string)) {
                    materials.add(material);
                }
            }
        }
        return materials;
    }

    private ArrayList<Material> string_to_tool(String string) {
        if (string.equalsIgnoreCase("any") || !string.contains(" ")) {
            return new ArrayList<>();
        }

        int space_index = string.indexOf(' ');
        String tier = string.substring(0, space_index);
        String tool = string.substring(space_index + 1);

        String[] tools = {"wooden","stone","golden","iron","diamond","netherite"};
        List<String> tool_list = new ArrayList();
        boolean temp = false;
        for (String t : tools) {
            if (t.equalsIgnoreCase(tier) || temp) {
                tool_list.add(t);
                temp = true;
            }
        }
        for (int i = 0; i < tool_list.size(); i++) {
            tool_list.set(i, tool_list.get(i)+"_"+tool);
        }
        return string_to_material(tool_list);
    }

    private String camel_case(String str) {
        for (int i = 1; i<str.length(); i++){
            if (str.charAt(i) == '_') {
                str = str.substring(0,i) + " "  + str.substring(i+1);
            }
            if (str.charAt(i-1) == ' ') {
                str = str.substring(0,i) + str.substring(i,i+1).toUpperCase() + str.substring(i+1);
            }
        }
        str = str.substring(0,1).toUpperCase() + str.substring(1, str.length());
        return str;
    }
}
