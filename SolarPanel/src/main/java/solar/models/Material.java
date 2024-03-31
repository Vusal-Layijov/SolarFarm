package solar.models;

public enum Material {

    MULTICRYSTALLINE_SILICON("Multicrystalline_silicon"),
    MONOCRYSTALLINE_SILICON("Monocrystalline_silicon"),
    AMORPHOUS_SILICON("Amorphous_silicon"),
    CADMIUM_TELLURIDE("Cadmium_telluride"),
    COPPER_INDIUM_GALLIUM_SELENIDE("Copper_indium_gallium_selenide");
    private String displayText;
    Material (String displayText){this.displayText=displayText;}

    public String getDisplayText() {
        return displayText;
    }
}
