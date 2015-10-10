package gg.uhc.hatremover;

public enum SkinPart {
    CAPE,
    JACKET,
    LEFT_SLEEVE,
    RIGHT_SLEEVE,
    LEFT_PANT_LEG,
    RIGHT_PANT_LEG,
    HAT;

    public int partFlag() {
        return 1 << this.ordinal();
    }
}
